package randomizedtest;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */
public class TestBuggyAList {
  // YOUR TESTS HERE
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> array1 = new AListNoResizing<>();
        BuggyAList<Integer> array2 = new BuggyAList<>();
        for (int i = 0; i < 3; i += 1) {
            array1.addLast(i);
            array2.addLast(i);
        }
        for (int j = 0; j < 3; j += 1) {
            int result1 = array1.removeLast();
            int result2 = array2.removeLast();
            assertEquals(result1, result2);
        }
    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> test = new BuggyAList<>();

        int N = 5000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                test.addLast(randVal);
                // System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // getLast
                if (L.size() == 0 || test.size() == 0) {
                    continue;
                }
                int result1 = L.getLast();
                int result2 = test.getLast();
                // System.out.println("L.getLast() = " + result1 + ". test.getLast() = " + result2);
                assertEquals(result1, result2);
            } else {
                if (L.size() == 0 || test.size() == 0) {
                    continue;
                }
                int result1 = L.removeLast();
                int result2 = test.removeLast();
                int length1 = L.size();
                int length2 = test.size();
                // System.out.println("L.removeLast() = " + result1 + ". L.size() = " + length1);
                // System.out.println("test.removeLast() = " + result2 + ". test.size() = " + length2);
                assertEquals(result1, result2);
                assertEquals(length1, length2);
            }
        }

    }
}
