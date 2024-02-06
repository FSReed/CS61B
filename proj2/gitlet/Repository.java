package gitlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

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
 *          - stage/        -- stores the adding of the next commit
 *          - branches      -- stores the information of branches
 *            - HEAD        -- stores the HEAD commit
 *            - Current     -- stores the current branch of the repo
 *            - BRANCH_TREE -- stores all the branches and its corresponding commit
 *          - COMMIT_TREE   -- stores the commit tree of the repo
 */
public class Repository {
    /**
     * TODO: add instance variables here.
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
    /** The staging directory */
    public static final File STAGING_PATH = join(GITLET_DIR, "stage");
    /** The branches of the repo. */
    public static final File BRANCH_PATH = join(GITLET_DIR, "branches");
    /** The HEAD of the repo */
    public static final File HEAD = join(BRANCH_PATH, "HEAD");
    /** The current branch of the repo */
    public static final File CURRENT_BRANCH = join(BRANCH_PATH, "Current");
    /** Use hashmap to store the branches
     * The mapping is: branch name -> sha1 code of the commit
     */
    public static HashMap<String, String> branchTree = new HashMap<>();
    public static final File BRANCH_TREE = join(BRANCH_PATH, "BRANCH_TREE");

    /** The commit tree of the repo */
    public static HashMap<String, Commit> commitTree = new HashMap<>();
    public static final File COMMIT_TREE = join(GITLET_DIR, "COMMIT_TREE");


    /* TODO: fill in the rest of this class. */

    /** Build up a new Repository */
    public static void initializeRepo() throws IOException{
        GITLET_DIR.mkdir();
        COMMIT_PATH.mkdir();
        BRANCH_PATH.mkdir();
        HEAD.createNewFile();
        BRANCH_TREE.createNewFile();
        COMMIT_TREE.createNewFile();
        /* Create the master branch */
        writeContents(CURRENT_BRANCH, "master");
        writeObject(BRANCH_TREE, branchTree);
        commit("initial commit");
    }

    /** Make a commit with message s.
     * The parent commit will be the HEAD of this repo.
     * For the first commit, its parentCommit will be null.
     */
    public static void commit(String s) throws IOException {
        String parentCommit = readContentsAsString(HEAD);
        Commit newOne = new Commit(s, parentCommit);
        String hashing = newOne.saveCommit();
        commitTree.put(hashing, newOne);
        writeObject(COMMIT_TREE, commitTree);
        updateBranchAfterCommit(hashing);
    }

    /** Add a file to the staging area */
    public static boolean add(String fileName) throws IOException{
        File currentFile = join(CWD, fileName);
        if (!currentFile.exists()) {
            return false;
        }
        boolean needToUpdate = compareWithPrevFile(fileName);
        if (needToUpdate) {
            // TODO: Update
            String content = readContentsAsString(currentFile);
            String SHA1Code = sha1(content);
            File newBlob = join(BLOB_PATH, SHA1Code);
            writeContents(newBlob, content);
            return newBlob.createNewFile();
        }
        return true;
    }

    /** Output the gitlet log */
    public static void log() {
        String hashing = readContentsAsString(HEAD);
        Commit currentCommit = findCommit(hashing);
        while(currentCommit != null) {
            printCommit(currentCommit, hashing);
            hashing = currentCommit.getParent();
            currentCommit = findCommit(hashing);
        }
    }

    /** Print one commit */
    private static void printCommit(Commit commit, String SHA1) {
        System.out.println("===");
        System.out.println("commit" + " " + SHA1);
        System.out.println("Date:" + " " + commit.getTimeStamp());
        System.out.println(commit.getMessage());
        System.out.println();
    }

    /** Compare the content of the current file to the content in the previous commit */
    private static boolean compareWithPrevFile(String fileName) {
        return true;
    }

    /** Update the branch after a commit */
    public static void updateBranchAfterCommit(String hashing) {
        writeContents(HEAD, hashing);
        String crtBranch = readContentsAsString(CURRENT_BRANCH);
        branchTree = readObject(BRANCH_TREE, HashMap.class);
        branchTree.put(crtBranch, hashing);
        writeObject(BRANCH_TREE, branchTree);
    }

    /** Given a sha1 code, return the commit it represents */
    public static Commit findCommit(String SHA1) {
        commitTree = readObject(COMMIT_TREE, HashMap.class);
        return commitTree.get(SHA1);
    }
}
