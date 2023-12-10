import org.junit.Test;
import static org.junit.Assert.*;

public class testDJS {

    @Test
    public void testCorrectness() {
        IntDJS test = new IntDJS(100);
        test.connect(12, 43);
        test.connect(43, 15);
        test.connect(15, 0);
        test.connect(34, 35);
        test.connect(0, 35);
        test.connect(65, 88);
        test.connect(34, 65);
        boolean result0_15 = test.isConnected(0, 15);
        boolean result12_35 = test.isConnected(12, 35);
        boolean result65_12 = test.isConnected(65, 12);
        assertTrue(result0_15);
        assertTrue(result12_35);
        assertTrue(result65_12);

        int size_12 = test.weightOfRoot(12);
        assertEquals(8, size_12);
    }
}
