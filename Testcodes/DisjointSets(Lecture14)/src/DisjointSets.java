public interface DisjointSets {
    /** Connect two items P and Q */
    void connect(int P, int Q);
    boolean isConnected(int P, int Q);
}
