import org.junit.Test;
import static org.junit.Assert.*; /** Notice this static notation! */

public class TestSort {

    @Test
    public void testSort(){
        String[] input = {"i", "have", "an", "egg"};
        String[] expected = {"an", "egg", "have", "i"};

        Sort.sort(input);
        assertArrayEquals(input, expected);

    }

    @Test
    public void TestSmallestIndex(){
        String[] input = {"i", "have", "an", "egg"};
        int expected = 2;

        int actual = Sort.findSmallestIndex(input, 0);

        assertEquals(actual, expected);
    }

    @Test
    public void TestSwap(){
        String[] input = {"i", "have", "an", "egg"};
        int a = 0;
        int b = 2;
        String[] expected = {"an", "have", "i", "egg"};

        Sort.swap(input, a, b);
        assertArrayEquals(input, expected);
    }
}