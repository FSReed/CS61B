package gitlet;

import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import static gitlet.Utils.*;

/** Stores different branches and a HEAD
 *  @author FSReed
 */
public class Branch implements Serializable {
    private String name;
    private String attaching;

    public Branch(String s, String c) {
        this.name = s;
        this.attaching = c;
    }

    public void attachTo(String commit) {
        this.attaching = commit;
    }

    public String getName() {
        return this.name;
    }

    /** Write the new information of the branch. If this branch doesn't
     * exist before, create one.
     */
    public void saveBranch(File tmp) throws IOException {
        writeObject(tmp, this);
        if (!tmp.exists()) {
            tmp.createNewFile();
        }
    }
}
