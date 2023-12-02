package flik;
import org.junit.*;
import static org.junit.Assert.*;

public class TestFlik {

    @Test
    public void isFlikAlright() {
        int[] test1 = {1, 2, 3, 4, 6, 45, 5, 7, 3, 128};
        int[] test2 = {1, 2, 3, 4, 6, 45, 5, 7, 3, 128};

        for (int i = 0; i < test1.length; i++) {
            boolean result1 = Flik.isSameNumber(test1[i], test2[i]);
            assertTrue(result1);
        }
    }

}
