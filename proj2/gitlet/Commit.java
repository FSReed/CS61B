package gitlet;

// TD: any imports you need here

import java.io.Serializable;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;
import java.util.TreeMap;

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
    public String message;

    /** The timestamp of this Commit. */
    public String timeStamp;

    /** The files that has been changed in this Commit.
     *  The mapping of file name -> sha1 of the blob
     */
    public TreeMap<String, String> snapshots;

    /** The parent commit, represented using hash code */
    public String parentCommit;

    /* TODO: fill in the rest of this class. */

    /** The Constructor
     * TODO: Add the files to commit
     */
    public Commit(String m, String parentHash) {
        this.message = m;
        this.timeStamp = getTime(Locale.US);
        this.parentCommit = parentHash;
        this.snapshots = new TreeMap<>();
    }

    /** Helper Function for getting the date for this commit */
    private String getTime(Locale L) {
        Date current = new Date();
        Formatter formatter = new Formatter(L);
        formatter.format("%1$ta %1$tb %1$td %1$tT %1$tY %1$tz", current);
        return formatter.toString();
    }
}
