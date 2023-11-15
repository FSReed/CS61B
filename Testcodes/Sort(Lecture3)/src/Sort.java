public class Sort{
    public static void main(String[] args) {
        String[] target = {"This", "is", "not", "what", "I", "am", "expecting", "to", "happen"};
        sort(target);
        for (String s : target) {
            System.out.println(s);
        }
    }
    public static void sort(String[] X){
        sort(X, 0);
    }

    private  static void sort(String[] X, int start){
        if (start == X.length) {
            return;
        } else{
            int smallestIndex = findSmallestIndex(X, start);
            swap(X, start, smallestIndex);
            sort(X, start + 1);
        }
    }

    public static int findSmallestIndex(String[] X, int start){
        int smallestIndex = start;
        for(int i = start; i < X.length; i += 1){
            if (X[i].compareTo(X[smallestIndex]) < 0){
                smallestIndex = i;
            }
        }
        return smallestIndex;
    }

    public static void swap(String[] X, int a, int b){
        String tmp = X[a];
        X[a] = X[b];
        X[b] = tmp;
    }

}