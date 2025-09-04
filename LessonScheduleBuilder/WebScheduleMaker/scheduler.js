// --- Core Scheduling Logic ---

/**
 * Represents a single day in the schedule.
 */
class ScheduleEntry {
    constructor(date) {
        this.date = date
        this.lessons = [] // An array of {period, group} objects
    }

    addLesson(period, group) {
        this.lessons.push({ period: `Pd ${period}`, group })
    }
}

/**
 * The main class for building the schedule based on new 25/26 rules.
 */
class ScheduleBuilder {
    constructor(startDate, dayCycle, daysOff, weeks) {
        const startParts = startDate.split("-")
        this.startDate = new Date(
            startParts[0],
            startParts[1] - 1,
            startParts[2]
        )
        this.dayCycle = dayCycle
        this.daysOff = daysOff
            .map((d) => {
                if (!d) return null
                const parts = d.split("-")
                return new Date(parts[0], parts[1] - 1, parts[2]).toDateString()
            })
            .filter(Boolean)
        this.weeks = weeks
        this.schedule = []

        this.LESSON_GROUPS = Array.from({ length: 22 }, (_, i) =>
            String.fromCharCode("A".charCodeAt(0) + i)
        ) // Groups A-V
        this.DAY1_PERIODS = [1, 4, 7, 8]
        this.DAY2_PERIODS = [1, 2, 3, 7, 8]

        this.periodAssignments = {}
        this.LESSON_GROUPS.forEach((g) => (this.periodAssignments[g] = {}))

        this.groupSets = []
        const allGroupsCopy = [...this.LESSON_GROUPS]
        this.groupSets.push(allGroupsCopy.splice(0, 5)) // A-E
        this.groupSets.push(allGroupsCopy.splice(0, 5)) // F-J
        this.groupSets.push(allGroupsCopy.splice(0, 4)) // K-N
        this.groupSets.push(allGroupsCopy.splice(0, 4)) // O-R
        this.groupSets.push(allGroupsCopy.splice(0, 4)) // S-V
    }

    /**
     * Rotates the bundles and the groups within them for the next cycle.
     */
    setupNextGroupCycle() {
        this.groupSets.push(this.groupSets.shift())
        this.groupSets.forEach((set) => {
            if (set.length > 1) {
                set.push(set.shift())
            }
        })
        return this.groupSets.flat()
    }

    /**
     * Finds the best available group for a period.
     * @param {string[]} groupsForCycle - The list of groups remaining in the cycle.
     * @param {Date} date - The current date for scheduling.
     * @param {number} period - The period to schedule for.
     * @param {boolean} isMercySearch - If true, relaxes the 28-day rule and finds the "least bad" option.
     * @returns {object} { group: string, indexInCycle: number }
     */
    findBestGroupForPeriod(groupsForCycle, date, period, isMercySearch) {
        let mercyCandidate = {
            group: "MU",
            indexInCycle: -1,
            longestDaysSince: -1,
        }

        for (let i = 0; i < groupsForCycle.length; i++) {
            const potentialGroup = groupsForCycle[i]
            const lastAssignmentDate =
                this.periodAssignments[potentialGroup][period]

            const daysSince = lastAssignmentDate
                ? (date - lastAssignmentDate) / (1000 * 60 * 60 * 24)
                : Infinity

            if (!isMercySearch) {
                // Standard search: return the first group that satisfies the 28-day rule.
                if (daysSince >= 28) {
                    return {
                        group: potentialGroup,
                        indexInCycle: i,
                    }
                }
            } else {
                // Mercy search: find the group that has waited the longest, regardless of the 28-day rule.
                if (daysSince > mercyCandidate.longestDaysSince) {
                    mercyCandidate = {
                        group: potentialGroup,
                        indexInCycle: i,
                        longestDaysSince: daysSince,
                    }
                }
            }
        }

        // If standard search, we fell through, so no eligible group was found.
        if (!isMercySearch) return { group: "MU", indexInCycle: -1 }

        // If mercy search, return the best candidate we found.
        return mercyCandidate
    }

    /**
     * Generates the entire schedule.
     */
    buildSchedule() {
        let currentDate = new Date(this.startDate.getTime())
        let endDate = new Date(this.startDate.getTime())
        endDate.setDate(endDate.getDate() + this.weeks * 7)

        let groupsForCycle = this.groupSets.flat()
        let weeklyLessonCount = 0

        while (currentDate < endDate) {
            const dayOfWeek = currentDate.getDay()

            if (dayOfWeek === 1) {
                // Monday
                weeklyLessonCount = 0
            }

            const isWeekday = dayOfWeek > 0 && dayOfWeek < 6
            const isDayOff = this.daysOff.includes(currentDate.toDateString())

            if (isWeekday && !isDayOff) {
                const entry = new ScheduleEntry(new Date(currentDate.getTime()))
                const periodsForDay =
                    this.dayCycle % 2 !== 0
                        ? this.DAY1_PERIODS
                        : this.DAY2_PERIODS

                for (const period of periodsForDay) {
                    if (weeklyLessonCount >= 22) {
                        entry.addLesson(period, "MU")
                    } else {
                        if (groupsForCycle.length === 0) {
                            groupsForCycle = this.setupNextGroupCycle()
                        }

                        // --- NEW, ROBUST LOGIC ---

                        // 1. First attempt: Strict search on the rotating group cycle.
                        let assignment = this.findBestGroupForPeriod(
                            groupsForCycle,
                            currentDate,
                            period,
                            false // Strict search
                        )

                        // 2. Second attempt: If the first failed, search ALL groups strictly.
                        if (assignment.group === "MU") {
                            const usedGroupsToday = entry.lessons.map(
                                (l) => l.group
                            )
                            const allAvailableGroups =
                                this.LESSON_GROUPS.filter(
                                    (g) => !usedGroupsToday.includes(g)
                                )

                            assignment = this.findBestGroupForPeriod(
                                allAvailableGroups,
                                currentDate,
                                period,
                                false // Still a strict search
                            )
                        }

                        // 3. Final Fallback: If BOTH strict searches failed, now we use mercy.
                        if (assignment.group === "MU") {
                            assignment = this.findBestGroupForPeriod(
                                groupsForCycle,
                                currentDate,
                                period,
                                true // Mercy search
                            )
                        }

                        // --- END OF NEW LOGIC ---

                        if (assignment.group !== "MU") {
                            entry.addLesson(period, assignment.group)
                            this.periodAssignments[assignment.group][period] =
                                new Date(currentDate.getTime())

                            // Remove the assigned group from the current cycle if it exists there.
                            const indexInCycle = groupsForCycle.indexOf(
                                assignment.group
                            )
                            if (indexInCycle > -1) {
                                groupsForCycle.splice(indexInCycle, 1)
                            }
                        } else {
                            entry.addLesson(period, "MU")
                        }
                    }
                    weeklyLessonCount++
                }
                this.schedule.push(entry)
                this.dayCycle++
            }
            currentDate.setDate(currentDate.getDate() + 1)
        }
        return this.schedule
    }
}
// Note: The UI logic and event listeners that were here are not needed for the unit tests.
// They can be removed from this file if it's used solely for testing.

/**
 * Generates the entire schedule.
 */
