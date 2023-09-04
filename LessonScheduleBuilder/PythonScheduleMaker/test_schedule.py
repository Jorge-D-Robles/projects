from schedule import rotate_group, randomize_group, find_group

all_groups = [['A', 'B', 'C', 'D'],
              ['E', 'F', 'G', 'H'],
              ['I', 'J', 'K', 'L'],
              ['M', 'N', 'O', 'P'],
              ['Q', 'R', 'S', 'T']]

def test_rotate_group():
    assert rotate_group(all_groups[0]) == ['D', 'A', 'B', 'C']

def test_randomize_group():
    assert randomize_group(all_groups[0]) != ['A', 'B', 'C', 'D']

def test_find_group():
    assert find_group(all_groups) == all_groups[0][0]