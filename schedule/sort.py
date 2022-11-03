import random
from datetime import date, timedelta
from tabulate import tabulate
group_randomizer = 0

group_change = 0

group_index = 0

total_changes = 0

all_groups = [['A', 'B', 'C', 'D'],
              ['E', 'F', 'G', 'H'],
              ['I', 'J', 'K', 'L'],
              ['M', 'N', 'O', 'P'],
              ['Q', 'R', 'S', 'T']]


def main():

    print(all_groups[group_index])
    for i in range(20):
        print(find_group(all_groups), end='')
    print("")
    for i in range(8):
        for i in range(20):
            print(find_group(all_groups), end='')
        print(all_groups[group_index])
        print("")

    current_date = date(2022, 11, 16)

def rotate_group(group):
    group.insert(0, group.pop())
    return group


def randomize_group(group):
    random.shuffle(group)
    return group[0]


def find_group(all_groups):
    global group_randomizer
    global group_change
    global group_index
    global total_changes

    # group_randomizer += 1
    # if group_randomizer == 21:
    #     randomize_group(all_groups)
    #     group_randomizer = 0

    if total_changes < 19:
        rotated = all_groups[group_index][group_change]

    else:
        all_groups = rotate_group(all_groups[group_index])
        rotated = all_groups

    total_changes += 1
    group_change += 1

    if group_change == 4:
        group_index += 1
        group_change = 0
        if group_index == 5:
            group_index = 0

    return rotated[0]


if __name__ == '__main__':
    main()
