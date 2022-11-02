def main():
    all_groups = [['A', 'B', 'C', 'D']
                  ['E', 'F', 'G', 'H']
                  ['I', 'J', 'K', 'L']
                  ['M', 'N', 'O', 'P']
                  ['Q', 'R', 'S', 'T']]

    # something % 5, 1 is group a, 2 group b, etc up to 5 = groupe
    # after the group function has been called 4 times, switch to the next subgroup
    # after 5 subgroups have been called, begin again, but randomize subgroup order

    print(group_a)
    for i in range(4):
        rotate_group(group_a)
        print(group_a)
    print(group_a)


# push the last item in the list to the front for group_a

def rotate_group(group):
    group.insert(0, group.pop())
    return group




if __name__ == '__main__':
    main()
