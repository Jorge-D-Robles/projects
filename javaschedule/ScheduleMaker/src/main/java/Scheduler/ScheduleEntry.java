package Scheduler;
import java.time.LocalDate;

public class ScheduleEntry {
    private LocalDate date;
    private String period2;
    private String group1;
    private String period3or6;
    private String group2;
    private String period8;
    private String group3;
    private String period9;
    private String group4;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPeriod2() {
        return period2;
    }

    public String getGroup1() {
        return group1;
    }

    public String getPeriod3or6() {
        return period3or6;
    }

    public String getGroup2() {
        return group2;
    }

    public String getPeriod8() {
        return period8;
    }

    public String getGroup3() {
        return group3;
    }

    public String getPeriod9() {
        return period9;
    }

    public String getGroup4() {
        return group4;
    }

    public void setPeriod(int dayCycle, int periodIndex, String group) {
        String period = "";
        switch (periodIndex) {
            case 1:
                period = "Period 2";
                break;
            case 2:
                period = dayCycle % 2 == 1 ? "Period 6" : "Period 3";
                break;
            case 3:
                period = "Period 8";
                break;
            case 4:
                period = "Period 9";
                break;
        }
        if (periodIndex == 1) {
            period2 = period;
            group1 = group;
        } else if (periodIndex == 2) {
            period3or6 = period;
            group2 = group;
        } else if (periodIndex == 3) {
            period8 = period;
            group3 = group;
        } else if (periodIndex == 4) {
            period9 = period;
            group4 = group;
        }
    }

    @Override
    public String toString() {
        return "ScheduleEntry{" +
                "date=" + date +
                ", period2='" + period2 + '\'' +
                ", group1='" + group1 + '\'' +
                ", period3or6='" + period3or6 + '\'' +
                ", group2='" + group2 + '\'' +
                ", period8='" + period8 + '\'' +
                ", group3='" + group3 + '\'' +
                ", period9='" + period9 + '\'' +
                ", group4='" + group4 + '\'' +
                '}';
    }
}
