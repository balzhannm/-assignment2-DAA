package structures;

public class DynamicArray {
    private int[] data;
    private int size;
    private final OperationCounter counter;

    public DynamicArray() {
        this(10, new OperationCounter());
    }

    public DynamicArray(int initialCapacity, OperationCounter counter) {
        if (initialCapacity < 1) initialCapacity = 1;
        this.data = new int[initialCapacity];
        this.counter = counter;
    }

    public int size() {
        return size;
    }

    public void add(int x) {
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
                counter.steps++;
                counter.moves++;
            }
            data = newData;
        }
        data[size++] = x;
        counter.steps++;
        counter.moves++;
    }

    public void add(int index, int x) {
        checkPosition(index);
        if (size == data.length) {
            int[] newData = new int[data.length * 2];
            for (int i = 0; i < size; i++) {
                newData[i] = data[i];
                counter.steps++;
                counter.moves++;
            }
            data = newData;
        }
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
            counter.steps++;
            counter.moves++;
        }
        data[index] = x;
        size++;
        counter.steps++;
        counter.moves++;
    }

    public int remove(int index) {
        checkIndex(index);
        int removed = data[index];
        counter.steps++;
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
            counter.steps++;
            counter.moves++;
        }
        size--;
        return removed;
    }

    public int get(int index) {
        checkIndex(index);
        counter.steps++;
        return data[index];
    }

    public boolean contains(int x) {
        for (int i = 0; i < size; i++) {
            counter.steps++;
            counter.comparisons++;
            if (data[i] == x) return true;
        }
        return false;
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
