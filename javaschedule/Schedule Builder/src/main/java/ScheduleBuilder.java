import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JOptionPane;

public class ScheduleBuilder {

    public static void main(String[] args) {
        List<String[]> allGroups = new ArrayList<>();
        boolean custom = true;

        JOptionPane.showMessageDialog(null,
                "Welcome to the musical lesson schedule builder. To start, please enter a starting date.");

        LocalDate startDate;
        while (true) {
            startDate = getDate();
            String message = "Your start date is " + startDate.format(DateTimeFormatter.ISO_DATE) + ". Are these days correct?";
            if (agree(message)) {
                break;
            }
        }

        int dayCycle;
        while (true) {
            String input = JOptionPane.showInputDialog(
                    "Is " + startDate.format(DateTimeFormatter.ISO_DATE) + " a day 1 or day 2? Type 1 or 2 only:");
            try {
                dayCycle = Integer.parseInt(input.trim());

                if (dayCycle == 1 || dayCycle == 2) {
                    String message = "Your start date is " + startDate.format(DateTimeFormatter.ISO_DATE) + " and it is a day " + dayCycle + ". Are these days correct?";
                    if (agree(message)) {
                        break;
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
            }
        }

        List<LocalDate> daysOff = new ArrayList<>();
        while (true) {
            try {
                int response = JOptionPane.showConfirmDialog(null, "Next, you may add any potential days off within the next sixty days. Would you like to add a day off?");
                if (response == JOptionPane.NO_OPTION) {
                    break;
                } else if (response == JOptionPane.YES_OPTION) {
                    LocalDate dayOff = getDate();
                    daysOff.add(dayOff);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
            }
        }

        int weeks;
        while (true) {
            try {
                String input = JOptionPane.showInputDialog("How many weeks would you like to schedule? Please enter a number between 1 - 52:");
                weeks = Integer.parseInt(input.trim());
                if (weeks > 0 && weeks < 52) {
                    JOptionPane.showMessageDialog(null, "You have chosen to schedule " + weeks + " weeks.");
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
            }
        }

        JOptionPane.showMessageDialog(null, "Finally, you may choose to start with a custom group order or the default group order.");
        String order = JOptionPane.showInputDialog("Would you like to build a custom schedule? y/n:").trim().toLowerCase();
        if (order.contains("y")) {
            customStart(allGroups);
            randomizeGroup(allGroups);
            custom = true;
        } else {
            regularStart(allGroups);
            custom = false;
        }
    }
    public static LocalDate getDate() {
        LocalDate date = null;
        while (date == null) {
            String dateString = JOptionPane.showInputDialog(null,
                    "Enter date in YYYY MM DD format:");
            try {
                String[] dateSplit = dateString.split(" ");
                int year = Integer.parseInt(dateSplit[0]);
                int month = Integer.parseInt(dateSplit[1]);
                int day = Integer.parseInt(dateSplit[2]);
                date = LocalDate.of(year, month, day);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
            }
        }
        return date;
    }

    public static boolean confirmDate(LocalDate date) {
        int dialogResult = JOptionPane.showConfirmDialog(null,
                "Your start date is " + date + ".\nAre these days correct?");
        return dialogResult == JOptionPane.YES_OPTION;
    }
    public static boolean confirmDate(LocalDate date) {
        Object[] options = {"Yes", "No"};
        int input = JOptionPane.showOptionDialog(null, "Your start date is " + date + ". Is this correct?", "Confirm Start Date", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return input == 0;
    }

    public static int getDayCycle(LocalDate date) {
        Object[] options = {"Day 1", "Day 2"};
        int input = JOptionPane.showOptionDialog(null, "Is " + date + " a Day 1 or Day 2?", "Select Day Cycle", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return input + 1;
    }

    public static List<LocalDate> getDaysOff() {
        List<LocalDate> daysOff = new ArrayList<>();
        String input;
        do {
            input = JOptionPane.showInputDialog(null, "Enter a day off in YYYY MM DD format, or type 'done' to finish:");
            if (!input.equals("done")) {
                try {
                    String[] dateInput = input.split(" ");
                    int year = Integer.parseInt(dateInput[0]);
                    int month = Integer.parseInt(dateInput[1]);
                    int day = Integer.parseInt(dateInput[2]);
                    LocalDate date = LocalDate.of(year, month, day);
                    daysOff.add(date);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException | DateTimeException e) {
                    JOptionPane.showMessageDialog(null, "Invalid date format. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } while (!input.equals("done"));
        return daysOff;
    }

    public static int getWeeks() {
        int weeks = 0;
        boolean validInput = false;
        while (!validInput) {
            String input = JOptionPane.showInputDialog(null, "How many weeks would you like to schedule? Please enter a number between 1 and 52:");
            try {
                weeks = Integer.parseInt(input);
                if (weeks >= 1 && weeks <= 52) {
                    validInput = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return weeks;
    }

    public static boolean isCustomSchedule() {
        Object[] options = {"Yes", "No"};
        int input = JOptionPane.showOptionDialog(null, "Would you like to build a custom schedule?", "Custom Schedule", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return input == 0;
    }

    public static void customStart(List<String[]> allGroups) {
        String[] day1 = new String[4];
        String[] day2 = new String[4];
        String[] day3 = new String[4];
        String[] day4 = new String[4];
        String[] day5 = new String[4];
        boolean validInput = false;
        while (!validInput) {
            String userInput = JOptionPane.showInputDialog(null, "Enter the number of courses you want to schedule (maximum 10): ");
            if (userInput == null) {
                System.exit(0);
            }
            try {
                numCourses = Integer.parseInt(userInput);
                if (numCourses < 1 || numCourses > 10) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 1 and 10.");
                } else {
                    validInput = true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number between 1 and 10.");
            }
        }

        // Initialize course array
        Course[] courses = new Course[numCourses];

        // Get course information from user
        for (int i = 0; i < numCourses; i++) {
            validInput = false;
            while (!validInput) {
                String courseName = JOptionPane.showInputDialog(null, "Enter the name of course #" + (i + 1) + ":");
                if (courseName == null) {
                    System.exit(0);
                }
                if (courseName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Course name cannot be empty.");
                } else {
                    courses[i] = new Course(courseName);
                    validInput = true;
                }
            }

            validInput = false;
            while (!validInput) {
                String daysPrompt = JOptionPane.showInputDialog(null, "Enter the days of the week this course meets (MTWRF): ");
                if (daysPrompt == null) {
                    System.exit(0);
                }
                String days = daysPrompt.toUpperCase();
                if (!days.matches("^[MTWRF]+$")) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter the days of the week this course meets (MTWRF):");
                } else {
                    courses[i].setDays(days);
                    validInput = true;
                }
            }

            validInput = false;
            while (!validInput) {
                String startTimePrompt = JOptionPane.showInputDialog(null, "Enter the start time of this course (in format HH:MM AM/PM): ");
                if (startTimePrompt == null) {
                    System.exit(0);
                }
                try {
                    LocalTime startTime = LocalTime.parse(startTimePrompt, DateTimeFormatter.ofPattern("hh:mm a"));
                    courses[i].setStartTime(startTime);
                    validInput = true;
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter the start time of this course (in format HH:MM AM/PM):");
                }
            }

            validInput = false;
            while (!validInput) {
                String durationPrompt = JOptionPane.showInputDialog(null, "Enter the duration of this course (in minutes): ");
                if (durationPrompt == null) {
                    System.exit(0);
                }
                try {
                    int duration = Integer.parseInt(durationPrompt);
                    if (duration <= 0) {
                        JOptionPane.showMessageDialog(null, "Invalid input. Please enter a positive integer.");
                    } else {
                        courses[i].setDuration(duration);
                        validInput = true;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please enter a positive integer.");
                }
            }
        }
        // Build schedule
        ScheduleBuilder builder = new ScheduleBuilder();
        Schedule schedule = builder.buildSchedule(courses);
        // Export schedule to CSV
        String filePath = "schedule.csv";
        try {
            FileWriter csvWriter = new FileWriter(filePath);
            csvWriter.append("Time,Monday,Tuesday,Wednesday,Thursday,Friday\n");

            for (int i = 0; i < 13; i++) {
                String[] row = new String[6];
                row[0] = startTime.plusHours(i).toString();
                for (int j = 0; j < 5; j++) {
                    row[j+1] = schedule[j][i];
                }
                csvWriter.append(String.join(",", row) + "\n");
            }

            csvWriter.flush();
            csvWriter.close();

            JOptionPane.showMessageDialog(null, "Schedule exported to CSV: " + filePath);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error exporting schedule to CSV.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        buildSchedule();
    }
}
