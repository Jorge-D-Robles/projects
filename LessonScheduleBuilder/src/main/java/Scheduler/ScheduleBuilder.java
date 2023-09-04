package Scheduler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScheduleBuilder {
    private final boolean customSchedule;
    private final LocalDate startDate;
    private int dayCycle;
    private final List<LocalDate> daysOff;
    private final int weeks;
    private final List<List<String>> allGroups;
    private final List<ScheduleEntry> schedule;

    private int group_randomizer = 0, group_change = 0, group_index = 0, total_changes = 0;

    public ScheduleBuilder(LocalDate startDate, int dayCycle, List<LocalDate> daysOff, int weeks, boolean customSchedule,
                           List<List<String>> allGroups) {
        this.startDate = startDate;
        this.dayCycle = dayCycle;
        this.daysOff = daysOff;
        this.weeks = weeks;
        this.allGroups = allGroups;
        this.customSchedule = customSchedule;
        this.schedule = new ArrayList<>();
    }

    private void rotateList(List<String> list) {
        Collections.rotate(list, 1);

    }

    private void shuffleGroups() {
        Collections.shuffle(allGroups);
    }

    private String findGroup() {
        String group = null;
        group_randomizer++;
        // rotates the 5 subgroups of 4 after each letter has gone through
        if (group_randomizer == 21) {
            shuffleGroups();
            group_randomizer = 1;
        }
        // to keep groups un-randomized the first loop of 20 groups. base case
        if (total_changes < 20 && !customSchedule)
            group = allGroups.get(group_index).get(group_change);

        if (customSchedule && group_change == 0) {
            rotateList(allGroups.get(group_index));
            group = allGroups.get(group_index).get(group_change);
        }
        //begin rotating the letters in the subgroups. ABCD -> DABC -> CDAB -> BCDA -> ABCD
        else if (group_change == 0 && total_changes >= 20 && !customSchedule) {
            rotateList(allGroups.get(group_index));
            group = allGroups.get(group_index).get(group_change);
        }
        else if (group_change > 0 && group_change < 5)
            group = allGroups.get(group_index).get(group_change);
        total_changes++;
        group_change++;

        if (group_change == 4) {
            group_index++;
            group_change = 0;
            if (group_index == 5)
                group_index = 0;
        }

        return group;
    }
    public List<ScheduleEntry> buildSchedule() {
        LocalDate currentDate = startDate;
        LocalDate endDate = startDate.plusWeeks(weeks);

        while (!currentDate.equals(endDate)) {
            if (daysOff.contains(currentDate) || currentDate.getDayOfWeek().getValue() > 5) {
                currentDate = currentDate.plusDays(1);
                continue;
            }
            ScheduleEntry entry = new ScheduleEntry();
            entry.setDate(currentDate);
            for (int i = 0; i < 4; i++) {
                // Get the group for this period
                String group = findGroup();

                // Set the period and group
                entry.setPeriod(dayCycle, i + 1, group);
            }
            schedule.add(entry);
            currentDate = currentDate.plusDays(1);
            dayCycle += 1;
        }

        return schedule;
    }

}
