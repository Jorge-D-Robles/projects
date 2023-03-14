import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MusicalLessonScheduler {
    private static List<List<String>> allGroups;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy MM dd");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MusicalLessonScheduler().start();
        });
    }

    private void start() {
        LocalDate startDate = getStartDate();
        int dayCycle = getDayCycle(startDate);
        List<LocalDate> daysOff = getDaysOff();
        int weeks = getWeeks();
        boolean custom = isCustom();
        if (!custom) {
            allGroups = initializeGroups();
        } else {
            allGroups = getCustomSchedule();
        }
        System.out.println(allGroups);
        ScheduleBuilder scheduleBuilder = new ScheduleBuilder(startDate, dayCycle, daysOff, weeks, allGroups);
        List<ScheduleEntry> schedule = scheduleBuilder.buildSchedule();
        displaySchedule(schedule);
    }

    private static List<List<String>> getCustomSchedule() {
        List<List<String>> customGroups = new ArrayList<>();

        String message = "You have chosen to start with a custom group order.\n" +
                "Please enter the previous group order in terms of days 1-5 with four groups per day.\n" +
                "Separate each group with an empty space.";

        JOptionPane.showMessageDialog(null, message, "Custom Group Order", JOptionPane.INFORMATION_MESSAGE);

        for (int i = 1; i <= 5; i++) {
            String input = JOptionPane.showInputDialog(null, "Day " + i + " out of 5:", "Custom Group Order",
                    JOptionPane.QUESTION_MESSAGE);
            List<String> groups = Arrays.asList(input.split(" "));
            customGroups.add(groups);
        }

        StringBuilder sb = new StringBuilder("Your custom group order is:\n");
        for (List<String> group : customGroups) {
            sb.append(String.join(" ", group)).append("\n");
        }

        JOptionPane.showMessageDialog(null, sb.toString(), "Custom Group Order", JOptionPane.INFORMATION_MESSAGE);

        return customGroups;
    }

    private List<List<String>> initializeGroups() {
        List<List<String>> allGroups = new ArrayList<>();
        char groupName = 'A';

        for (int i = 0; i < 5; i++) {
            List<String> group = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                group.add(Character.toString(groupName++));
            }
            allGroups.add(group);
        }
        return allGroups;
    }

    private LocalDate getStartDate() {
        while (true) {
            String input = JOptionPane.showInputDialog(null, "Enter date in YYYY MM DD format:");
            try {
                return LocalDate.parse(input, DATE_FORMAT);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Wrong input. Please try again.");
            }
        }
    }

    private int getDayCycle(LocalDate startDate) {
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "Is " + startDate + " a day 1 or day 2? Type 1 or 2 only:");
            try {
                int dayCycle = Integer.parseInt(input.trim());
                if (dayCycle == 1 || dayCycle == 2) {
                    return dayCycle;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
            }
        }
    }

    private List<LocalDate> getDaysOff() {
        List<LocalDate> daysOff = new ArrayList<>();
        while (true) {
            int result = JOptionPane.showConfirmDialog(null, "Would you like to add a day off?", "",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                daysOff.add(getStartDate());
            } else {
                break;
            }
        }
        return daysOff.stream().sorted().collect(Collectors.toList());
    }

    private int getWeeks() {
        while (true) {
            String input = JOptionPane.showInputDialog(null,
                    "How many weeks would you like to schedule? Please enter a number between 1 - 52:");
            try {
                int weeks = Integer.parseInt(input.trim());
                if (weeks > 0 && weeks <= 52) {
                    return weeks;
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please try again.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Invalid input. Try again.");
            }
        }
    }

    private boolean isCustom() {
        int result = JOptionPane.showConfirmDialog(null, "Would you like to build a custom schedule?", "",
                JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    private void displaySchedule(List<ScheduleEntry> schedule) {
        String[] columnNames = { "Date", "Period 2", "Group 1", "Period 3/6", "Group 2", "Period 8", "Group 3",
                "Period 9", "Group 4" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        for (ScheduleEntry entry : schedule) {
            Object[] rowData = {
                    entry.getDate(),
                    entry.getPeriod2(),
                    entry.getGroup1(),
                    entry.getPeriod3or6(),
                    entry.getGroup2(),
                    entry.getPeriod8(),
                    entry.getGroup3(),
                    entry.getPeriod9(),
                    entry.getGroup4()
            };
            model.addRow(rowData);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Musical Lesson Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(800, 600));

        JButton saveButton = new JButton("Save to CSV");
        saveButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Specify a file to save the schedule");
            FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
            fileChooser.addChoosableFileFilter(filter);
            fileChooser.setFileFilter(filter);

            int userSelection = fileChooser.showSaveDialog(frame);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                    fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
                }
                exportToCSV(table, fileToSave);
            }
        });

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveButton);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void exportToCSV(JTable table, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                fileWriter.write(table.getColumnName(i));
                if (i < table.getColumnCount() - 1) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write("\n");

            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    fileWriter.write(table.getValueAt(i, j).toString());
                    if (j < table.getColumnCount() - 1) {
                        fileWriter.write(",");
                    }
                }
                fileWriter.write("\n");
            }

            JOptionPane.showMessageDialog(null, "Schedule exported to " + file.getAbsolutePath(), "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "An error occurred while exporting the schedule.", "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
