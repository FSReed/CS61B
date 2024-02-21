package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static gitlet.Utils.*;

// TD: any imports you need here

/** Represents a gitlet repository.
 *  TD: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *
 *  @author FSReed
 *          The structure of the repository:
 *          .gitlet/        -- top level of folder for all persistent data
 *          - commits/      -- stores all the commits
 *          - blobs/        -- stores all the blobs
 *          - Staged Files  -- stores the snapshot of files to be committed
 *          - branches/     -- stores the information of branches
 *            - HEAD        -- stores the HEAD commit
 *            - Current     -- stores the current branch of the repo
 *            - BRANCH_TREE -- stores all the branches and its corresponding commit
 *          - COMMIT_TREE   -- stores the commit tree of the repo
 */
public class Repository {
    /**
     * TD: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commits directory */
    public static final File COMMIT_PATH = join(GITLET_DIR, "commits");
    /** The blob directory */
    public static final File BLOB_PATH = join(GITLET_DIR, "blobs");
    /** The snapshot map inside the Staging Area */
    public static final File STAGED = join(GITLET_DIR, "Staged Files");
    /** The branches of the repo. */
    public static final File BRANCH_PATH = join(GITLET_DIR, "branches");
    /** The HEAD of the repo */
    public static final File HEAD = join(BRANCH_PATH, "HEAD");
    /** The current branch of the repo */
    public static final File CURRENT_BRANCH = join(BRANCH_PATH, "Current");
    /** Use hashmap to store the branches
     * The mapping is: branch name(String) -> sha1 code of the commit(String)
     */
    public static final File BRANCH_TREE = join(BRANCH_PATH, "BRANCH_TREE");

    /** Use hashmap to store the commits
     * The mapping is: sha1 code of the commit(String) -> commit(Commit)
     */
    public static final File COMMIT_TREE = join(GITLET_DIR, "COMMIT_TREE");

    /** Used for lazy-load and lazy-cache */
    private static HashMap<String, Commit> commitTree = null;
    private static TreeMap<String, String> branchTree = null;
    private static TreeMap<String, String> stagedTree = null;

    /** Status code for checkout a file */
    public static final int CHECKOUT_SUCCESS = 0;
    public static final int CHECKOUT_NO_COMMIT = 1;
    public static final int CHECKOUT_NO_FILE_IN_COMMIT = 2;

    /** Status code for checkout to a branch */
    public static final int CHECKOUT_NO_BRANCH_EXISTS = 3;
    public static final int CHECKOUT_SAME_BRANCH = 4;
    public static final int CHECKOUT_UNTRACKED_FILE = 5;

    /** Status code for rm-branch */
    public static final int RM_SUCCESS = 0;
    public static final int RM_CURRENT_BRANCH = 1;
    public static final int RM_NO_SUCH_BRANCH = 2;

    /** Status code for reset */
    public static final int RESET_SUCCESS = 0;
    public static final int RESET_NO_COMMIT = 1;
    public static final int RESET_UNTRACKED_FILE = 2;

    /** Status code for merge */
    public static final int MERGE_SUCCESS = 0;
    public static final int MERGE_UNCOMMITTED_CHANGES = 1;
    public static final int MERGE_NO_SUCH_BRANCH = 2;
    public static final int MERGE_CURRENT_BRANCH = 3;
    public static final int MERGE_UNTRACKED_FILES = 4;
    public static final int MERGE_NO_OPERATION = 5;
    public static final int MERGE_CHECKOUT = 6;
    public static final int MERGE_CONFLICT = 7;

    /* Fill in the rest of this class. */

    /** Build up a new Repository */
    public static void initializeRepo() {
        GITLET_DIR.mkdir();
        COMMIT_PATH.mkdir();
        BRANCH_PATH.mkdir();
        BLOB_PATH.mkdir();
        createFile(HEAD);
        /* Make sure there will always be a map in the BRANCH_TREE */
        writeObject(BRANCH_TREE, new TreeMap<>());
        createFile(BRANCH_TREE);
        /* Make sure there will always be a treemap of snapshots in the Staging Area */
        writeObject(STAGED, new TreeMap<>());
        createFile(STAGED);
        /* Make sure there will always be a map in the COMMIT_TREE */
        writeObject(COMMIT_TREE, new HashMap<>());
        createFile(COMMIT_TREE);
        /* Create the master branch */
        writeContents(CURRENT_BRANCH, "master");
        initialCommit();
    }

    /** Helper function for creating a initial commit */
    private static void initialCommit() {
        Commit tmp = Commit.createInitialCommit();
        String initialHash = saveCommit(tmp);
        addToCommitTree(initialHash, tmp);
        updateBranch(initialHash);
    }

    /** Make a commit with message s.
     * The parent commit will be the HEAD of this repo.
     * For the first commit, its parentCommit will be null.
     */
    public static boolean commit(String s) {
        String parentCommit = readContentsAsString(HEAD);
        Commit newOne = new Commit(s, parentCommit);
        return processCommit(newOne);
    }

    /** Extend commit method to fit merge commits */
    private static boolean processCommit(Commit targetCommit) {
        copySnapshotFromParent(targetCommit);
        boolean changed = clearStagingArea(targetCommit);
        if (!changed) {
            return false;
        }
        String hashing = saveCommit(targetCommit);
        addToCommitTree(hashing, targetCommit);
        updateBranch(hashing);
        return true;
    }

    /** Helper Function for copying the snapshot of its parent */
    private static void copySnapshotFromParent(Commit commit) {
        Commit parentCommit = findCommit(commit.getParentCommit());
        if (parentCommit == null) {
            return;
        }
        commit.getSnapshots().putAll(parentCommit.getSnapshots());
    }

    /** Add a commit to the commit tree */
    private static void addToCommitTree(String hashing, Commit commit) {
        loadCommitTree();
        commitTree.put(hashing, commit);
        writeObject(COMMIT_TREE, commitTree);
    }

    /** Save the commit into .gitlet/commits and return its name */
    private static String saveCommit(Commit commit) {
        String fileName = commit.getSha1();
        File tmp = join(Repository.COMMIT_PATH, fileName);
        if (!tmp.exists()) {
            writeObject(tmp, commit);
            createFile(tmp);
        }
        return fileName;
    }

    /** Update the branch after a commit */
    private static void updateBranch(String hashing) {
        writeContents(HEAD, hashing);
        String crtBranch = readContentsAsString(CURRENT_BRANCH);
        loadBranchTree();
        branchTree.put(crtBranch, hashing);
        writeObject(BRANCH_TREE, branchTree);
    }

    /** Given a sha1 code, return the commit it represents */
    private static Commit findCommit(String sha1) {
        loadCommitTree();
        return commitTree.get(sha1); // Can be null.
    }

    /** Create Blobs and update the commit */
    private static boolean clearStagingArea(Commit commit) {
        loadStagedTree();
        if (stagedTree.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, String> entry: stagedTree.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            if (content == null) {
                commit.getSnapshots().remove(fileName);
                continue;
            }
            String hashing = sha1(content);
            File blob = join(BLOB_PATH, hashing);
            if (!blob.exists()) {
                createFile(blob);
                writeContents(blob, content);
            }
            commit.getSnapshots().put(fileName, hashing);
        }
        /* Clear the staging area after commit */
        writeObject(STAGED, new TreeMap<>());
        return true;
    }
    /* ------------End of helper function for commit-------------------------*/

    /** Add a file to the staging area */
    public static boolean addToStagingArea(String fileName) {
        File currentFile = join(CWD, fileName);
        if (!currentFile.exists()) {
            return false;
        }
        updateFileContent(fileName);
        return true;
    }

    /** Compare the content of the current file to the content in the previous commit.
     *  The previous commit is the commit pointed by HEAD.
     */
    private static void updateFileContent(String fileName) {
        Commit prevCommit = findCommit(readContentsAsString(HEAD)); // Constant
        TreeMap<String, String> fileList = prevCommit.getSnapshots();
        String blobHash = fileList.get(fileName); // log(N). Can be null.
        File currentFile = join(CWD, fileName);
        String contentInFile = readContentsAsString(currentFile);
        String newBlobHash = sha1(contentInFile); // Won't be null.
        loadStagedTree();
        if (newBlobHash.equals(blobHash)) {
            stagedTree.remove(fileName); // The only case we won't stage the file
        } else {
            stagedTree.put(fileName, contentInFile);
        }
        writeObject(STAGED, stagedTree);
    }
    /* ------------End of helper function for addToStagingArea---------------*/

    /** gitlet rm */
    public static boolean remove(String fileName) {
        loadStagedTree();
        String staged = stagedTree.remove(fileName);
        Commit prevCommit = findCommit(readContentsAsString(HEAD));
        boolean tracked = prevCommit.getSnapshots().containsKey(fileName);
        if (tracked) {
            File target = join(CWD, fileName);
            if (target.exists()) {
                restrictedDelete(target);
            }
            stagedTree.put(fileName, null);
        }
        writeObject(STAGED, stagedTree);
        return staged != null || tracked;
    }

    /** Output the gitlet log */
    public static void log() {
        String hashing = readContentsAsString(HEAD);
        Commit currentCommit = findCommit(hashing);
        while (currentCommit != null) {
            printCommit(currentCommit, hashing);
            hashing = currentCommit.getParentCommit();
            currentCommit = findCommit(hashing);
        }
    }

    public static void globalLog() {
        List<String> allCommit = plainFilenamesIn(COMMIT_PATH);
        assert allCommit != null;
        for (String commitID: allCommit) {
            Commit currentCommit = findCommit(commitID);
            printCommit(currentCommit, commitID);
        }
    }
    /** Print one commit */
    private static void printCommit(Commit commit, String sha1) {
        System.out.println("===");
        System.out.println("commit" + " " + sha1);
        if (commit instanceof MergeCommit) {
            printMergeInfo((MergeCommit) commit);
        }
        System.out.println("Date:" + " " + commit.getTimeStamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    /** Print the line that contains merge information */
    private static void printMergeInfo(MergeCommit commit) {
        String parent = commit.getParentCommit().substring(0, 7);
        String mergeinParent = commit.getMergeCommit().substring(0, 7);
        System.out.println("Merge: " + parent + " " + mergeinParent);
    }
    /* ------------End of helper function for logs---------------------------*/

    /** gitlet find */
    public static boolean find(String targetMessage) {
        boolean result = false;
        List<String> allCommits = plainFilenamesIn(COMMIT_PATH);
        assert allCommits != null;
        for (String commitID: allCommits) {
            Commit current = findCommit(commitID);
            if (current.getMessage().equals(targetMessage)) {
                result = true;
                System.out.println(commitID);
            }
        }
        return result;
    }
    /* ------------End of helper function for find---------------------------*/

    /** gitlet status */
    public static void status() {
        printBranchState();
        printStagingArea();
        printExtra();
    }

    private static void printBranchState() {
        System.out.println("=== Branches ===");
        loadBranchTree();
        String currentBranch = readContentsAsString(CURRENT_BRANCH);
        for (String branchName: branchTree.keySet()) {
            if (branchName.equals(currentBranch)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }
        System.out.println();
    }

    private static void printStagingArea() {
        loadStagedTree();
        TreeSet<String> stagedFiles = new TreeSet<>();
        TreeSet<String> removedFiles = new TreeSet<>();
        for (Map.Entry<String, String> entry: stagedTree.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            if (content == null) {
                removedFiles.add(fileName);
            } else {
                stagedFiles.add(fileName);
            }
        }
        System.out.println("=== Staged Files ===");
        for (String fileName: stagedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String fileName: removedFiles) {
            System.out.println(fileName);
        }
        System.out.println();
    }

    private static void printExtra() {
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
        TreeSet<String> tmp = findUntrackedFiles();
        for (String fileName: tmp) {
            System.out.println(fileName);
        }
        System.out.println();
    }

    private static TreeSet<String> findUntrackedFiles() {
        List<String> allFile = plainFilenamesIn(CWD);
        if (allFile == null) {
            return new TreeSet<>(); // An Empty Heap.
        }
        TreeSet<String> untrackedFiles = new TreeSet<>();
        String headHash = readContentsAsString(HEAD);
        Commit headCommit = findCommit(headHash);
        loadStagedTree();
        for (String fileName: allFile) {
            /* get(fileName) == null indicates no key or staged for removal */
            if (!headCommit.getSnapshots().containsKey(fileName)
                    && stagedTree.get(fileName) == null) {
                untrackedFiles.add(fileName);
            }
        }
        return untrackedFiles;
    }
    /* ------------End of helper function for status-------------------------*/

    /** gitlet checkout */
    public static int checkoutOneFileToCommit(String commitID, String fileName) {
        commitID = findFullCommitID(commitID);
        if (commitID == null) {
            return CHECKOUT_NO_COMMIT;
        }
        Commit targetCommit = findCommit(commitID);
        if (!targetCommit.getSnapshots().containsKey(fileName)) {
            return CHECKOUT_NO_FILE_IN_COMMIT;
        }
        File targetFile = join(CWD, fileName);
        if (!targetFile.exists()) {
            createFile(targetFile);
        }
        String blobHash = targetCommit.getSnapshots().get(fileName); // Get SHA1 of content.
        String contentInFile = readContentsAsString(join(BLOB_PATH, blobHash));
        writeContents(targetFile, contentInFile);
        return CHECKOUT_SUCCESS;
    }

    public static int checkoutOneFileToHEAD(String fileName) {
        String currentCommitID = readContentsAsString(HEAD);
        return checkoutOneFileToCommit(currentCommitID, fileName);
    }

    public static int checkoutToBranch(String branchName) {
        loadBranchTree();
        String currentBranch = readContentsAsString(CURRENT_BRANCH);
        if (!branchTree.containsKey(branchName)) {
            return CHECKOUT_NO_BRANCH_EXISTS;
        }
        if (branchName.equals(currentBranch)) {
            return CHECKOUT_SAME_BRANCH;
        }
        TreeSet<String> untrackedFiles = findUntrackedFiles();
        if (!untrackedFiles.isEmpty()) {
            return CHECKOUT_UNTRACKED_FILE;
        }
        /* Checkout to a branch */
        String targetHash = branchTree.get(branchName);
        writeContents(CURRENT_BRANCH, branchName); // Change current branch
        writeContents(HEAD, targetHash); // Change HEAD Commit
        deleteAndModify(targetHash);
        return CHECKOUT_SUCCESS;
    }

    /** Delete the files in the working space which are not tracked in a commit
     *  Then Modify the files in the commit
     */
    private static void deleteAndModify(String commitHash) {
        Commit commit = findCommit(commitHash);
        List<String> fileList = plainFilenamesIn(CWD);
        /* Delete untracked files */
        assert fileList != null;
        for (String fileName: fileList) {
            if (!commit.getSnapshots().containsKey(fileName)) {
                File tmp = join(CWD, fileName);
                restrictedDelete(tmp);
            }
        }
        /* Modify tracked files */
        for (Map.Entry<String, String> entry: commit.getSnapshots().entrySet()) {
            String fileName = entry.getKey();
            String blobHash = entry.getValue();
            File targetFile = join(CWD, fileName);
            if (!targetFile.exists()) {
                createFile(targetFile);
            }
            String content = readContentsAsString(join(BLOB_PATH, blobHash));
            writeContents(targetFile, content);
        }
        stagedTree = new TreeMap<>();
        writeObject(STAGED, stagedTree); // Clear the staging area
    }
    /** Return the FullID of a shortID. If no such ID exists, return null. */
    private static String findFullCommitID(String shortID) {
        int idLength = shortID.length();
        List<String> commitList = plainFilenamesIn(COMMIT_PATH);
        assert commitList != null;
        for (String fullID: commitList) {
            String tmp = fullID.substring(0, idLength);
            if (tmp.equals(shortID)) {
                return fullID;
            }
        }
        return null;
    }
    /* ------------End of helper function for checkout-----------------------*/

    /** gitlet branch, rm-branch */
    public static boolean addNewBranch(String branchName) {
        loadBranchTree();
        if (branchTree.containsKey(branchName)) {
            return false;
        }
        String headCommitHash = readContentsAsString(HEAD);
        branchTree.put(branchName, headCommitHash);
        writeObject(BRANCH_TREE, branchTree);
        return true;
    }

    public static int removeBranch(String branchName) {
        loadBranchTree();
        String currentBranch = readContentsAsString(CURRENT_BRANCH);
        if (currentBranch.equals(branchName)) {
            return RM_CURRENT_BRANCH;
        }
        if (!branchTree.containsKey(branchName)) {
            return RM_NO_SUCH_BRANCH;
        }
        branchTree.remove(branchName);
        writeObject(BRANCH_TREE, branchTree);
        return RM_SUCCESS;
    }
    /* ------------End of helper function for (rm-)branch--------------------*/

    /** gitlet reset */
    public static int reset(String commitID) {
        commitID = findFullCommitID(commitID);
        if (commitID == null) {
            return RESET_NO_COMMIT;
        }
        TreeSet<String> untrackedFiles = findUntrackedFiles();
        if (!untrackedFiles.isEmpty()) {
            return RESET_UNTRACKED_FILE;
        }
        deleteAndModify(commitID);
        updateBranch(commitID);
        return RESET_SUCCESS;
    }
    /* ------------End of helper function for reset--------------------------*/

    /** gitlet merge */
    public static int merge(String targetBranch) {
        loadBranchTree();
        loadStagedTree();
        if (!stagedTree.isEmpty()) {
            return MERGE_UNCOMMITTED_CHANGES;
        }
        if (!branchTree.containsKey(targetBranch)) {
            return MERGE_NO_SUCH_BRANCH;
        }
        String currentBranch = readContentsAsString(CURRENT_BRANCH);
        if (targetBranch.equals(currentBranch)) {
            return MERGE_CURRENT_BRANCH;
        }
        TreeSet<String> untrackedFiles = findUntrackedFiles();
        if (!untrackedFiles.isEmpty()) {
            return MERGE_UNTRACKED_FILES;
        }
        /* End of pre-check */
        String currentCommitHash = branchTree.get(currentBranch);
        String targetCommitHash = branchTree.get(targetBranch);
        String splitCommitHash = findSplitPoint(currentCommitHash, targetCommitHash);
        if (splitCommitHash.equals(targetCommitHash)) {
            return MERGE_NO_OPERATION;
        }
        if (splitCommitHash.equals(currentCommitHash)) {
            checkoutToBranch(targetBranch);
            return MERGE_CHECKOUT;
        }
        Commit splitPoint = findCommit(splitCommitHash);
        Commit currentCommit = findCommit(currentCommitHash);
        Commit targetCommit = findCommit(targetCommitHash);
        HashSet<String> allFiles = new HashSet<>();
        allFiles.addAll(splitPoint.allFiles());
        allFiles.addAll(currentCommit.allFiles());
        allFiles.addAll(targetCommit.allFiles()); // A HashSet containing all files recorded.
        boolean mergeConflict = false;
        for (String fileName: allFiles) {
            boolean hasConflict = mergeFile(fileName, splitCommitHash,
                    currentCommitHash, targetCommitHash);
            mergeConflict = mergeConflict || hasConflict;
        }
        // Commit
        mergingCommit(currentBranch, targetBranch);
        return mergeConflict ? MERGE_CONFLICT : MERGE_SUCCESS;
    }

    /** Find the split point of two commit
     *  Do BFS of two given commits simultaneously
     *  When in an iteration, a commit is found in another path,
     *  this commit is guaranteed to be the latest common ancestor.
     */
    private static String findSplitPoint(String firstCommit, String secondCommit) {
        loadCommitTree();
        GraphIterator graphOne = new GraphIterator(firstCommit);
        GraphIterator graphTwo = new GraphIterator(secondCommit);
        String splitPoint = null;
        int minDist = (int) Double.POSITIVE_INFINITY;
        while (!graphOne.queue.isEmpty() || !graphTwo.queue.isEmpty()) {
            int dist1 = graphOne.visit();
            int dist2 = graphTwo.visit();
            String commit1 = graphOne.currentCommit;
            String commit2 = graphTwo.currentCommit;
            if (graphTwo.contains(commit1)) {
                int totalDist1 = dist1 + graphTwo.get(commit1);
                if (totalDist1 < minDist) {
                    splitPoint = commit1;
                    minDist = totalDist1;
                }
            }
            if (graphOne.contains(commit2)) {
                int totalDist2 = dist2 + graphOne.get(commit2);
                if (totalDist2 < minDist) {
                    splitPoint = commit2;
                    minDist = totalDist2;
                }
            }
        }
        return splitPoint;
    }

    /** Graph iterator helper class */
    private static class GraphIterator {
        GraphIterator(String startingNode) {
            allPossibleNodes = new TreeMap<>();
            currentCommit = startingNode;
            allPossibleNodes.put(currentCommit, 0);
            queue = new LinkedList<>();
            queue.offer(currentCommit);
        }
        boolean hasPassed(String commitNode) {
            return allPossibleNodes.containsKey(commitNode);
        }

        /** Visit the queue, update the distances and offer.
         *  Return the current Distance from root
         */
        int visit() {
            if (!queue.isEmpty()) {
                currentCommit = queue.poll();
                updateDistAndOffer();
            }
            return allPossibleNodes.get(currentCommit);
        }
        boolean contains(String commitID) {
            return allPossibleNodes.containsKey(commitID);
        }

        int get(String commitID) {
            return allPossibleNodes.get(commitID);
        }
        /** Add the ancestor of a commit to the Queue and */
        void updateDistAndOffer() {
            Commit current = findCommit(currentCommit);
            if (current.getParentCommit().isEmpty()) {
                return;
            }
            int currentDist = allPossibleNodes.get(currentCommit);
            String parent = current.getParentCommit();
            if (!queue.contains(parent)) {
                queue.offer(parent);
                allPossibleNodes.put(parent, currentDist + 1);
            }
            if (current instanceof MergeCommit) {
                String merge = ((MergeCommit) current).getMergeCommit();
                if (!queue.contains(merge)) {
                    queue.offer(merge);
                    allPossibleNodes.put(merge, currentDist + 1);
                }
            }
        }

        TreeMap<String, Integer> allPossibleNodes;
        String currentCommit;
        LinkedList<String> queue;
    }



    /** Process the file according to three commits in merge
     *  Return whether there's a conflict during merge.
     */
    private static boolean mergeFile(String fileName, String sp, String crt, String tar) {
        if (hasSameContent(sp, crt, fileName)) {
            if (!hasSameContent(sp, tar, fileName) && !hasSameContent(crt, tar, fileName)) {
                // Represents |A|A|!A| -> !A
                int fileDeleted = checkoutOneFileToCommit(tar, fileName);
                if (fileDeleted == CHECKOUT_NO_FILE_IN_COMMIT) {
                    remove(fileName);
                } else {
                    addToStagingArea(fileName);
                }
            }
        } else {
            // Represents |A|!A1|!A2| -> Conflict
            if (!hasSameContent(sp, tar, fileName) && !hasSameContent(crt, tar, fileName)) {
                writeConflictMessage(crt, tar, fileName);
                return true;
            }
        }
        return false;
    }

    /** Returns whether two commits have the same content of one file */
    private static boolean hasSameContent(String commit1, String commit2, String fileName) {
        String contentInCommit1 = getFileContent(commit1, fileName);
        String contentInCommit2 = getFileContent(commit2, fileName);
        if (contentInCommit1 == null) {
            return contentInCommit2 == null; // Both commits don't track this file.
        } else {
            return contentInCommit1.equals(contentInCommit2);
        }
    }

    /** Write the conflict message into a file and stage it */
    private static void writeConflictMessage(String current, String target, String fileName) {
        String newContent = "<<<<<<< HEAD\n";
        newContent += getFileContent(current, fileName);
        newContent += "=======\n";
        newContent += getFileContent(target, fileName);
        newContent += ">>>>>>>\n";
        writeContents(join(CWD, fileName), newContent);
        addToStagingArea(fileName);
    }

    /** Return the content of a file */
    private static String getFileContent(String current, String fileName) {
        Commit currentCommit = findCommit(current);
        TreeMap<String, String> snapshots = currentCommit.getSnapshots();
        if (snapshots == null) {
            return "";
        }
        if (snapshots.get(fileName) == null) {
            return "";
        }
        File blob = join(BLOB_PATH, snapshots.get(fileName));
        return readContentsAsString(blob);
    }

    /** Create a merge commit */
    private static void mergingCommit(String currentBranch, String givenBranch) {
        String message = "Merged " + givenBranch
                + " into " + currentBranch + ".";
        loadBranchTree();
        String parent = branchTree.get(currentBranch);
        String target = branchTree.get(givenBranch);
        MergeCommit targetCommit = new MergeCommit(message, parent, target);
        processCommit(targetCommit);
    }

    /** Helper functions for the whole repo */
    private static void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void loadCommitTree() {
        if (commitTree == null)  {
            commitTree = readObject(COMMIT_TREE, HashMap.class);
        }
    }
    private static void loadBranchTree() {
        if (branchTree == null) {
            branchTree = readObject(BRANCH_TREE, TreeMap.class);
        }
    }
    private static void loadStagedTree() {
        if (stagedTree == null) {
            stagedTree = readObject(STAGED, TreeMap.class);
        }
    }
}
