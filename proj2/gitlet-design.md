# Gitlet Design Document

**Name**: FSReed

## Classes and Data Structures

### Commit

#### Fields

```Java
/** The message of this Commit. */
    public String message;

    /** The timestamp of this Commit. */
    public String timeStamp;

    /** The files that has been changed in this Commit.
     *  The mapping of file name -> sha1 of the blob
     */
    public TreeMap<String, String> snapshots;

    /** The parent commit, represented using hash code */
    public String parentCommit;
```

### Repository

#### Fields

```Java
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
```

## Structure of the repository

![design](./Design.png)

## Persistence

1. The SHA1 of a **Commit** can be computed as:

   ```Java
   String result = commit.message
                + commit.timeStamp
                + commit.snapshots.toString()
                + commit.parentCommit;
    String fileName = sha1(result);
   ```

2. The SHA1 of a **Blob** can be computed using the content of the blob straight forward.
3. In `branches`:
   - **HEAD** is serialized to store the **SHA1 code** of the commit at HEAD.
   - **Current** is serialized to store the **name** of the current branch.
   - **BRANCH_TREE** is a `HashMap` serialized to store all the branches, with a mapping of **branch-name -> commit SHA1 code**.
4. Staged Filed is a `TreeMap`.
5. `COMMIT_TREE` is a `HashMap`.

## Problems I met in this project

1. I need to come up with a way to serialize **Commit**s. As all metadata one commit has can guarantee this commit is unique, so I simple add up all the attributes of one Commit into one String and SHA1 this String.
2. When getting changed files stored in the staging area, I need to iterate over the `Staged Files`. But Java can't iterate over a TreeMap using `foreach` directly so I went to [StackOverflow](https://stackoverflow.com/questions/1318980/how-to-iterate-over-a-treemap) to find a way to implement the iteration.
