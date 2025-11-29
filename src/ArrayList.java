package a;

public class ArrayList<T> implements List<T> {
    private int maxsize;
    private int size;
    private int current;
    private T[] nodes;

    public ArrayList(int n) {
        if (n <= 0) n = 1;
        maxsize = n;
        size = 0;
        current = -1;
        nodes = (T[]) new Object[n];
    }

    public boolean full()  { return size == maxsize; }
    public boolean empty() { return size == 0; }

    public boolean last() {
        return !empty() && current == size - 1;
    }

    public void findFirst() {
        if (!empty()) current = 0;
    }

    public void findNext() {
        if (!empty() && !last()) current++;
    }

    public T retrieve() {
        return !empty() ? nodes[current] : null;
    }

    public void update(T val) {
        if (!empty()) nodes[current] = val;
    }

    public void insert(T val) {
        if (full()) return;
        for (int i = size - 1; i > current; --i) {
            nodes[i + 1] = nodes[i];
        }
        current++;
        nodes[current] = val;
        size++;
    }

    public void remove() {
        if (empty()) return;
        for (int i = current + 1; i < size; i++) {
            nodes[i - 1] = nodes[i];
        }
        size--;
        if (size == 0) current = -1;
        else if (current == size) current = 0;
    }

     
    public int size() { return size; }
}