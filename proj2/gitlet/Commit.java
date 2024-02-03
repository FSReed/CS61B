package gitlet;

// TODO: any imports you need here

import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TD: You'll likely use this in this class
import java.util.Formatter;
import java.util.Locale;
import java.io.File;

/** Represents a gitlet commit object.
 *  TD: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *  It will store a "commit message", a "timestamp", the "snapshots of the files"
 *  and its "parent commit".
 *  @author FSReed
 */
public class Commit implements Serializable {
    /**
     * TD: add instance variables here.
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;

    /** The timestamp of this Commit. */
    private String timeStamp;

    /** TODO: The files that has been changed in this Commit. */

    /** The parent commit, represented using hash code */
    private String parentCommit;

    /* TODO: fill in the rest of this class. */

    /** The Constructor
     * TODO: Add the files to commit
     */
    public Commit(String m, String parentHash) {
        this.message = m;
        this.timeStamp = getTime(Locale.US);
        this.parentCommit = parentHash;
    }

    /** Helper Function for getting the date for this commit */
    private String getTime(Locale L) {
        Date current = new Date();
        Formatter formatter = new Formatter(L);
        formatter.format("%1$ta %1$tb %1$td %1$tT %1$tY %1$tz", current);
        return formatter.toString();
    }

    public File saveCommit() throws IOException {
        int hashing= this.hashCode();
        String fileName = Integer.toString(hashing);
        File tmp = Utils.join(Repository.COMMIT_PATH, fileName);
        if (!tmp.exists()) {
            Utils.writeObject(tmp, this);
            tmp.createNewFile();
        }
        return tmp;
    }
}
