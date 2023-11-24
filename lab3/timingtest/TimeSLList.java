package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {

    public static AList<Integer> Ns;
    public static AList<Double> times;
    public static AList<Integer> opCounts;
    public static int initialSize = 1000;
    public static int testCount = 10000;
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
        printTimingTable(Ns, times, opCounts);
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        Ns = new AList<>();
        times = new AList<>();
        opCounts = new AList<>();

        for (int N = initialSize; N <= 128000; N = N * 2) {
            Ns.addLast(N);
        }
        for (int i = 0; i < Ns.size(); i += 1) {
            SLList<Double> tmp = new SLList<>();
            int currentSize = Ns.get(i);
            for (int j = 0; j < currentSize; j += 1) {
                tmp.addFirst(1.0);
            }
            Stopwatch testTime = new Stopwatch(); // Start time test.
            for (int addTimes = 0; addTimes < testCount; addTimes += 1) {
                tmp.getLast();
            }
            double currentTime = testTime.elapsedTime();
            opCounts.addLast(testCount);
            times.addLast(currentTime);
        }
    }

}
