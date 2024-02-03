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
 *          .gitlet/    -- top level of folder for all persistent data
 *          - commits/  -- stores all the commits
 *          - blobs/    -- stores all the blobs
 *          - stage/    -- stores the adding of the next commit
 *          - branch    -- stores the HEAD and all branches
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** Use HashMap to store the commit tree */
    private static HashMap<String, Commit> commitTree = new HashMap<>();

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The commits directory */
    public static final File COMMIT_PATH = join(GITLET_DIR, "commits");
    /** The staging directory */
    public static final File STAGING_PATH = join(GITLET_DIR, "stage");
    /** The branches directory */
    public static final File BRANCH_PATH = join(GITLET_DIR, "branches");
    /** The HEAD of the repo */
    public static String HEAD = null;
    public static Branch currentBranch = null;

    /* TODO: fill in the rest of this class. */

    /** Build up a new Repository */
    public static void initializeRepo() {
        GITLET_DIR.mkdir();
        COMMIT_PATH.mkdir();
        STAGING_PATH.mkdir();
        BRANCH_PATH.mkdir();
        currentBranch = new Branch("master", null);
    }

    /** Make a commit with message s */
    public static void commit(String s) throws IOException {
        Commit newOne = new Commit(s, null);
        File fileName = newOne.saveCommit();
        String a = sha1(readContentsAsString(fileName));
        HEAD = a;
        currentBranch.attachTo(a);
        commitTree.put(a, newOne);
        currentBranch.saveBranch(join(BRANCH_PATH, currentBranch.getName()));
    }
}
