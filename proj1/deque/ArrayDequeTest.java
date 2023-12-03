package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class ArrayDequeTest {

    @Test
    public void printAndSizeTest() {
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        adq.addFirst(3);
        adq.addLast(5);
        adq.addFirst(6);
        adq.printDeque();
        assertEquals(3, adq.size());
    }

    @Test
    public void resizeTest() {
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        for (int i = 0; i < 8; i++) {
            adq.addFirst(i);
        }
        adq.addFirst(12);
        assertEquals(9, adq.size());
        adq.printDeque();
    }

    @Test
    public void removeTest() {
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        for (int i = 0; i < 8; i++) {
            adq.addFirst(i);
        }
        int result1 = adq.removeFirst();
        assertEquals(7, result1);
        assertEquals(7, adq.size());
        int result2 = adq.removeLast();
        assertEquals(0, result2);
        assertEquals(6, adq.size());

        ArrayDeque<String> adq2 = new ArrayDeque<String>();
        adq2.addFirst("Student");
        assertEquals(1, adq2.size());
        adq2.removeLast();
        assertEquals(0, adq2.size());
    }

    @Test
    public void getItemTest() {
        ArrayDeque<Integer> adq = new ArrayDeque<Integer>();
        for (int i = 0; i < 8; i++) {
            adq.addFirst(i);
        }
        int result1 = adq.get(3);
        assertEquals(4, result1);
        int result2 = adq.get(7);
        assertEquals(0, result2);
    }
}
