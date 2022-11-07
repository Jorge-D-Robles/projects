# Schedule.py

**Schedule.py** is a Python program that was created to solve the unique problem of Jonas E. Salk Middle School's music lesson plan scheduling. At Salk, music teachers must schedule "pull-out" lessons - music lessons where students are pulled out of their daily classes once a week in order to have a music lesson instead. The problem was presented as such -

1. There are 20 total groups, with four periods available for lesson periods a day. The school operates on a two-day cycle.

2. Students must not miss the same period more than once every four weeks.

3. If possible, students should shuffle through Monday - Friday rather than always going on Mondays, Tuesdays, etc.

4. Custom parameters should be set, such as being able to input days off, choose what the group names should be, etc.

Prior to schedule.py, the music teachers at Salk spent hours trying to build a schedule every few weeks by hand and was noted to be one of the most frustrating parts of the job.

**Schedule.py** solves this problem by taking the 20 groups and dividing them 5 bigger subgroups of 4 as noted below.

```
all_groups = [['A', 'B', 'C', 'D'],
              ['E', 'F', 'G', 'H'],
              ['I', 'J', 'K', 'L'],
              ['M', 'N', 'O', 'P'],
              ['Q', 'R', 'S', 'T']]
```

A couple global counters were implemented in order to count how many times the main group sorting function was called. The function first rotates the group using the following:
```
def rotate_group(group):
    group.insert(0, group.pop())
    return group
```

The index starts at `all_groups[0][0]`, goes up to `all_groups[0][3]`, Rotates the second subgroup and moves up an index `all_groups[1][0]`, etc up to `all_groups[4][3]`. Once the 
function has been called 20 times, 

```
def randomize_group(group):
    random.shuffle(group)
    return group
```

will be called to shuffle the days of the week each group will have lessons on.

On the other side of the program, the main schedule is written as a list of dictionaries/hash tables. The schedule runs while the start date is not the current date being written. Each dictionary corresponds to one day, containing four periods and four groups that have been called with the group sorting function. 

At the end of the loop, the date moves up by one using the datetime library, and the loop will only run in its entirety if the current date in the loop is not a weekend or matches one of the user-inputted days off. If not, it will add one day to the current date and move on.

Once the entire schedule has been made, it will print out the schedule in a nice format using the tabulate library as well as write the schedule to a .csv file. 

Thank you for reading. If you have any questions, feel free to contact me at jorge@jorge.dev.