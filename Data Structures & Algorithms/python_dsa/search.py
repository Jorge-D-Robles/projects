from random import randint
def generate_array(size, num=5):
    arr = set()
    print("Setting array up")
    for i in range(size):
        arr.add(randint(0, size))
    arr.add(num)
    print("turning array into list")
    arr = list(arr)
    print("sorting list")
    arr.sort()
    print("done")
    return arr

def linear_search(arr, num):
    if num in arr:
        return True
    else:
        return False
def binary_search(arr, num):
    low = 0
    high = len(arr) - 1
    print(f"Finding {num}")
    while low <= high:
        if num > arr[-1] or num < arr[0]:
            return False
        mid = (low + high) // 2
        print(arr[low], arr[mid], arr[high])
        if num == arr[mid]:
            return True
        elif num > arr[mid]:
            low = mid + 1
        else:
            high = mid - 1
    return False

def main():
    num = int(input("What number to find?: "))
    arr = [i for i in range(100)]
    print(linear_search(arr, num))
    print(binary_search(arr, num))

if __name__ == "__main__":
    main()