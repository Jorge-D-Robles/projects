def main():
    group_a = ['A', 'B', 'C', 'D']
    group_b = ['E', 'F', 'G', 'H']
    group_c = ['I', 'J', 'K', 'L']
    group_d = ['M', 'N', 'O', 'P']
    group_e = ['Q', 'R', 'S', 'T']
    print(group_a)
    for i in range(4):
        rotate_group(group_a)
        print(group_a)
    print(group_a)


#push the last item in the list to the front for group_a

def rotate_group(group):
    group.insert(0, group.pop())
    return group


if __name__ == '__main__':
    main()