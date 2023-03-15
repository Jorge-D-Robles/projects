# MusicalLessonScheduler

`MusicalLessonScheduler` is a Java Swing application that allows users to generate a musical lesson schedule based on user input. The application provides an interactive graphical user interface (GUI) for entering the required data, generates a schedule, displays the schedule in a table, and allows the user to export the schedule to a CSV file.

## Features

* Customizable days off
* Customizable group order
* Generates and displays a musical lesson schedule
* Export generated schedule to a CSV file

## Usage

To use the `MusicalLessonScheduler` class, follow these steps:

1. Create an instance of the `MusicalLessonScheduler` class:

```java
MusicalLessonScheduler scheduler = new MusicalLessonScheduler();
Run the application to display the main window.
Classes and Methods
MusicalLessonScheduler
This is the main class of the application, which extends JFrame. It handles the creation of the main window, user input, and schedule display.

Methods
generateSchedule(): Generates the schedule based on user input.
getCustomSchedule(): Prompts the user to enter a custom group order and returns it as a list of lists of strings.
initializeGroups(): Initializes the default groups (A through T) and returns them in a list of lists of strings.
displaySchedule(List<ScheduleEntry> schedule): Creates a new window to display the generated schedule in a table and allows the user to export the schedule to a CSV file.
exportToCSV(JTable table, File file): Exports the schedule data in the JTable to a CSV file.
DaysOffPanel
This is an inner class of the MusicalLessonScheduler class. It creates a panel that allows the user to enter days off and dynamically add new fields to enter more days off as needed.

Methods
addDayOffField(): Adds a new day off text field to the panel.
getDaysOff(): Returns a list of LocalDate objects representing the days off entered by the user.
Dependencies
Java 8 or later
Java Swing
