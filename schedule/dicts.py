import csv
from datetime import date, timedelta
from tabulate import tabulate
import random

#GROUP FUNCTION BELOW
###########################################################################################
group_randomizer = 0  # this is for randomizing the subgroup order each loop of 20 groups

group_change = 0  # tracks individual swaps

group_index = 0  # tracks each subgroup of 5 groups of 4

total_changes = 0  # tracks total group swaps

all_groups = [['A', 'B', 'C', 'D'],
              ['E', 'F', 'G', 'H'],
              ['I', 'J', 'K', 'L'],
              ['M', 'N', 'O', 'P'],
              ['Q', 'R', 'S', 'T']]

###########################################################################################
#DICTS BELOW

schedule = []
all_periods = ['Period 2', 'Period 3/6', 'Period 8', 'Period 9']
day1_periods = ['Period 2', 'Period 6', 'Period 8', 'Period 9']
day2_periods = ['Period 2', 'Period 3', 'Period 8', 'Period 9']

days_off = [date(2022, 11, 16), date(2022, 12, 1)]
def main():

    current_date = date(2022, 11, 16)
    end_date = current_date + timedelta(days=90)

    day_cycle = 1

    while end_date != current_date:

        if current_date in days_off or date.weekday(current_date) > 4:
            current_date += timedelta(days=1)
            continue
        temp = {}
        temp['Date'] = current_date
        for i in range(len(day1_periods)):
            if day_cycle % 2 == 1:
                temp[f'{all_periods[i]}'] = day1_periods[i]
            else:
                temp[f'{all_periods[i]}'] = day2_periods[i]
            # create a get_group function that returns a new group over the list
            temp[f'Group {i+1}'] = find_group(all_groups)

        schedule.append(temp)
        current_date += timedelta(days=1)
        day_cycle += 1


    print(tabulate(schedule, headers="keys", tablefmt="grid"))

    with open('test.csv', 'w') as file:
        writer = csv.writer(file)
        writer.writerow(['Date', 'Period 2', 'Group 1', 'Period 3/6',
                        'Group 2', 'Period 8', 'Group 3', 'Period 9', 'Group 4'])
        for entry in schedule:
            writer.writerow(entry.values())
    print('Done.')

# rotate functions begin here
#########################################################################################


def rotate_group(group):
    group.insert(0, group.pop())
    return group


def randomize_group(group):
    random.shuffle(group)
    return group


def find_group(all_groups):
    global group_randomizer
    global group_change
    global group_index
    global total_changes

    group_randomizer += 1
    if group_randomizer == 21:
        randomize_group(all_groups)
        group_randomizer = 0

    if total_changes < 19:  # to keep groups un-randomized the first loop of 20 groups. base case
        rotated = all_groups[group_index][group_change]

    else:  # begin rotating the letters in the subgroups. ABCD -> DABC -> CDAB -> BCDA -> ABCD
        all_groups = rotate_group(all_groups[group_index])
        rotated = all_groups

    total_changes += 1  # increment total changes
    group_change += 1  # increment a rotation counter.

    if group_change == 4:  # if the rotation counter reaches 4, reset it and increment the subgroup index
        group_index += 1
        group_change = 0
        if group_index == 5:  # if the subgroup index reaches 5, reset it to return back to the first subgroup
            group_index = 0

    return rotated[0]  # return the current rotated letter

if __name__ == '__main__':
    main()