import csv
from tabulate import tabulate
#create a list of dictionaries rather than a dict of dicts.
my_dict = []
keys = ['Period 1', 'Period 3', 'Period 5', 'Period 7']
groups = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',
          'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't']
day = 1
while day < 31:
    temp = {}
    temp['day'] = day
    for i in range(len(keys)):
        temp[f'{keys[i]}'] = keys[i]
        temp[f'Group {i+1}'] = groups[i] #create a get_group function that returns a new group over the list
    my_dict.append(temp)
    day += 1
    
    
print(my_dict)
print(tabulate(my_dict, headers='keys', tablefmt="grid"))

with open('test.csv', 'w') as file:
    writer = csv.writer(file)
    writer.writerow(['Date', 'Period', 'Group', 'Period', 'Group', 'Period', 'Group', 'Period', 'Group'])
    for entry in my_dict:
        writer.writerow(entry.values())