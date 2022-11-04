from datetime import date, timedelta
import csv
import sys
from tabulate import tabulate
import random


def main():
    global custom
    # adding make schedule at the top to skip the input parts. added redundant versions of input in the function just to test faster.
    print("Welcome to the musical lesson schedule builder. To start, please enter a starting date.")
    while True:
        start_date = get_date()
        print(f"Your start date is {start_date}.")
        if agree():
            break
        else:
            continue
    while True:
        try:
            day_cycle = int(
                input(f"Is {start_date} a day 1 or day 2? Type 1 or 2 only: ").strip())

            if day_cycle == 1 or day_cycle == 2:
                print(f"Your start date is {start_date} and it is a day {day_cycle}.")
                if agree():
                    break
            else:
                print("Invalid input. Try again.")
                continue
        except ValueError:
            print("Invalid input. Try again.")
            continue
               
    while True:
        try:    
            print("Next, you may add any potential days off within the next sixty days.")
            days_off = off()
            print(f"Your days off are: ")

            for i in range(len(days_off)):
                print(days_off[i])
            if agree():
                break
            else:
                print("Please re-enter your days off.")
        except ValueError:
            print("Invalid input. Try again.")
            continue
    while True:
        try:
            weeks = int(input(
                "How many weeks would you like to schedule? Please enter a number between 1 - 52: ").strip())
            if weeks > 0 and weeks < 52:
                print(f"You have chosen to schedule {weeks} weeks.")
                break
            else:
                print("invalid input. Please try again.")
        except ValueError:
            print("Invalid input. Try again.")
            continue
    
    print("Finally, you may choose to start with a custom group order or the default group order.")
    order = input(
        "Would you like to build a custom schedule? y/n: ").strip().lower()
    if "y" in order:
        custom_start(all_groups)
        randomize_group(all_groups)
        custom = True
    else:
        regular_start(all_groups)
        custom = False

    print("")
    make_schedule(start_date, day_cycle, days_off, weeks)
    print("Your schedule is complete. Please check the schedule.csv file for your schedule.")
    sys.exit("Thank you for using the schedule builder. Goodbye.")


def get_date():
    while True:
        try: 
            year, month, day = input(
                "Enter date in YYYY MM DD format: ").split(" ")
            year = int(year)
            month = int(month)
            day = int(day)

            user_date = date(year, month, day)
            return user_date
        except ValueError:
            print("Wrong input. Please try again.")
            continue


def agree():
    while True:
        check = input("Are these days correct? y/n: ").strip().lower()
        print("")
        if "y" in check:
            return True
        elif "n" in check:
            return False
        else:
            print("Invalid input. Please try again.")
            continue
        
        


def off():
    try:
        days_off = []
        days_prompt = "y"
        while True:

            days_prompt = input(
                "Would you like to add a day off? y / n: ").strip().lower()
            if "n" in days_prompt:
                break
            elif "y" in days_prompt:
                days_off.append(get_date())
            else:
                print("Invalid input. Please try again.")
                continue
        days_off = sorted(days_off)
        return days_off
    except ValueError:
        print("Invalid input. Please try again.")


# GROUP FUNCTION BELOW
###########################################################################################
group_randomizer = 0  # this is for randomizing the subgroup order each loop of 20 groups

group_change = 0  # tracks individual swaps

group_index = 0  # tracks each subgroup of 5 groups of 4

total_changes = 0  # tracks total group swaps

all_groups = []

custom = True
###########################################################################################
# DICTS BELOW

schedule = []
all_periods = ['Period 2', 'Period 3/6', 'Period 8', 'Period 9']
day1_periods = ['Period 2', 'Period 6', 'Period 8', 'Period 9']
day2_periods = ['Period 2', 'Period 3', 'Period 8', 'Period 9']


def custom_start(all_groups):
    while True:
        temp_group = []
        print("You have chosen to start with a custom group order. Please enter the previous group order in terms of days 1-5 with four groups per day. Please separate each group with an empty space.")
        group1 = input("Day 1 out of 5: ").strip().split(" ")
        temp_group.append(group1)
        group2 = input("Day 2 out of 5: ").strip().split(" ")
        temp_group.append(group2)
        group3 = input("Day 3 out of 5: ").strip().split(" ")
        temp_group.append(group3)
        group4 = input("Day 4 out of 5: ").strip().split(" ")
        temp_group.append(group4)
        group5 = input("Day 5 out of 5: ").strip().split(" ")
        temp_group.append(group5)
        print("Your custom group order is: ")
        print(tabulate(temp_group))
        check = input("Are these groups correct? y/n: ").strip().lower()
        if "y" not in check:
            temp_group = []
            continue
        else:
            # check if group1 index[3] is valid
            try:
                for i in range(4):
                    temp = temp_group[i][3]
                    
                
                
            except IndexError:
                print("Invalid group order. Please try again.")
                temp_group = []
                continue
            else:
                all_groups.append(group1)
                all_groups.append(group2)
                all_groups.append(group3)
                all_groups.append(group4)
                all_groups.append(group5)
                return all_groups


def regular_start(all_groups):
    all_groups.append(['A', 'B', 'C', 'D'])
    all_groups.append(['E', 'F', 'G', 'H'])
    all_groups.append(['I', 'J', 'K', 'L'])
    all_groups.append(['M', 'N', 'O', 'P'])
    all_groups.append(['Q', 'R', 'S', 'T'])
    return all_groups


def make_schedule(current_date, day_cycle, days_off, weeks):
    end_date = current_date
    while weeks > 0:
        end_date = end_date + timedelta(days=7)
        weeks -= 1  # subtracts a week from the total number of weeks

    print(f"Your end date is: {end_date}.")

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

    with open('schedule.csv', 'w') as file:
        writer = csv.writer(file)
        writer.writerow(['Date', 'Period 2', 'Group 1', 'Period 3/6',
                        'Group 2', 'Period 8', 'Group 3', 'Period 9', 'Group 4'])
        for entry in schedule:
            writer.writerow(entry.values())

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
    global custom

    group_randomizer += 1
    if group_randomizer == 21:
        # rotates the 5 subgroups of 4 after each letter has gone through
        all_groups = randomize_group(all_groups)
        group_randomizer = 1

    # to keep groups un-randomized the first loop of 20 groups. base case
    if total_changes < 20 and custom == False:
        rotated = all_groups[group_index][group_change]

    if custom == True and group_change == 0:
        all_groups[group_index] = rotate_group(all_groups[group_index])
        rotated = all_groups[group_index]

    # begin rotating the letters in the subgroups. ABCD -> DABC -> CDAB -> BCDA -> ABCD
    elif group_change == 0 and total_changes >= 20 and custom == False:
        # all_groups = rotate_group(all_groups[group_index])
        all_groups[group_index] = rotate_group(all_groups[group_index])
        rotated = all_groups[group_index]

    elif group_change > 0 and group_change < 5:
        rotated = all_groups[group_index][group_change]

    total_changes += 1
    group_change += 1  # increment a rotation counter.

    if group_change == 4:  # if the rotation counter reaches 4, reset it and increment the subgroup index
        group_index += 1
        group_change = 0
        if group_index == 5:  # if the subgroup index reaches 5, reset it to return back to the first subgroup
            group_index = 0

    # return rotated[0]  #return the current rotated letter
    return rotated[0] if type(rotated) == list else rotated


if __name__ == '__main__':
    main()
