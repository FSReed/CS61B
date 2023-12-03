package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    public Comparator<T> comparator;
    public MaxArrayDeque(Comparator<T> c) {
        this.comparator = c;
    }

    public T max(Comparator<T> c) {
        if (this.size() == 0) {
            return null;
        } else {
            T max = this.get(0);
            for (int i = 0; i < this.size(); i += 1) {
                if (c.compare(this.get(i), max) > 0) {
                    max = this.get(i);
                }
            }
            return max;
        }
    }

    public T max() {
        return this.max(this.comparator);
    }
}
