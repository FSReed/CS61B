package deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
    private T[] array;
    private int size;
    private int length;
    private int front;
    private int tail;
    private double threshold;
    public ArrayDeque() {
        array = (T[]) new Object[8];
        length = 8;
        front = 0;
        tail = 1;
        size = 0;
        threshold = 0.25;
    }

    /** These are the helper methods.
     * Return the position that is one more before the current index.
     * e.g., prevPosition(1) = 0, prevPosition(0) = length - 1
     *       nextPosition(0) = 1, nextPosition(length - 1) = 0
     */
    private int prevPosition(int position) {
        if (position == 0) {
            return length - 1;
        } else {
            return position - 1;
        }
    }
    private int nextPosition(int position) {
        if (position == length - 1) {
            return 0;
        } else {
            return position + 1;
        }
    }

    private double uRate() {
        return (double) size / length;
    }
    /* To check if the list is full or empty.*/
    private boolean isFull() {
        return size == length;
    }

    /* Resize the current list.*/
    private void resize(int newLength) {
        T[] newArray = (T[]) new Object[newLength];
        int pointer = nextPosition(front);
        for (int i = 0; i < size; i++) {
            newArray[i] = array[pointer];
            pointer = nextPosition(pointer);
        }
        array = newArray;
        length = newLength;
        front = newLength - 1;
        tail = size;
    }

    private void checkUtilization() {
        if (uRate() < threshold && length > 8) {
            resize(length / 4);
        }
    }

    /* Adds an item of type T to the front of the deque.*/
    public void addFirst(T element) {
        if (isFull()) {
            resize(length * 2);
        }
        array[front] = element;
        front = prevPosition(front);
        size += 1;
    }

    /* Adds an item of type T to the back of the deque.*/
    public void addLast(T element) {
        if (isFull()) {
            resize(length * 2);
        }
        array[tail] = element;
        tail = nextPosition(tail);
        size += 1;
    }

    /* Returns true if deque is empty, false otherwise.*/

    /*  Returns the number of items in the deque.*/
    public int size() {
        return size;
    }

    /* Prints the items in the deque from first to last, separated by a space.*/
    public void printDeque() {
        int pointer = nextPosition(front);
        for (int i = 0; i < size; i++) {
            System.out.println(array[pointer]);
            pointer = nextPosition(pointer);
        }
    }

    /* Removes and returns the item at the front of the deque.
    If no such item exists, returns null.*/
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        front = nextPosition(front);
        T result = array[front];
        array[front] = null;
        size -= 1;
        checkUtilization();
        return result;
    }

    /* Removes and returns the item at the back of the deque.
    If no such item exists, returns null.*/
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        tail = prevPosition(tail);
        T result = array[tail];
        array[tail] = null;
        size -= 1;
        checkUtilization();
        return result;
    }

    /**  Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     */
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        int target = Math.floorMod(front + index + 1, length);
        return array[target];
    }

    public Iterator<T> iterator() {
        ArrayDeque<T> currentDeque = this;
        return new Iterator<>() {
            private int position;
            @Override
            public boolean hasNext() {
                return position < currentDeque.size();
            }

            @Override
            public T next() {
                T result = currentDeque.get(position);
                position += 1;
                return result;
            }
        };
    }

    public boolean equals(Object o) {
        if (o instanceof Deque) {
            if (((Deque) o).size() == this.size()) {
                for (int i = 0; i < this.size(); i += 1) {
                    if (!this.get(i).equals(((Deque) o).get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
