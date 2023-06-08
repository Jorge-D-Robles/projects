import java.util.ArrayList;

public class SLList<E> {
    /* Nest SingleNode class into SLL, to create a full singly linked list */
    private class SingleNode {
        public E item;
        public SingleNode next;

        public SingleNode(E i, SingleNode n) {
            item = i;
            next = n;
        }
    }
    //the first item (if it exists) is at sentinel.next
    private final SingleNode sentinel;
    //set the counter of size for linked list
    private int size;
    // Creates an empty SLList - instantiate a list w/ no ints if needed - constructor
    public SLList() {
        sentinel = new SingleNode(null, null);
        size = 0;
    }
    //Constructor for SLL list, adding a sentinel node and adding a first element
    public SLList(E x) {
        sentinel = new SingleNode(null, null);
        sentinel.next = new SingleNode(x, null);
        size = 1;
    }
    public static void main(String[] args) {
        SLList<String> L = new SLList<>("10"); //creates new SLL with value 10 as first
        System.out.println(L.printList());
        L.addFirst("lll"); //adds 10 to the first node
        System.out.println(L.printList());
        L.addFirst("abc"); //adds node of 5 to first node, push everything back
        System.out.println(L.printList());
        L.addLast("20"); //appends 20 to end of the list
        System.out.println(L.printList());
        System.out.println(L.getFirst()); // output 5
        System.out.println(L.size()); // output 4

    }

    /* Linked List methods added to the class */

    //adds x to the front of the list
    public void addFirst(E x) {
        sentinel.next = new SingleNode(x, sentinel.next);
        size += 1;
    }
    //returns first item in list
    public E getFirst() {
        return sentinel.next.item;
    }
    /*Adds an item to the end of the list */
    public void addLast(E x) {
        size += 1;
        SingleNode p = sentinel;
        //Move p until it reaches the end of the list
        while (p.next != null) {
            p = p.next;
        }
        p.next = new SingleNode(x, null);
    }
    //returns the size of the list
    public int size() {
        return size;
    }

    //iterates through the list, printing out the entire list
    public ArrayList<E> printList() {
        SingleNode p = sentinel;
        ArrayList<E> fullList = new ArrayList<>();

        while (p.next != null) {
            p = p.next;
            fullList.add(p.item);
        }
        return fullList;
    }
}
