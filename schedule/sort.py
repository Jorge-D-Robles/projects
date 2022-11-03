import random
from tabulate import tabulate
group_randomizer = 0 #this is for randomizing the subgroup order each loop of 20 groups

group_change = 0 #tracks individual swaps

group_index = 0 #tracks each subgroup of 5 groups of 4

total_changes = 0 #tracks total group swaps

all_groups = [['A', 'B', 'C', 'D'],
              ['E', 'F', 'G', 'H'],
              ['I', 'J', 'K', 'L'],
              ['M', 'N', 'O', 'P'],
              ['Q', 'R', 'S', 'T']]


def main():
    #this entire block of code is for testing purposes to see what the output is
    print(all_groups[group_index])
    for i in range(20):
        print(find_group(all_groups), end='')
    print("")
    print(tabulate(all_groups))
    for i in range(16):
        for i in range(20):
            print(find_group(all_groups), end='') #printing the entire 20 group loop in one line
        print(all_groups[group_index]) #to see what the first line is 
        print(tabulate(all_groups)) #for testing purposes, to read table
        print("")



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
    if group_randomizer == 20:
        rotate_group(all_groups) #rotates the 5 subgroups of 4 after each letter has gone through
        rotate_group(all_groups)
        group_randomizer = 0

    if total_changes < 21: #to keep groups un-randomized the first loop of 20 groups. base case
        rotated = all_groups[group_index][group_change]

    if group_change == 0 and total_changes >= 21: #begin rotating the letters in the subgroups. ABCD -> DABC -> CDAB -> BCDA -> ABCD
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

#FIX SORT.PY - THE LAST LETTER IS NOT BEING ROTATED CORRECTLY OUT OF 20!!