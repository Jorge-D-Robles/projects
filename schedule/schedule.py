from datetime import date, timedelta
import csv
import sys
from tabulate import tabulate
import random

def main():
    #adding make schedule at the top to skip the input parts. added redundant versions of input in the function just to test faster.
    print("Welcome to the musical lesson schedule builder. To start, please enter a starting date.")
    start_date = get_date()
    print(f"Your start date is {start_date}.")
    while True:
        day_cycle = int(input(f"Is {start_date} a day 1 or day 2? Type 1 or 2 only: ").strip())

        if day_cycle == 1 or day_cycle == 2:
            break
        else:
            print("Invalid input. Try again.")
    print(f"Your start date is {start_date} and it is a day {day_cycle}.")
    agree()
    print("Next, you may add any potential days off within the next sixty days.")
    days_off = off()
    print(f"Your days off are: ")

    for i in range(len(days_off)):
        print(days_off[i])
    agree()
    weeks = int(input("How many weeks would you like to schedule? Please enter a number: ").strip())
   
    make_schedule(start_date, day_cycle, days_off, weeks)
    print("Your schedule is complete. Please check the schedule.csv file for your schedule.")
    sys.exit("Thank you for using the schedule builder. Goodbye.")
def get_date():
    while True:
        year, month, day = input("Enter date in YYYY MM DD format: ").split(" ")
        year = int(year)
        month = int(month)
        day = int(day)
        try:
            user_date = date(year, month, day)
            return user_date
        except ValueError:
            print("Wrong input. Please try again.")
            continue


def agree():
    check = input("Are these days correct? y/n: ").strip().lower()
    if "y" not in check:
        sys.exit("Quitting. Please re-run the program to try again.")
    print("")


def off():
    days_off = []
    days_prompt = "y"
    while days_prompt.lower().strip() == "y":

        days_prompt = input("Would you like to add a day off? y / n: ").strip().lower()
        if days_prompt.startswith("n"):
            break
        elif days_prompt.startswith("y"):
            days_off.append(get_date())
        else:
            continue
    days_off = sorted(days_off)
    return days_off



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

def make_schedule(current_date, day_cycle, days_off, weeks):
    end_date = current_date
    while weeks > 0:
        end_date = end_date + timedelta(days=7)
        weeks -= 1 # subtracts a week from the total number of weeks
        
    print(end_date)

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
        randomize_group(all_groups) #rotates the 5 subgroups of 4 after each letter has gone through
        group_randomizer = 0

    if total_changes < 20: #to keep groups un-randomized the first loop of 20 groups. base case
        rotated = all_groups[group_index][group_change]

    if group_change == 0 and total_changes >= 20: #begin rotating the letters in the subgroups. ABCD -> DABC -> CDAB -> BCDA -> ABCD
        # all_groups = rotate_group(all_groups[group_index])
        all_groups[group_index] = rotate_group(all_groups[group_index])
        rotated = all_groups[group_index]
    
    elif group_change > 0 and group_change < 5:
        rotated = all_groups[group_index][group_change]
        
    total_changes += 1
    group_change += 1 #increment a rotation counter.

    if group_change == 4: #if the rotation counter reaches 4, reset it and increment the subgroup index
        group_index += 1
        group_change = 0
        if group_index == 5: #if the subgroup index reaches 5, reset it to return back to the first subgroup
            group_index = 0

    return rotated[0] if type(rotated) == list else rotated  #return the current rotated letter
    # return rotated[0] if type(rotated) == list else rotated

if __name__ == '__main__':
    main()