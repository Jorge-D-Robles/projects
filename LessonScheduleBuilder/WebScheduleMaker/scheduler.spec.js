/**
 * Helper function to assert that no group is scheduled for the same period
 * within a 28-day window.
 * @param {Array<ScheduleEntry>} schedule The generated schedule to test.
 */
const assertNo28DayConflicts = (schedule) => {
    const oneDayInMilliseconds = 1000 * 60 * 60 * 24
    // This object will track the last date a group was assigned to a specific period.
    // Format: { "GroupA": { "Pd 1": Date, "Pd 4": Date }, "GroupB": { ... } }
    const lastSeen = {}

    schedule.forEach((dayEntry) => {
        dayEntry.lessons.forEach((lesson) => {
            const { group, period } = lesson

            // Skip "MU" (make-up) groups as they don't follow the rule.
            if (group.startsWith("MU")) {
                return
            }

            if (!lastSeen[group]) {
                lastSeen[group] = {}
            }

            const lastTimeInPeriod = lastSeen[group][period]

            if (lastTimeInPeriod) {
                const differenceInMs = dayEntry.date - lastTimeInPeriod
                const differenceInDays = Math.round(
                    differenceInMs / oneDayInMilliseconds
                )

                // The core expectation of the test.
                expect(differenceInDays).toBeGreaterThanOrEqual(
                    28,
                    `Group ${group} was scheduled for ${period} again after only ${differenceInDays} days on ${dayEntry.date.toDateString()}`
                )
            }

            // After the check, update the 'last seen' date for this group/period combination.
            lastSeen[group][period] = dayEntry.date
        })
    })
}

describe("ScheduleBuilder", () => {
    // A list of test cases for the 28-day conflict rule.
    const conflictTestCases = [
        {
            description: "when starting on Monday with days off",
            startDate: "2025-09-01", // A Monday
            daysOff: ["2025-09-02", "2025-09-03", "2025-09-15"],
        },
        {
            description: "when starting on Tuesday with multiple days off",
            startDate: "2025-09-02", // A Tuesday
            daysOff: ["2025-09-03", "2025-09-15", "2025-09-16", "2025-09-17"],
        },
        {
            description: "when starting on Wednesday with no days off",
            startDate: "2025-09-03", // A Wednesday
            daysOff: [],
        },
        {
            description: "when starting on Thursday with days off",
            startDate: "2025-09-04", // A Thursday
            daysOff: ["2025-09-05", "2025-09-06", "2025-09-07"],
        },
    ]

    // Dynamically create a test for each case in the list above.
    conflictTestCases.forEach((testCase) => {
        it(`should not schedule a group for the same period within 28 days ${testCase.description}`, () => {
            const scheduleBuilder = new ScheduleBuilder(
                testCase.startDate,
                1, // startCycle
                testCase.daysOff,
                16 // weeks
            )

            const schedule = scheduleBuilder.buildSchedule()

            assertNo28DayConflicts(schedule)
        })
    })

    it("should not schedule any lessons on weekends", () => {
        const scheduleBuilder = new ScheduleBuilder("2025-09-01", 1, [], 4)
        const schedule = scheduleBuilder.buildSchedule()

        schedule.forEach((dayEntry) => {
            const dayOfWeek = dayEntry.date.getDay() // 0 = Sunday, 6 = Saturday
            expect(dayOfWeek).not.toBe(
                0,
                `A lesson was scheduled on a Sunday: ${dayEntry.date.toDateString()}`
            )
            expect(dayOfWeek).not.toBe(
                6,
                `A lesson was scheduled on a Saturday: ${dayEntry.date.toDateString()}`
            )
        })
    })

    it("should not schedule any lessons on specified days off", () => {
        const dayOff = "2025-09-10" // A Wednesday
        const scheduleBuilder = new ScheduleBuilder(
            "2025-09-08",
            1,
            [dayOff],
            2
        )
        const schedule = scheduleBuilder.buildSchedule()

        const dayOffDateString = new Date(dayOff + "T00:00:00").toDateString()

        schedule.forEach((dayEntry) => {
            expect(dayEntry.date.toDateString()).not.toBe(
                dayOffDateString,
                `A lesson was scheduled on a specified day off: ${dayOffDateString}`
            )
        })
    })
})
