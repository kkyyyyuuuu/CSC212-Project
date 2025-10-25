public class CustomDynamicArray<T> {

    private static final int INITIAL_CAPACITY = 10;
    private Object[] array; 
    private int size;
    private int capacity;

    public CustomDynamicArray() {
        this.capacity = INITIAL_CAPACITY;
        this.array = new Object[this.capacity];
        this.size = 0;
    }

    public void add(T element) {
        if (size == capacity) {
            ensureCapacity();
        }
        array[size] = element;
        size++;
    }

    private void ensureCapacity() {
        this.capacity *= 2; 
        
        Object[] newArray = new Object[this.capacity];
        
        for (int i = 0; i < size; i++) {
            newArray[i] = this.array[i];
        }
        this.array = newArray;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        return (T) array[index]; 
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        
        T removedElement = (T) array[index];
        
        for (int i = index; i < size - 1; i++) {
            array[i] = array[i + 1];
        }
        
        array[size - 1] = null;
        size--;
        
        return removedElement;
    }

    public int getSize() {
        return size;
    }
    
    public void set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        array[index] = element;
    }

    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (array[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }
}
