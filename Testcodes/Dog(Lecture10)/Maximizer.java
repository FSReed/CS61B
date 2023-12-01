public class Maximizer {
    public static OurComparable max(OurComparable[] a) {
        int maxIdex = 0;
        for (int i = 0; i < a.length; i += 1) {
            int cmp = a[i].compareTo(a[maxIdex]);
            if (cmp > 0) {
                maxIdex = i;
            }
        }
        return a[maxIdex];
    }
}