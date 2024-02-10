package gitlet;

import org.eclipse.jetty.util.IO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;

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

    /* TODO: fill in the rest of this class. */

    /** Build up a new Repository */
    public static void initializeRepo(){
        GITLET_DIR.mkdir();
        COMMIT_PATH.mkdir();
        BRANCH_PATH.mkdir();
        BLOB_PATH.mkdir();
        createFile(HEAD);
        /* Make sure there will always be a map in the BRANCH_TREE */
        writeObject(BRANCH_TREE, new HashMap<>());
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
        updateBranchAfterCommit(initialHash);
    }

    /** Make a commit with message s.
     * The parent commit will be the HEAD of this repo.
     * For the first commit, its parentCommit will be null.
     */
    public static boolean commit(String s) {
        String parentCommit = readContentsAsString(HEAD);
        Commit newOne = new Commit(s, parentCommit);
        copySnapshotFromParent(newOne);
        boolean changed = clearStagingArea(newOne);
        if (!changed) {
            return false;
        }
        String hashing = saveCommit(newOne);
        addToCommitTree(hashing, newOne);
        updateBranchAfterCommit(hashing);
        return true;
    }

    /** Helper Function for copying the snapshot of its parent */
    private static void copySnapshotFromParent(Commit commit) {
        Commit parentCommit = findCommit(commit.parentCommit);
        if (parentCommit == null) {
            return;
        }
        commit.snapshots.putAll(parentCommit.snapshots);
    }

    /** Add a commit to the commit tree */
    private static void addToCommitTree(String hashing, Commit commit) {
        HashMap<String, Commit> tmp = readObject(COMMIT_TREE, HashMap.class);
        tmp.put(hashing, commit);
        writeObject(COMMIT_TREE, tmp);
    }

    /** Save the commit into .gitlet/commits and return its name */
    private static String saveCommit(Commit commit) {
        String result = commit.message
                + commit.timeStamp
                + commit.snapshots.toString()
                + commit.parentCommit;
        String fileName = sha1(result);
        File tmp = join(Repository.COMMIT_PATH, fileName);
        if (!tmp.exists()) {
            writeObject(tmp, commit);
            createFile(tmp);
        }
        return fileName;
    }

    /** Update the branch after a commit */
    private static void updateBranchAfterCommit(String hashing) {
        writeContents(HEAD, hashing);
        String crtBranch = readContentsAsString(CURRENT_BRANCH);
        HashMap<String, String> tmp = readObject(BRANCH_TREE, HashMap.class);
        tmp.put(crtBranch, hashing);
        writeObject(BRANCH_TREE, tmp);
    }

    /** Given a sha1 code, return the commit it represents */
    private static Commit findCommit(String SHA1) {
        HashMap<String, Commit> tmp = readObject(COMMIT_TREE, HashMap.class);
        return tmp.get(SHA1); // Can be null.
    }

    /** Create Blobs and update the commit */
    private static boolean clearStagingArea(Commit commit){
        TreeMap<String, String> tmp = readObject(STAGED, TreeMap.class);
        if (tmp.isEmpty()) {
            return false;
        }
        for (Map.Entry<String, String> entry: tmp.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            if (content == null) {
                commit.snapshots.remove(fileName);
                continue;
            }
            String hashing = sha1(content);
            File blob = join(BLOB_PATH, hashing);
            if (!blob.exists()) {
                createFile(blob);
                writeContents(blob, content);
            }
            commit.snapshots.put(fileName, hashing);
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
        TreeMap<String, String> fileList = prevCommit.snapshots;
        String blobHash = fileList.get(fileName); // log(N). Can be null.
        File currentFile = join(CWD, fileName);
        String contentInFile = readContentsAsString(currentFile);
        String newBlobHash = sha1(contentInFile); // Won't be null.
        TreeMap<String, String> tmp = readObject(STAGED, TreeMap.class);
        if (newBlobHash.equals(blobHash)) {
            tmp.remove(fileName); // The only case we won't stage the file
        } else {
            tmp.put(fileName, contentInFile);
        }
        writeObject(STAGED, tmp);
    }
    /* ------------End of helper function for addToStagingArea---------------*/

    /** gitlet rm */
    public static boolean remove(String fileName) {
        TreeMap<String, String> tmp = readObject(STAGED, TreeMap.class);
        String staged = tmp.remove(fileName);
        Commit prevCommit = findCommit(readContentsAsString(HEAD));
        boolean tracked = prevCommit.snapshots.containsKey(fileName);
        if (tracked) {
            File target = join(CWD, fileName);
            if (target.exists()) {
                restrictedDelete(target);
            }
            tmp.put(fileName, null);
        }
        writeObject(STAGED, tmp);
        return staged != null || tracked;
    }

    /** Output the gitlet log */
    public static void log() {
        String hashing = readContentsAsString(HEAD);
        Commit currentCommit = findCommit(hashing);
        while(currentCommit != null) {
            printCommit(currentCommit, hashing);
            hashing = currentCommit.parentCommit;
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
    private static void printCommit(Commit commit, String SHA1) {
        System.out.println("===");
        System.out.println("commit" + " " + SHA1);
        System.out.println("Date:" + " " + commit.timeStamp);
        System.out.println(commit.message);
        System.out.println();
    }
    /* ------------End of helper function for logs---------------------------*/

    /** gitlet find */
    public static boolean find(String targetMessage) {
        boolean result = false;
        List<String> allCommits = plainFilenamesIn(COMMIT_PATH);
        assert allCommits != null;
        for (String commitID: allCommits) {
            Commit current = findCommit(commitID);
            if (current.message.equals(targetMessage)) {
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
        HashMap<String, String> branchTree = readObject(BRANCH_TREE, HashMap.class);
        FileHeap heap = new FileHeap(branchTree.size());
        for (String branchName: branchTree.keySet()) {
            heap.add(branchName);
        }
        String currentBranch = readContentsAsString(CURRENT_BRANCH);
        heap.printWithTarget(currentBranch);
        System.out.println();
    }

    private static void printStagingArea() {
        TreeMap<String, String> stagingArea = readObject(STAGED, TreeMap.class);
        FileHeap stagedFiles = new FileHeap(stagingArea.size());
        FileHeap removedFiles = new FileHeap(stagingArea.size());
        for (Map.Entry<String, String> entry: stagingArea.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            if (content == null) {
                removedFiles.add(fileName);
            } else {
                stagedFiles.add(fileName);
            }
        }
        System.out.println("=== Staged Files ===");
        stagedFiles.printWithTarget("");
        System.out.println();
        System.out.println("=== Removed Files ===");
        removedFiles.printWithTarget("");
        System.out.println();
    }

    private static void printExtra() {
        System.out.println("=== Untracked Files ===");
        FileHeap tmp = findUntrackedFiles();
        tmp.printWithTarget("");
        System.out.println();
    }

    private static FileHeap findUntrackedFiles() {
        List<String> allFile = plainFilenamesIn(CWD);
        if (allFile == null) {
            return new FileHeap(0); // An Empty Heap.
        }
        FileHeap untrackedFiles = new FileHeap(allFile.size());
        String headHash = readContentsAsString(HEAD);
        Commit headCommit = findCommit(headHash);
        TreeMap<String, String> stagingArea = readObject(STAGED, TreeMap.class);
        for (String fileName: allFile) {
            /* get(fileName) == null indicates no key or staged for removal */
            if (!headCommit.snapshots.containsKey(fileName)
                    && stagingArea.get(fileName) == null) {
                untrackedFiles.add(fileName);
            }
        }
        return untrackedFiles;
    }
    /* ------------End of helper function for status-------------------------*/

    /** gitlet checkout */
    public static int checkoutOneFileToCommit(String commitID, String fileName) {
        commitID = findFullCommitID(commitID);
        Commit targetCommit = findCommit(commitID);
        if (targetCommit == null) {
            return CHECKOUT_NO_COMMIT;
        }
        if (!targetCommit.snapshots.containsKey(fileName)) {
            return CHECKOUT_NO_FILE_IN_COMMIT;
        }
        File targetFile = join(CWD, fileName);
        if (!targetFile.exists()) {
            createFile(targetFile);
        }
        String blobHash = targetCommit.snapshots.get(fileName); // Get SHA1 of content.
        String contentInFile = readContentsAsString(join(BLOB_PATH, blobHash));
        writeContents(targetFile, contentInFile);
        return CHECKOUT_SUCCESS;
    }

    public static int checkoutOneFileToHEAD(String fileName) {
        String currentCommitID = readContentsAsString(HEAD);
        return checkoutOneFileToCommit(currentCommitID, fileName);
    }

    public static int checkoutToBranch(String branchName) {
        HashMap<String, String> branchTree = readObject(BRANCH_TREE, HashMap.class);
        String currentBranch = readContentsAsString(CURRENT_BRANCH);
        if (!branchTree.containsKey(branchName)) {
            return CHECKOUT_NO_BRANCH_EXISTS;
        }
        if (branchName.equals(currentBranch)) {
            return CHECKOUT_SAME_BRANCH;
        }
        FileHeap untrackedFiles = findUntrackedFiles();
        if (!untrackedFiles.isEmpty()) {
            return CHECKOUT_UNTRACKED_FILE;
        }
        /* Checkout to a branch */
        String targetHash = branchTree.get(branchName);
        Commit targetCommit = findCommit(targetHash);
        writeContents(CURRENT_BRANCH, branchName); // Change current branch
        writeContents(HEAD, targetHash); // Change HEAD Commit
        deleteAndModify(targetCommit);
        writeObject(STAGED, new TreeMap<>()); // Clear the staging area
        return CHECKOUT_SUCCESS;
    }

    /** Delete the files in the working space which are not tracked in a commit
     *  Then Modify the files in the commit
     */
    private static void deleteAndModify(Commit commit) {
        List<String> fileList = plainFilenamesIn(CWD);
        /* Delete untracked files */
        assert fileList != null;
        for (String fileName: fileList) {
            if (!commit.snapshots.containsKey(fileName)) {
                File tmp = join(CWD, fileName);
                restrictedDelete(tmp);
            }
        }
        /* Modify tracked files */
        for (Map.Entry<String, String> entry: commit.snapshots.entrySet()) {
            String fileName = entry.getKey();
            String blobHash = entry.getValue();
            File targetFile = join(CWD, fileName);
            if (!targetFile.exists()) {
                createFile(targetFile);
            }
            String content = readContentsAsString(join(BLOB_PATH, blobHash));
            writeContents(targetFile, content);
        }
    }
    /** Return the FullID of a shortID. If no such ID exists, return null. */
    private static String findFullCommitID(String shortID) {
        int IDLength = shortID.length();
        List<String> commitList = plainFilenamesIn(COMMIT_PATH);
        assert commitList != null;
        for (String fullID: commitList) {
            String tmp = fullID.substring(0, IDLength);
            if (tmp.equals(shortID)) {
                return fullID;
            }
        }
        return null;
    }
    /* ------------End of helper function for checkout-----------------------*/

    /** gitlet branch, rm-branch */
    public static boolean addNewBranch(String branchName) {
        HashMap<String, String> branchTree = readObject(BRANCH_TREE, HashMap.class);
        if (branchTree.containsKey(branchName)) {
            return false;
        }
        String headCommitHash = readContentsAsString(HEAD);
        branchTree.put(branchName, headCommitHash);
        writeObject(BRANCH_TREE, branchTree);
        return true;
    }

    public static int removeBranch(String branchName) {
        HashMap<String, String> branchTree = readObject(BRANCH_TREE, HashMap.class);
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

    private static void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void test() {
    }
}
