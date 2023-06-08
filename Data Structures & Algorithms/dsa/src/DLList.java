import java.util.ArrayList;
import java.util.NoSuchElementException;

public class DLList<E> {
    /* Nest Node class into DLL, to create a full doubly linked list */
    private class Node {
        public Node prev;
        public E item;
        public Node next;

        public Node(Node p, E i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    //the first item (if it exists) is at sentinel.next
    private final Node sentinel;
    //set the counter of size for linked list
    private int size;

    // Creates an empty DLList - instantiate a list w/ no ints if needed - constructor
    public DLList() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel; //add next and prev pointers back to the sentinel
        sentinel.prev = sentinel;
        size = 0;
    }

    //Constructor for SLL list, adding a sentinel node and adding a first element
    public DLList(E x) {
        sentinel = new Node(null, null, null);
        sentinel.next = new Node(sentinel, x, null);
        sentinel.prev = sentinel.next;
        size = 1;
    }

    public static void main(String[] args) {
        DLList<Integer> list = new DLList<>(); //creates new DLL, no nodes
        for (int i = 1; i < 26; i++) {
            list.addLast(i);
        }
        list.printList();
        list.insert(420, 21);
        list.linearSearch(420);
        list.remove(5);
        list.printList();
    }

    /* Linked List methods added to the class */

    //adds x to the front of the list
    public void addFirst(E x) {
        Node newNode = new Node(sentinel, x, sentinel.next);
        sentinel.next.prev = newNode; //sets the pointer from first node's prev -> new node
        sentinel.next = newNode; //sets the first node (after sentinel) to new node
        if (size == 0) //if there are no elements in the list
            sentinel.prev = newNode; // set the last node to be first node as well
        size++;
    }

    /*Adds an item to the end of the list */
    public void addLast(E x) {
        Node newNode = new Node(sentinel.prev, x, sentinel);
        sentinel.prev.next = newNode;
        sentinel.prev = newNode;
        if (size == 0)
            sentinel.next = newNode;
        size++;
    }
    /* Inserts an item to any point in the linked list */
    public void insert(E x, int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index out of bounds");

        Node currentNode = sentinel.next;
        int location = 0;
        while (currentNode != null && currentNode != sentinel) {
            if (location == index) {
                Node newNode = new Node(currentNode.prev, x, currentNode);
                currentNode.prev.next = newNode;
                currentNode.prev = newNode;
                size++;
                return;
            }
            currentNode = currentNode.next;
            location++;
        }
    }
    /* Removes any item from the list */
    public void remove(E x) {
        Node currentNode = sentinel.next;
        while (currentNode != null && currentNode != sentinel) {
            if (x.equals(currentNode.item)) {
                currentNode.prev.next = currentNode.next;
                currentNode.next.prev = currentNode.prev;
                size--;
                return;
            }
            currentNode = currentNode.next;
        }
    }

    //remove and return first item of DLL
    public E popFirst() {
        if (size == 0) //if list is empty
            throw new NoSuchElementException("List is empty.");

        E oldFirst = sentinel.next.item; //old first item -> first node after sentinel
        sentinel.next = sentinel.next.next; //sets the 2nd item to be 1st item
        sentinel.next.prev = sentinel; // sets prev of new first item to be sentinel

        // If the list becomes empty after removing the node, sentinel.next and sentinel.prev should point back to sentinel itself.
        if (size == 1)
            sentinel.prev = sentinel;
        size--;
        return oldFirst;
    }

    ///remove and return last item of DLL
    public E popLast() {
        if (size == 0)
            throw new NoSuchElementException("List is empty.");
        E oldLast = sentinel.prev.item; //old last item -> sentinel prev since circular
        sentinel.prev = sentinel.prev.prev; //sets 2nd to last item to be last item
        sentinel.prev.next = sentinel; //sets the circular motion back to sentinel
        if (size == 1)
            sentinel.next = sentinel;
        size--;
        return oldLast;
    }
    //returns first item in list
    public E getFirst() {
        return sentinel.next.item;
    }

    //returns the size of the list
    public E getLast() {
        return sentinel.prev.item;
    }
    public int size() {
        return size;
    }

    //iterates through the list, printing out the entire list
    public String printList() {
        Node currentNode = sentinel.next;
        StringBuilder output = new StringBuilder();

        while (currentNode != null && currentNode != sentinel) {
            output.append(currentNode.item);
            output.append(" ");
            currentNode = currentNode.next;
        }
        System.out.println(output.toString().trim());
        return output.toString().trim(); // trim to remove trailing space
    }
    public int linearSearch(E input) {
        Node currentNode = sentinel.next;
        int location = 0;
        while (currentNode != null && currentNode != sentinel) {
            if (currentNode.item.equals(input)) {
                System.out.println("Input " + input + " found at index " + location);
                return location;
            }

            currentNode = currentNode.next;
            location++;
        }
        System.out.println("Input not found in list.");
        return -1;
    }

    public DLList<E> sort(DLList<E> list) {
    return list;
    }
}
