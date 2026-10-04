package structures;

public class MyLinkedList {
    private static class Node {
        int value;
        Node next;
        Node prev;
        Node(int value) {
            this.value = value;
        }
    }

    private Node head;
    private int size;
    private final OperationCounter counter;

    public MyLinkedList() {
        this(new OperationCounter());
    }

    public MyLinkedList(OperationCounter counter) {
        this.counter = counter;
    }

    public int size() {
        return size;
    }

    public void add(int x) {
        Node node = new Node(x);
        counter.moves++;
        if (head == null) { head = node; size++; return; }
        Node cur = head;
        while (cur.next != null) { cur = cur.next; counter.steps++; }
        cur.next = node; node.prev = cur; counter.moves += 2; size++;
    }

    public void add(int index, int x) {
        checkPosition(index);
        Node node = new Node(x);
        counter.moves++;
        if (index == 0) {
            node.next = head;
            if (head != null) head.prev = node;
            head = node;
            counter.moves += 2;
            size++;
            return;
        }
        Node prev = nodeAt(index - 1);
        node.next = prev.next;
        node.prev = prev;
        if (prev.next != null) prev.next.prev = node;
        prev.next = node;
        counter.moves += 4;
        size++;
    }

    public int remove(int index) {
        checkIndex(index);
        if (index == 0) {
            int value = head.value;
            head = head.next;
            if (head != null) head.prev = null;
            counter.steps++;
            counter.moves += 2;
            size--;
            return value;
        }
        Node prev = nodeAt(index - 1);
        Node removed = prev.next;
        prev.next = removed.next;
        if (removed.next != null) removed.next.prev = prev;
        counter.steps++;
        counter.moves += 2;
        size--;
        return removed.value;
    }

    public int get(int index) {
        checkIndex(index);
        return nodeAt(index).value;
    }

    public boolean contains(int x) {
        Node cur = head;
        while (cur != null) {
            counter.steps++;
            counter.comparisons++;
            if (cur.value == x) return true;
            cur = cur.next;
        }
        return false;
    }

    private Node nodeAt(int index) {
        if (index < size / 2) {
            Node cur = head;
            for (int i = 0; i < index; i++) { cur = cur.next; counter.steps++; }
            return cur;
        }
        Node cur = head;
        if (size > 0) { while (cur.next != null) cur = cur.next; }
        for (int i = size - 1; i > index; i--) { cur = cur.prev; counter.steps++; }
        return cur;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index: " + index);
    }

    private void checkPosition(int index) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("index: " + index);
    }
}
