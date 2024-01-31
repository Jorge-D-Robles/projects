package Scheduler;
import java.time.LocalDate;

public class ScheduleEntry {
    private int dayCycle; // Add this line
    private LocalDate date;
    private String period1;
    private String group1;
    private String period2;
    private String group2;
    private String period3;
    private String group3;
    private String period4;
    private String group4;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
    // The getters in this program must be modified every year. This is because the schedule changes every year.
    // Since the algorithm rotates groups numbered 1,2,3,4, if there are days were say period 4 is shared day 1 and day 2, but is out of order in the algorithm, there will be bugs.
    // by modifying the getters, we solve this problem by making sure that the algorithm is always in order and simply returning the correct order without having to modify the algorithm or
    // table generator every year.
    public String getPeriod1() {
        return dayCycle % 2 == 1 ? period1 : period3;
    }

    public String getGroup1() {
        return dayCycle % 2 == 1 ? group1 : group3;
    }

    public String getPeriod2() {
        return dayCycle % 2 == 1 ? period2 : period1;
    }

    public String getGroup2() {
        return dayCycle % 2 == 1 ? group2 : group1;
    }

    public String getPeriod3() {
        return dayCycle % 2 == 1 ? period3 : period2;
    }

    public String getGroup3() {
        return dayCycle % 2 == 1 ? group3 : group2;
    }

    public String getPeriod4() {
        return period4; // Period 9 on all days
    }

    public String getGroup4() {
        return group4; // Group 4 on all days since period 9 is all days
    }



    public void setPeriod(int dayCycle, int periodIndex, String group) {
        this.dayCycle = dayCycle; // Update the dayCycle member
        String period = "";
        switch (periodIndex) {
            case 1:
                period = dayCycle % 2 == 1 ? "Period 1" : "Period 6";
                break;
            case 2:
                period = dayCycle % 2 == 1 ? "Period 3" : "Period 8";
                break;
            case 3:
                period = "Period 4";
                break;
            case 4:
                period = "Period 9";
                break;
        }
        if (periodIndex == 1) {
            period1 = period;
            group1 = group;
        } else if (periodIndex == 2) {
            period2 = period;
            group2 = group;
        } else if (periodIndex == 3) {
            period3 = period;
            group3 = group;
        } else if (periodIndex == 4) {
            period4 = period;
            group4 = group;
        }
    }

    @Override
    public String toString() {
        return "ScheduleEntry{" +
                "date=" + date +
                ", period2='" + period1 + '\'' +
                ", group1='" + group1 + '\'' +
                ", period3or6='" + period2 + '\'' +
                ", group2='" + group2 + '\'' +
                ", period8='" + period3 + '\'' +
                ", group3='" + group3 + '\'' +
                ", period9='" + period4 + '\'' +
                ", group4='" + group4 + '\'' +
                '}';
    }
}
