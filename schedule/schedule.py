from datetime import date, timedelta
import datetime as dt
import calendar
import csv
import sys


def main():
    #adding make schedule at the top to skip the input parts. added redundant versions of input in the function just to test faster.
    make_schedule()
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


def make_schedule():
    # current_date = get_date() #This would go first at the top of main, testing purposes
    # end_date = current_date + timedelta(days=30) #sets end date 30 days after user inputted date
    # print(end_date) #testing only
    # day_cycle = 1 #testing only
    # grps = 'ABCDEFGHIJKLMAOPQRST'
    # day1_periods = [2, 6, 8, 9]
    # day2_periods = [2, 3, 8, 9]
    # schedule = {
    #     # date: {'period': period, 'group': group}
    
    # }
    # while end_date != current_date:
    #     date = current_date
    #     print(date)
    #     date = {}
    #     if day_cycle == 1:
    #         for period in day1_periods:
    #             for group in grps:
    #                 print(period)
    #                 date["period"] = day1_periods[period]
    #                 date["group"] = group
    #                 break
    #             break
    #         print(date)
    #     elif day_cycle == 2:
    #         for periods in day2_periods:
    #             ...
    #     schedule[date] = date
    #     current_date = current_date + timedelta(days=1)

    # print(schedule)
    # return schedule
    ...

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


if __name__ == "__main__":
    main()
