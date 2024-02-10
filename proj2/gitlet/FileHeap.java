package gitlet;

public class FileHeap {
    private final String[] fileNames;
    private int size;
    private final int capacity;
    public FileHeap(int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.fileNames = new String[capacity + 1];
        fileNames[0] = null;
    }

    public void add(String fileName) {
        int currentPlace = size + 1;
        fileNames[currentPlace] = fileName;
        String parent = fileNames[currentPlace / 2];
        while (parent != null && fileName.compareTo(parent) < 0) {
            swap(currentPlace, currentPlace / 2);
            currentPlace = currentPlace / 2;
            parent = fileNames[currentPlace / 2];
        }
        this.size += 1;
    }

    /** WARNING: This operation will clear the heap! */
    public void printWithTarget(String currentBranch) {
        int totalSize = this.size;
        for (int i = 0; i < totalSize; i++) {
            if (fileNames[1].equals(currentBranch)) {
                System.out.print("*");
            }
            System.out.println(fileNames[1]);
            fileNames[1] = fileNames[this.size];
            this.size -= 1;
            resize();
        }
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    private void resize() {
        int currentPlace = 1;
        String target = fileNames[1];
        int left = currentPlace * 2;
        int right = currentPlace * 2 + 1;
        /* When both children are in the range */
        while (right <= this.size) {
            if (target.compareTo(fileNames[left]) <= 0
                    || target.compareTo(fileNames[right]) <= 0) {
                break;
            }
            int nextPlace = (fileNames[left].compareTo(fileNames[right]) <= 0) ? left : right;
            swap(currentPlace, nextPlace);
            currentPlace = nextPlace;
            left = currentPlace * 2;
            right = currentPlace * 2 + 1;
        }
        /* Are we missing the left child? */
        if (left == this.size && target.compareTo(fileNames[left]) > 0) {
            swap(currentPlace, left);
        }
    }

    private void swap(int first, int second) {
        String tmp = fileNames[first];
        fileNames[first] = fileNames[second];
        fileNames[second] = tmp;
    }
}
