package deque;
import java.util.Iterator;

public class ArrayDeque<Item> implements Deque<Item>{
    private Item[] array;
    private int size;
    public int length;
    public int front;
    public int tail;
    public ArrayDeque() {
        array = (Item[]) new Object[8];
        length = 8;
        front = 0;
        tail = 0;
        size = 0;
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

    /* To check if the list is full or empty.*/
    public boolean isFull() {
        return size == length;
    }

    /* Resize the current list.*/
    private void resize(int newLength) {
        Item[] newArray = (Item[]) new Object[newLength];
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

    /* Adds an item of type T to the front of the deque.*/
    public void addFirst(Item element) {
        if (isFull()) {
            resize(length * 2);
        }
        array[front] = element;
        if (isEmpty()) {
            tail = nextPosition(tail);
        }
        front = prevPosition(front);
        size += 1;
    }

    /* Adds an item of type T to the back of the deque.*/
    public void addLast(Item element) {
        if (isFull()) {
            resize(length * 2);
        }
        array[tail] = element;
        if (isEmpty()) {
            front = prevPosition(front);
        }
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

    /* Removes and returns the item at the front of the deque. If no such item exists, returns null.*/
    public Item removeFirst() {
        if (!isEmpty()) {
            size -= 1;
        }
        front = nextPosition(front);
        Item result = array[front];
        array[front] = null;
        return result;
    }

    /* Removes and returns the item at the back of the deque. If no such item exists, returns null.*/
    public Item removeLast() {
        if (!isEmpty()) {
            size -= 1;
        }
        tail = prevPosition(tail);
        Item result = array[tail];
        array[tail] = null;
        return result;
    }

    /**  Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null.
     */
    public Item get(int index) {
        if (index >= size) {
            return null;
        }
        int target = Math.floorMod(front + index + 1, length);
        return array[target];
    }

    public Iterator<Item> iterator() {
        ArrayDeque<Item> currentDeque = this;
        return new Iterator<>() {
            private int position;
            @Override
            public boolean hasNext() {
                return position < currentDeque.size();
            }

            @Override
            public Item next() {
                Item result = currentDeque.get(position);
                position += 1;
                return result;
            }
        };
    }

    public boolean equals(Object o) {
        if (o instanceof ArrayDeque newArray) {
            if (newArray.size() == this.size()) {
                for (int i = 0; i < this.size(); i += 1) {
                    if (!this.get(i).equals(newArray.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}