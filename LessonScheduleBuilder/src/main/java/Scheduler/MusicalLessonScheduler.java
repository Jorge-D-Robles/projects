package Scheduler;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DragSource;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MusicalLessonScheduler {
    private static List<List<String>> allGroups;
    private static LocalDate startDate;
    private static int dayCycle;
    private static int weeks;
    private static List<LocalDate> daysOff;
    private static boolean customSchedule;

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
        JFrame frame = new JFrame("Musical Lesson Scheduler by Jorge Robles Version Mar 2024");
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
                startDate = LocalDate.parse(startDateField.getText(), DATE_FORMAT);
                dayCycle = Integer.parseInt(dayCycleField.getText());
                weeks = Integer.parseInt(weeksField.getText());
                daysOff = daysOffPanel.getDaysOff();
                customSchedule = customScheduleCheckbox.isSelected();

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

                ScheduleBuilder scheduleBuilder = new ScheduleBuilder(startDate, dayCycle, daysOff, weeks, customSchedule, allGroups);

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

    static class DaysOffPanel extends JPanel {
        private final List<JTextField> dayOffFields;

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
        customScheduleDialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        customScheduleDialog.setLayout(new BorderLayout());
        customScheduleDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        customScheduleDialog.setSize(new Dimension(1200, 500));

        // Create a JPanel with a grid layout to hold the input fields for each day.
        JPanel customSchedulePanel = new JPanel(new BorderLayout());
        JPanel inputFieldsPanel = new JPanel(new GridLayout(5, 1));
        List<JTextField> groupOrderFields = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            // Create a JPanel for each row (day) and add a label and a text field.
            JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            rowPanel.add(new JLabel("Day " + i + " out of 5:"));
            JTextField groupOrderField = new JTextField(20);
            groupOrderFields.add(groupOrderField);
            rowPanel.add(groupOrderField);
            inputFieldsPanel.add(rowPanel);
        }
        customSchedulePanel.add(inputFieldsPanel, BorderLayout.WEST);

        // Create a JTextArea to show the live custom group order.
        JTextArea liveOrderDisplay = new JTextArea(10, 30);
        liveOrderDisplay.setEditable(false);
        JScrollPane liveOrderScrollPane = new JScrollPane(liveOrderDisplay);
        customSchedulePanel.add(liveOrderScrollPane, BorderLayout.EAST);

        // Create a submit button to process the user input and close the JDialog.
        JButton submitButton = new JButton("Submit");
        submitButton.setEnabled(false); // Disable the submit button by default

        // Add a DocumentListener to the input fields to update the live custom group order.
        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateLiveOrder();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateLiveOrder();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateLiveOrder();
            }

            private void updateLiveOrder() {
                StringBuilder sb = new StringBuilder("Live custom group order:\nRemember, please only input 4 groups per day/line.\n");
                int lineNumber = 1;
                boolean allValid = true;
                for (JTextField groupOrderField : groupOrderFields) {
                    String text = groupOrderField.getText();
                    sb.append("Day #").append(lineNumber++).append(": ").append(text).append("\n");
                    List<String> groups = Arrays.asList(text.split(" "));
                    if (groups.size() != 4) {
                        allValid = false;
                    }
                }
                submitButton.setEnabled(allValid);
                liveOrderDisplay.setText(sb.toString());
            }
        };
        for (JTextField groupOrderField : groupOrderFields) {
            groupOrderField.getDocument().addDocumentListener(documentListener);
        }

        // Add the custom schedule panel to the JDialog.
        customScheduleDialog.add(customSchedulePanel, BorderLayout.CENTER);

        // Add the submit button action listener.
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
        String[] columnNames = { "Date", "Period", "Group 1", "Period", "Group 2", "Period", "Group 3",
                "Period", "Group 4" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        // Add the schedule entries to the table model.
        model.addRow(new Object[] { "Note:", "Pd 1 = 6", "Pd 3 = 8", "Pd 4 = 4", "Pd 9 = 9", "when", "swapping", "day 1", "to day 2" });
        int counter = -1;
        for (ScheduleEntry entry : schedule) {
            counter++;
            if (counter % 5 == 0) {
                model.addRow(new Object[] { "", "", "", "", "", "", "", "", "" });
            }
            Object[] rowData = {
                    entry.getDate(),
                    entry.getPeriod1(),
                    entry.getGroup1(),
                    entry.getPeriod2(),
                    entry.getGroup2(),
                    entry.getPeriod3(),
                    entry.getGroup3(),
                    entry.getPeriod4(),
                    entry.getGroup4()
            };
            model.addRow(rowData);
        }
        // Create a table with the table model and add it to a scroll pane.
        JTable table = new JTable(model);
        table.setDragEnabled(true);
        table.setDropMode(DropMode.INSERT_ROWS);
        table.setTransferHandler(new TableRowTransferHandler(table));
        JScrollPane scrollPane = new JScrollPane(table);

        // Create a frame to display the schedule.
        JFrame frame = new JFrame("Musical Lesson Schedule");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.setPreferredSize(new Dimension(1100, 800));

        // Create a reroll button to reroll schedule.
        JButton ReRollButton = new JButton("Re-Roll");
        ReRollButton.addActionListener(e -> {
            frame.dispose();
            reRollSchedule(new ScheduleBuilder(startDate, dayCycle, daysOff, weeks, customSchedule, allGroups));
        });
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
        bottomPanel.add(ReRollButton);
        bottomPanel.add(saveButton);
        frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

        // Display the frame with the schedule.
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Create a re-roll button to re-roll the schedule.
    private void reRollSchedule(ScheduleBuilder newBuilder) {
        List<ScheduleEntry> newSched = newBuilder.buildSchedule();
        displaySchedule(newSched);
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



    public class TableRowTransferHandler extends TransferHandler {
        // A custom DataFlavor to manage the type of data being transferred. Here it's an Integer representing the row index.
        private final DataFlavor localObjectFlavor = new DataFlavor(Integer.class, "Integer Row Index");
        private JTable table = null;

        // Constructor that takes a JTable. This handler will be associated with this table.
        public TableRowTransferHandler(JTable table) {
            this.table = table;
        }

        // Called to create a Transferable object when a drag starts.
        @Override
        protected Transferable createTransferable(JComponent c) {
            assert (c == table); // Ensure that the component is the table.
            // Return a new Transferable implementation where getTransferData will return the selected row index.
            return new Transferable() {
                @Override
                public DataFlavor[] getTransferDataFlavors() {
                    // Return the array of supported data flavors (types); in this case, just the custom row index flavor.
                    return new DataFlavor[]{localObjectFlavor};
                }

                @Override
                public boolean isDataFlavorSupported(DataFlavor flavor) {
                    // Check if the data flavor is supported (i.e., if it matches our custom flavor).
                    return localObjectFlavor.equals(flavor);
                }

                @Override
                public Object getTransferData(DataFlavor flavor) {
                    // Return the data being transferred; here, it's the index of the selected row.
                    return table.getSelectedRow();
                }
            };
        }

        // Method to determine if the component can accept the data being transferred.
        @Override
        public boolean canImport(TransferSupport info) {
            // Check if the component in the TransferSupport is the table and if the data flavor is supported.
            boolean b = info.getComponent() == table && info.isDrop() && info.isDataFlavorSupported(localObjectFlavor);
            // Change the cursor to indicate if the data can be dropped here.
            table.setCursor(b ? DragSource.DefaultMoveDrop : DragSource.DefaultMoveNoDrop);
            return b;
        }

        // Specify the actions supported by this handler; in this case, moving of rows.
        @Override
        public int getSourceActions(JComponent c) {
            return TransferHandler.MOVE;
        }

        // Method to handle data import when a drop occurs.
        @Override
        public boolean importData(TransferSupport info) {
            // First, check if the data can be imported into this component.
            if (!canImport(info)) {
                return false;
            }

            // Get the JTable component where the drop event occurred.
            JTable target = (JTable) info.getComponent();
            // Get the drop location details within the table.
            JTable.DropLocation dl = (JTable.DropLocation) info.getDropLocation();
            // Determine the row index at the drop location.
            int index = dl.getRow();
            // Get the total row count of the table model.
            int max = table.getModel().getRowCount();
            // Ensure the index is within the valid range of the table's rows.
            if (index < 0 || index > max) {
                index = max;
            }

            // Initialize a variable to store the index of the row being dragged.
            int rowFrom = -1;
            try {
                // Attempt to retrieve the dragged row index from the Transferable object.
                rowFrom = (Integer) info.getTransferable().getTransferData(localObjectFlavor);
            } catch (Exception e) {
                // In case of any exception, print the stack trace for debugging.
                e.printStackTrace();
            }

            // Adjust the drop index if moving a row downwards in the table.
            // This compensates for the change in indices as a row is moved.
            if (rowFrom != -1 && rowFrom < index) {
                index--;
            }

            // Perform the row move operation if the dragged row is different from the drop index.
            if (rowFrom != -1 && rowFrom != index) {
                // Move the row in the table model.
                ((DefaultTableModel) table.getModel()).moveRow(rowFrom, rowFrom, index);
                // Adjust the index for selection if needed.
                if (index > rowFrom && index < table.getRowCount() - 1) {
                    index--;
                }
                // Update the table selection to reflect the new position of the moved row.
                target.getSelectionModel().addSelectionInterval(index, index);
                return true;
            }

            // Return false if no valid move operation was performed.
            return false;
        }



        // Method called after the transfer is complete.
        @Override
        protected void exportDone(JComponent c, Transferable t, int act) {
            if (act == TransferHandler.MOVE) {
                // Reset the cursor to the default cursor once the drag-and-drop operation is complete.
                table.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }


}
