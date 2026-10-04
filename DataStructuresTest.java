import org.junit.jupiter.api.Test;
import structures.*;

import static org.junit.jupiter.api.Assertions.*;

public class DataStructuresTest {
    @Test
    void dynamicArrayWorks() {
        OperationCounter c = new OperationCounter();
        DynamicArray a = new DynamicArray(2, c);
        a.add(10); a.add(20); a.add(1, 15);
        assertEquals(15, a.get(1));
        assertTrue(a.contains(20));
        assertEquals(15, a.remove(1));
        assertEquals(2, a.size());
    }

    @Test
    void linkedListWorks() {
        OperationCounter c = new OperationCounter();
        MyLinkedList list = new MyLinkedList(c);
        list.add(10); list.add(20); list.add(1, 15);
        assertEquals(15, list.get(1));
        assertTrue(list.contains(20));
        assertEquals(15, list.remove(1));
        assertEquals(2, list.size());
    }

    @Test
    void heapReturnsSortedValues() {
        OperationCounter c = new OperationCounter();
        MinHeap heap = new MinHeap(2, c);
        int[] values = {7, 2, 9, 1, 5, 3};
        for (int x : values) heap.insert(x);
        int previous = Integer.MIN_VALUE;
        while (heap.size() > 0) {
            int current = heap.extractMin();
            assertTrue(previous <= current);
            previous = current;
        }
    }

    @Test
    void invalidIndexesThrow() {
        DynamicArray a = new DynamicArray();
        assertThrows(IndexOutOfBoundsException.class, () -> a.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> a.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> a.add(2, 5));

        MyLinkedList l = new MyLinkedList();
        assertThrows(IndexOutOfBoundsException.class, () -> l.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> l.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> l.add(2, 5));
    }

    @Test
    void emptyHeapThrows() {
        MinHeap h = new MinHeap();
        assertThrows(IllegalStateException.class, h::peekMin);
        assertThrows(IllegalStateException.class, h::extractMin);
    }
}
