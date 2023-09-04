# ScheduleMaker Documentation

`ScheduleMaker` is a Java program designed to solve the unique scheduling problem faced by Jonas E. Salk Middle School's music lesson plan. The school operates on a two-day cycle with 20 total groups and four periods available for lesson periods each day. The students must not miss the same period more than once every four weeks, and if possible, they should shuffle through Monday - Friday rather than always going on the same day of the week. 

This Java program is a refactored version of the original Python program, `schedule.py`, and it incorporates Object-Oriented Programming (OOP) principles to enhance modularity, readability, and maintainability.  It also includes a complete graphical user interface (GUI) rather than the command-line interface of the original Python version. 

**To read the original Python `schedule.py`, please browse the folder in this repository titled "PythonScheduleMaker."**

This program is easily accessed by running the ***ScheduleMaker.jar*** as long as a Java Runtime Environment is installed on the computer running the file and outputs the completed schedule as both an easy to read **table** as well as the option to save the schedule as a ***CSV*** file to allow for sharing and modification.

## Class Structure

The program consists of three main classes: `MusicalLessonScheduler`, `ScheduleEntry`, and `ScheduleBuilder`.

### Main Class

The `MusicalLessonScheduler` class is the entry point of the program. It handles user inputs such as start date, day cycle, days off, weeks, and whether to use a custom schedule or not. It then creates an instance of `ScheduleBuilder` with these parameters and calls the `buildSchedule` method to generate the schedule.

### ScheduleEntry Class

`ScheduleEntry` is a data class that represents a single entry in the schedule. It encapsulates the date, periods, and groups for a single day. It provides getter methods for accessing these properties and a `setPeriod` method for setting the period and group for a given day cycle and period index. The `toString` method is overridden to provide a human-readable representation of the schedule entry.

### ScheduleBuilder Class

`ScheduleBuilder` is the core class that implements the scheduling logic. It maintains the start date, day cycle, days off, weeks, all groups, and the schedule as instance variables. The `buildSchedule` method generates the schedule based on these parameters. It also provides helper methods like `rotateList` and `shuffleGroups` for rotating and shuffling the groups, respectively.

## Working Principle

The `ScheduleBuilder` class uses the builder design pattern to construct the schedule. It starts with the given start date and iterates through each day until the end date, which is calculated based on the number of weeks provided. 

For each day, if it is not a day off and is a weekday, a new `ScheduleEntry` is created. The periods and groups for the day are set based on the current day cycle and the groups' rotation state. The `ScheduleEntry` is then added to the schedule.

The groups are rotated and shuffled periodically to ensure that students do not miss the same period more than once every four weeks and that they shuffle through the days of the week.

Once the schedule is built, it is returned as a list of `ScheduleEntry` objects. Each `ScheduleEntry` object represents the schedule for a single day.

## Conclusion

The `ScheduleMaker` Java program provides an efficient and flexible solution to the music lesson scheduling problem at Jonas E. Salk Middle School. By leveraging OOP principles, it offers a more structured and maintainable approach compared to the original Python version. 

For any questions or further information, feel free to contact at jorge@jorgerobles.com.