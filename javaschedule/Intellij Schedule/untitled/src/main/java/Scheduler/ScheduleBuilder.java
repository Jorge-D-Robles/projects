package Scheduler;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ScheduleBuilder {
    private LocalDate startDate;
    private int dayCycle;
    private List<LocalDate> daysOff;
    private int weeks;
    private List<List<String>> allGroups;
    private List<ScheduleEntry> schedule;

    public ScheduleBuilder(LocalDate startDate, int dayCycle, List<LocalDate> daysOff, int weeks,
                           List<List<String>> allGroups) {
        this.startDate = startDate;
        this.dayCycle = dayCycle;
        this.daysOff = daysOff;
        this.weeks = weeks;
        this.allGroups = allGroups;
        this.schedule = new ArrayList<>();
    }

    private List<String> rotateList(List<String> list) {
        List<String> rotatedList = new ArrayList<>(list);
        Collections.rotate(rotatedList, 1);
        return rotatedList;
    }

    private void shuffleGroups() {
        Collections.shuffle(allGroups);
    }

    public List<ScheduleEntry> buildSchedule() {
        LocalDate currentDate = startDate;
        LocalDate endDate = startDate.plusWeeks(weeks);
        int groupRotationCounter = 0;

        while (!currentDate.equals(endDate)) {
            if (daysOff.contains(currentDate) || currentDate.getDayOfWeek().getValue() > 5) {
                currentDate = currentDate.plusDays(1);
                continue;
            }
            ScheduleEntry entry = new ScheduleEntry();
            entry.setDate(currentDate);
            for (int i = 0; i < 4; i++) {
                int groupIndex = (dayCycle - 1) % allGroups.get(i).size();

                if (dayCycle % 2 == 1) {
                    entry.setPeriod(dayCycle, i + 1, allGroups.get(i).get(groupIndex));
                } else {
                    entry.setPeriod(dayCycle, i + 1, allGroups.get(i).get(groupIndex));
                }
            }
            schedule.add(entry);
            currentDate = currentDate.plusDays(1);
            dayCycle += 1;
            groupRotationCounter += 1;

            if (groupRotationCounter % 20 == 0) {
                for (int i = 0; i < allGroups.size(); i++) {
                    List<String> rotatedGroup = rotateList(allGroups.get(i));
                    allGroups.set(i, rotatedGroup);
                }
                shuffleGroups();
            }
        }

        return schedule;
    }

}
