package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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


    /* TODO: fill in the rest of this class. */

    /** Build up a new Repository */
    public static void initializeRepo() throws IOException{
        GITLET_DIR.mkdir();
        COMMIT_PATH.mkdir();
        BRANCH_PATH.mkdir();
        BLOB_PATH.mkdir();
        HEAD.createNewFile();
        /* Make sure there will always be a map in the BRANCH_TREE */
        writeObject(BRANCH_TREE, new HashMap<>());
        BRANCH_TREE.createNewFile();
        /* Make sure there will always be a treemap of snapshots in the Staging Area */
        writeObject(STAGED, new TreeMap<>());
        STAGED.createNewFile();
        /* Make sure there will always be a map in the COMMIT_TREE */
        writeObject(COMMIT_TREE, new HashMap<>());
        COMMIT_TREE.createNewFile();
        /* Create the master branch */
        writeContents(CURRENT_BRANCH, "master");
        initialCommit();
    }

    /** Helper function for creating a initial commit */
    private static void initialCommit() throws IOException{
        Commit tmp = Commit.createInitialCommit();
        String initialHash = saveCommit(tmp);
        addToCommitTree(initialHash, tmp);
        updateBranchAfterCommit(initialHash);
    }

    /** Make a commit with message s.
     * The parent commit will be the HEAD of this repo.
     * For the first commit, its parentCommit will be null.
     */
    public static void commit(String s) throws IOException {
        String parentCommit = readContentsAsString(HEAD);
        Commit newOne = new Commit(s, parentCommit);
        copySnapshotFromParent(newOne);
        clearStagingArea(newOne);
        String hashing = saveCommit(newOne);
        addToCommitTree(hashing, newOne);
        updateBranchAfterCommit(hashing);
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
    private static String saveCommit(Commit commit) throws IOException {
        String result = commit.message
                + commit.timeStamp
                + commit.snapshots.toString()
                + commit.parentCommit;
        String fileName = sha1(result);
        File tmp = join(Repository.COMMIT_PATH, fileName);
        if (!tmp.exists()) {
            writeObject(tmp, commit);
            tmp.createNewFile();
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
    private static void clearStagingArea(Commit commit) throws IOException{
        TreeMap<String, String> tmp = readObject(STAGED, TreeMap.class);
        for(Map.Entry<String, String> entry: tmp.entrySet()) {
            String fileName = entry.getKey();
            String content = entry.getValue();
            String hashing = sha1(content);
            File blob = join(BLOB_PATH, hashing);
            if (!blob.exists()) {
                blob.createNewFile();
                writeContents(blob, content);
            }
            commit.snapshots.put(fileName, hashing);
        }
        /* Clear the staging area after commit */
        writeObject(STAGED, new TreeMap<>());
    }
    /* ------------End of helper function for commit-------------------------*/

    /** Add a file to the staging area */
    public static boolean addToStagingArea(String fileName) throws IOException{
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
    private static void updateFileContent(String fileName) throws IOException{
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

    /** Print one commit */
    private static void printCommit(Commit commit, String SHA1) {
        System.out.println("===");
        System.out.println("commit" + " " + SHA1);
        System.out.println("Date:" + " " + commit.timeStamp);
        System.out.println(commit.message);
        System.out.println();
    }
}
