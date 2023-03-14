import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.time.format.DateTimeParseException;

public class MusicalLessonScheduler {
    private static List<List<String>> allGroups;
    // Define a date format pattern for LocalDate parsing and formatting.
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy MM dd");

    public static void main(String[] args) {
        // Create and display the GUI on the event-dispatching thread.
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MusicalLessonScheduler().createAndShowGUI();
        });
    }

    private void createAndShowGUI() {
        // Create the main frame and set its properties.
        JFrame frame = new JFrame("Musical Lesson Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 600));
        frame.setLayout(new BorderLayout());

        // Create the main panel and set its layout.
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Start date panel
        JPanel startDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        startDatePanel.add(new JLabel("Enter start date (YYYY MM DD):"));
        JTextField startDateField = new JTextField(10);
        startDatePanel.add(startDateField);
        mainPanel.add(startDatePanel);

        // Day cycle panel
        JPanel dayCyclePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dayCyclePanel.add(new JLabel("Enter day cycle (1 or 2):"));
        JTextField dayCycleField = new JTextField(2);
        dayCyclePanel.add(dayCycleField);
        mainPanel.add(dayCyclePanel);

        // Weeks panel
        JPanel weeksPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        weeksPanel.add(new JLabel("Enter number of weeks (1-52):"));
        JTextField weeksField = new JTextField(3);
        weeksPanel.add(weeksField);
        mainPanel.add(weeksPanel);

        // Days off panel
        DaysOffPanel daysOffPanel = new DaysOffPanel();
        mainPanel.add(daysOffPanel);

        // Custom schedule checkbox
        JCheckBox customScheduleCheckbox = new JCheckBox("Build custom schedule");
        mainPanel.add(customScheduleCheckbox);

        // Add the main panel to the frame.
        frame.add(mainPanel, BorderLayout.CENTER);

        // Create the start button and its action listener.
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            try {
                // Parse user inputs.
                LocalDate startDate = LocalDate.parse(startDateField.getText(), DATE_FORMAT);
                int dayCycle = Integer.parseInt(dayCycleField.getText());
                int weeks = Integer.parseInt(weeksField.getText());
                List<LocalDate> daysOff = daysOffPanel.getDaysOff();
                boolean customSchedule = customScheduleCheckbox.isSelected();

                // Validate user inputs.
                if (dayCycle != 1 && dayCycle != 2) {
                    JOptionPane.showMessageDialog(frame, "Invalid day cycle. Please enter 1 or 2.");
                    return;
                }

                if (weeks < 1 || weeks > 52) {
                    JOptionPane.showMessageDialog(frame,
                            "Invalid number of weeks. Please enter a value between 1 and 52.");
                    return;
                }

                // Close the main frame.
                frame.dispose();

                // If the custom schedule checkbox is selected, get the custom schedule,
                // otherwise, initialize the groups.
                if (customSchedule) {
                    allGroups = getCustomSchedule();
                } else {
                    allGroups = initializeGroups();
                }

                // Create a ScheduleBuilder object with the user inputs and generate the
                // schedule.
                ScheduleBuilder scheduleBuilder = new ScheduleBuilder(startDate, dayCycle, daysOff, weeks, allGroups);
                List<ScheduleEntry> schedule = scheduleBuilder.buildSchedule();

                // Display the generated schedule in a new window.
                displaySchedule(schedule);
            } catch (Exception ex) {
                // Show an error message if the user inputs are invalid.
                JOptionPane.showMessageDialog(frame, "Invalid input. Please check your entries and try again.");
            }
        });

        // Add the start button to the frame.
        frame.add(startButton, BorderLayout.SOUTH);

        // Set the frame location and make it visible.
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    class DaysOffPanel extends JPanel {
        private List<JTextField> dayOffFields;

        public DaysOffPanel() {
            super(new GridLayout(0, 1));
            dayOffFields = new ArrayList<>();

            // Create the panel for the "+" button and the "Enter day off" line.
            JPanel firstLinePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            // Create the "Enter day off" line.
            JTextField firstDayOffField = new JTextField(10);
            dayOffFields.add(firstDayOffField);
            firstLinePanel.add(new JLabel("Enter day off (YYYY MM DD):"));
            firstLinePanel.add(firstDayOffField);

            // Create the "+" button.
            JButton addDayOffButton = new JButton("+");
            addDayOffButton.addActionListener(e -> addDayOffField());
            firstLinePanel.add(addDayOffButton);

            // Add the panel to the DaysOffPanel.
            add(firstLinePanel);

            add(new JPanel());
        }

        // Method to add a new day off text field.
        private void addDayOffField() {
            JTextField dayOffField = new JTextField(10);
            dayOffFields.add(dayOffField);

            JPanel dayOffPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            dayOffPanel.add(new JLabel("Enter day off (YYYY MM DD):"));
            dayOffPanel.add(dayOffField);
            add(dayOffPanel, getComponentCount() - 1);
            revalidate();
            repaint();
        }

        // Method to get the days off from the text fields.
        public List<LocalDate> getDaysOff() {
            List<LocalDate> daysOff = new ArrayList<>();
            for (JTextField dayOffField : dayOffFields) {
                String dayOffText = dayOffField.getText().trim();
                if (!dayOffText.isEmpty()) {
                    try {
                        LocalDate dayOff = LocalDate.parse(dayOffText, DATE_FORMAT);
                        daysOff.add(dayOff);
                    } catch (DateTimeParseException e) {
                        JOptionPane.showMessageDialog(this, "Invalid date format. Please use the format YYYY MM DD.");
                    }
                }
            }
            return daysOff;
        }
    }

    // Method to get the custom schedule from the user.
    private List<List<String>> getCustomSchedule() {
        List<List<String>> customGroups = new ArrayList<>();

        // Show an informational message to the user about entering the custom group
        // order.
        String message = "You have chosen to start with a custom group order.\n" +
                "Please enter the previous group order in terms of days 1-5 with four groups per day.\n" +
                "Separate each group with an empty space.";
        JOptionPane.showMessageDialog(null, message, "Custom Group Order", JOptionPane.INFORMATION_MESSAGE);

        // Create a new JDialog for the custom group order input.
        JDialog customScheduleDialog = new JDialog((Frame) null, "Custom Group Order", true);
        customScheduleDialog.setLayout(new BorderLayout());
        customScheduleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        customScheduleDialog.setSize(new Dimension(800, 500));

        // Create a JPanel with a grid layout to hold the input fields for each day.
        JPanel customSchedulePanel = new JPanel(new GridLayout(5, 1));
        List<JTextField> groupOrderFields = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            // Create a JPanel for each row (day) and add a label and a text field.
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rowPanel.add(new JLabel("Day " + i + " out of 5:"));
            JTextField groupOrderField = new JTextField(20);
            groupOrderFields.add(groupOrderField);
            rowPanel.add(groupOrderField);
            customSchedulePanel.add(rowPanel);
        }
        // Add the custom schedule panel to the JDialog.
        customScheduleDialog.add(customSchedulePanel, BorderLayout.CENTER);

        // Create a submit button to process the user input and close the JDialog.
        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> {
            // Iterate through the group order fields and store the groups in the
            // customGroups list.
            for (JTextField groupOrderField : groupOrderFields) {
                List<String> groups = Arrays.asList(groupOrderField.getText().split(" "));
                customGroups.add(groups);
            }

            // Show the entered custom group order in a message dialog.
            StringBuilder sb = new StringBuilder("Your custom group order is:\n");
            for (List<String> group : customGroups) {
                sb.append(String.join(" ", group)).append("\n");
            }
            JOptionPane.showMessageDialog(null, sb.toString(), "Custom Group Order", JOptionPane.INFORMATION_MESSAGE);

            // Close the custom schedule JDialog.
            customScheduleDialog.dispose();
        });

        // Add the submit button to the JDialog.
        customScheduleDialog.add(submitButton, BorderLayout.SOUTH);
        customScheduleDialog.setLocationRelativeTo(null);
        customScheduleDialog.setVisible(true);

        return customGroups;
    }

    // Method to initialize the default groups.
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

    // Method to display the generated schedule in a new window.
    private void displaySchedule(List<ScheduleEntry> schedule) {
        String[] columnNames = { "Date", "Period 2", "Group 1", "Period 3/6", "Group 2", "Period 8", "Group 3",
                "Period 9", "Group 4" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add the schedule entries to the table model.
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
        // Create a table with the table model and add it to a scroll pane.
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a frame to display the schedule.
        JFrame frame = new JFrame("Musical Lesson Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(800, 600));

        // Create a save button to export the schedule to a CSV file.
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

        // Add the save button to the bottom panel of the frame.
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(saveButton);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame with the schedule.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Method to export the schedule table to a CSV file.
    private void exportToCSV(JTable table, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            // Write the column names to the CSV file.
            for (int i = 0; i < table.getColumnCount(); i++) {
                fileWriter.write(table.getColumnName(i));
                if (i < table.getColumnCount() - 1) {
                    fileWriter.write(",");
                }
            }
            fileWriter.write("\n");

            // Write the table data to the CSV file.
            for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    fileWriter.write(table.getValueAt(i, j).toString());
                    if (j < table.getColumnCount() - 1) {
                        fileWriter.write(",");
                    }
                }
                fileWriter.write("\n");
            }
            // Show a message that the export was successful.
            JOptionPane.showMessageDialog(null, "Schedule exported to " + file.getAbsolutePath(), "Export Successful",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            // Show an error message if the export failed.
            JOptionPane.showMessageDialog(null, "An error occurred while exporting the schedule.", "Export Failed",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

}
