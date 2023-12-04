package deque;

public interface Deque<T> extends Iterable<T> {
    void addFirst(T element);
    void addLast(T element);
    default boolean isEmpty() {
        return size() == 0;
    }
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);
}
