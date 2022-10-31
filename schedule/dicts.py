import csv
from datetime import date, timedelta
from tabulate import tabulate


#create a list of dictionaries rather than a dict of dicts.
schedule = []
all_periods = ['Period 2', 'Period 3/6', 'Period 8', 'Period 9']
day1_periods = ['Period 2', 'Period 6', 'Period 8', 'Period 9']
day2_periods = ['Period 2', 'Period 3', 'Period 8', 'Period 9']

days_off = [date(2022, 11, 16), date(2022, 12, 1)]

groups = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
          'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T']
# for group in range(len(groups)):
#     groups[group] = []
#     print(group)

# print(group)    
    
current_date = date(2022, 11, 16)
end_date = current_date + timedelta(days=30)

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
        temp[f'Group {i+1}'] = groups[i] #create a get_group function that returns a new group over the list

             
    schedule.append(temp)
    current_date += timedelta(days=1)
    day_cycle += 1


print(tabulate(schedule, headers="keys",tablefmt="grid"))

with open('test.csv', 'w') as file:
    writer = csv.writer(file)
    writer.writerow(['Date', 'Period 2', 'Group 1', 'Period 3/6', 'Group 2', 'Period 8', 'Group 3', 'Period 9', 'Group 4'])
    for entry in schedule:
        writer.writerow(entry.values())
print('Done.')

    