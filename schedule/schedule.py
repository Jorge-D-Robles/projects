from datetime import date
import datetime as dt
import csv
import sys


def main():
    days_off = []

    print("Welcome to the musical lesson schedule builder. To start, please enter a starting date.")
    start_date = get_date()
    print(f"Your start date is {start_date}")
    agree()
    print("Next, you may add any potential days off within the next sixty days.")

    days_off = sorted(days_off)
    print(f"Your days off are: ")
    agree()
    for i in range(len(days_off)):
        print(days_off[i])


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
    check = input("Are these days off correct? y/n: ").strip().lower()
    if "y" not in check:
        sys.exit("Quitting. Please re-run the program to try again.")


def off(days_off):
    days_prompt = "y"
    while days_prompt.lower().strip() == "y":

        days_prompt = input("Would you like to add a day off? y / n: ").strip().lower()
        if days_prompt.startswith("n"):
            break
        elif days_prompt.startswith("y"):
            days_off.append(get_date())
            # Not working, fix after food.
        else:
            continue
    return days_off


if __name__ == "__main__":
    main()
