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
        temp[keys[i]] = groups[i]
    my_dict.append(temp)
    day += 1
    
    
print(my_dict)
print(tabulate(my_dict, headers='keys'))