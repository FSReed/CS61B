package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {

    /* Add the Ns AList.*/
    public static AList<Integer> Ns;
    public static AList<Double> times;
    public static AList<Integer> opCounts;
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
        timeAListConstruction();
        printTimingTable(Ns, times, opCounts);
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE

         /* First we need to construct the ALists in this class.
         * But in fact the construction can be implemented when declaring the variables.
         */
        Ns = new AList<>();
        times = new AList<>();
        opCounts = new AList<>();

        int size = 1000;
        for (int i = 0; i < 8; i++) {
            Ns.addLast(size);
            size = size * 2;
        }
        for(int position = 0; position < Ns.size(); position += 1) {
            opCounts.addLast(Ns.get(position));
            AList<Integer> test = new AList<>();
            int computeTime = Ns.get(position);
            Stopwatch nwtime = new Stopwatch(); // Start time test.
            for (int j = 0; j < computeTime; j += 1 ) {
                test.addLast(0);
            }
            double cost = nwtime.elapsedTime();
            times.addLast(cost);
        }
    }
}
