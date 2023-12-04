package tester;

import static org.junit.Assert.*;

import edu.princeton.cs.introcs.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void randomTest() {
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        for (int i = 0; i < 10000; i += 1) {
            int operation = StdRandom.uniform(4);
            switch (operation) {
                case 0:
                    ads.addFirst(i);
                    sad.addFirst(i);
                    System.out.println("addFirst(" + i + ")");
                    break;
                case 1:
                    System.out.println("addLast(" + i + ")");
                    ads.addLast(i);
                    sad.addLast(i);
                    break;
                case 2:
                    if (!ads.isEmpty() && !sad.isEmpty()) {
                        System.out.println("removeFirst()");
                        Integer result1 = ads.removeFirst();
                        Integer result2 = sad.removeFirst();
                        assertEquals("Remove first should be equal.", result1, result2);
                    }
                    break;
                case 3:
                    if (!ads.isEmpty() && !sad.isEmpty()) {
                        System.out.println("removeLast()");
                        Integer r1 = ads.removeLast();
                        Integer r2 = sad.removeLast();
                        assertEquals("Remove last should be equal.", r1, r2);
                    }
                    break;
                default:
            }
        }

    }
}
