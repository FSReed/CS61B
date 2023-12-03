package deque;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.*;

public class MaxArrayDequeTest {
    @Test
    public void testMax() {
        Comparator<Integer> integerComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;
            }
        };

        MaxArrayDeque<Integer> m = new MaxArrayDeque<>(integerComparator);
        m.addFirst(3);
        m.addFirst(5);
        m.addLast(23);
        m.addFirst(12);
        int result = m.max();
        assertEquals(23, result);
    }
}
