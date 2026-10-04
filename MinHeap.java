package structures;

public class MinHeap {
    private int[] heap;
    private int size;
    private final OperationCounter counter;

    public MinHeap() {
        this(10, new OperationCounter());
    }

    public MinHeap(int initialCapacity, OperationCounter counter) {
        if (initialCapacity < 1) initialCapacity = 1;
        heap = new int[initialCapacity];
        this.counter = counter;
    }

    public int size() {
        return size;
    }

    public void insert(int x) {
        ensureCapacity();
        int i = size++;
        heap[i] = x;
        counter.steps++;
        counter.moves++;
        while (i > 0) {
            int parent = (i - 1) / 2;
            counter.comparisons++;
            if (heap[parent] <= heap[i]) break;
            swap(i, parent);
            i = parent;
        }
    }

    public int peekMin() {
        if (size == 0) throw new IllegalStateException("empty heap");
        counter.steps++;
        return heap[0];
    }

    public int extractMin() {
        if (size == 0) throw new IllegalStateException("empty heap");
        int result = heap[0];
        counter.steps++;
        heap[0] = heap[--size];
        counter.moves++;
        if (size > 0) bubbleDown(0);
        return result;
    }

    private void bubbleDown(int i) {
        while (true) {
            int left = 2 * i + 1;
            int right = left + 1;
            int smallest = i;

            if (left < size) {
                counter.comparisons++;
                if (heap[left] < heap[smallest]) smallest = left;
            }
            if (right < size) {
                counter.comparisons++;
                if (heap[right] < heap[smallest]) smallest = right;
            }
            if (smallest == i) break;
            swap(i, smallest);
            i = smallest;
        }
    }

    private void swap(int a, int b) {
        int t = heap[a];
        heap[a] = heap[b];
        heap[b] = t;
        counter.moves += 3;
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int[] newHeap = new int[heap.length * 2];
            for (int i = 0; i < size; i++) {
                newHeap[i] = heap[i];
                counter.steps++;
                counter.moves++;
            }
            heap = newHeap;
        }
    }
}
