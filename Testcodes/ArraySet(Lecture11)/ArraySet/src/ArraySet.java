import java.util.Iterator;

public class ArraySet<T> implements Iterable<T> {

    private T[] items;
    private int size;

    public ArraySet() {
        items = (T[]) new Object[100];
        size = 0;
    }

    public boolean contains(T x) {
        for (int i = 0; i < size; i += 1) {
            if (items[i].equals(x)) {
                return true;
            }
        }
        return false;
    }

    public int size() {
        return this.size;
    }
    @Override
    public Iterator<T> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<T> {
        private int wizPos;

        public ArrayIterator() {
            wizPos = 0;
        }
        @Override
        public boolean hasNext() {
            return items[wizPos] != null;
        }

        @Override
        public T next() {
            T result = items[wizPos];
            wizPos += 1;
            return result;
        }
    }
    public void add(T x) {
        if (x == null) {
            throw new IllegalArgumentException("Can't add null to the set"); //Add an exception.
        }
        if (contains(x)) {
            return;
        }
        items[size] = x;
        size += 1;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("{");
        for (T element: this) {
            result.append(element);
            result.append(",");
        }
        result.append("}");
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;} // Don't iterate itself.

        if (o instanceof ArraySet otherSet) {
            if (otherSet.size() != this.size()) {
                return false;
            } else {
                for (T element: this) {
                    if (!otherSet.contains(element)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        ArraySet<Integer> s = new ArraySet<>();
        s.add(23);
        s.add(4);
        s.add(15);
        s.add(42);
        System.out.println(s);

        ArraySet<Integer> s1 = new ArraySet<>();
        s1.add(23);
        s1.add(4);
        s1.add(15);
        s1.add(41);
        System.out.println(s.equals(s1));
    }
}
