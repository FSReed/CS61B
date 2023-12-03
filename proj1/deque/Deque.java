package deque;

public interface Deque<Item> {
    public void addFirst(Item element);
    public void addLast(Item element);
    public default boolean isEmpty() {
        return size() == 0;
    }
    public int size();
    public void printDeque();
    public Item removeFirst();
    public Item removeLast();
    public Item get(int index);
}
