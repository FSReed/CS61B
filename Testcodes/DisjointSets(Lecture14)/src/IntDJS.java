public class IntDJS implements DisjointSets{

    private int[] set;
    public IntDJS(int size) {
        set = new int[size];
        for (int i = 0; i < size; i++) {
            set[i] = -1;
        }
    }
    @Override
    public void connect(int P, int Q) {
        if (!isConnected(P, Q)) {
            int root1 = findRoot(P);
            int root2 = findRoot(Q);

            int weight1 = weightOfRoot(root1);
            int weight2 = weightOfRoot(root2);

            if (weight1 < weight2) {
                set[root1] = root2;
                set[root2] = set[root2] - weight1;
            } else {
                set[root2] = root1;
                set[root1] = set[root1] - weight2;
            }
        }
    }

    public boolean isConnected(int P, int Q) {

        P = findRoot(P);
        Q = findRoot(Q);
        return P == Q;
    }

    public int findRoot(int P) {
        while (set[P] >= 0) {
            P = set[P];
        }
        return P;
    }

    public int weightOfRoot(int root) {
        return -set[root];
    }
}
