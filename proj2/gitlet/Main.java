package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author FSReed
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TD: what if args is empty?
        if (args.length == 0) {
            exitWithMessage("Please enter a command.");
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TD: handle the `init` command
                if (Repository.GITLET_DIR.exists()) {
                    exitWithMessage(initialError);
                }
                Repository.initializeRepo();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
        }
    }

    private static void exitWithMessage(String s) {
        System.out.println(s);
        System.exit(0);
    }

    /** Check the input operands */
    private static void sanityCheck(String[] args, int length) {
        if (args.length != length) {
            exitWithMessage("Incorrect operands.");
        }
        if (!Repository.GITLET_DIR.exists()) {
            exitWithMessage("Not in an initialized Gitlet directory.");
        }
    }

    private static final String initialError =
            "A Gitlet version-control system already exists in the current directory.";
}
