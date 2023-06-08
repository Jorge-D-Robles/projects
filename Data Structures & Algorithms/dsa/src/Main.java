public class Main {
    public static void main(String[] args) {
        testDLList();
    }
    public static void testDLList() {
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
}
