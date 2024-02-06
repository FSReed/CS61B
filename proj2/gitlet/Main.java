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
                if (repoExists()) {
                    exitWithMessage(initialError);
                }
                Repository.initializeRepo();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                break;
            // TODO: FILL THE REST IN
            case "log":
                if (!repoExists()) {
                    exitWithMessage(repoError);
                }
                Repository.log();
                break;
            /*Just for test */
            case "commit":
                String message = args[1];
                Repository.commit(message);
                break;
        }
        System.out.println("End of procedure.");
    }

    private static void exitWithMessage(String s) {
        System.out.println(s);
        System.exit(0);
    }

    /** Check the input operands */
    private static void sanityParamCheck(String[] args, int length) {
        if (args.length != length) {
            exitWithMessage("Incorrect operands.");
        }
    }

    /** Check the existence of the repo */
    private static boolean repoExists() {
        return Repository.GITLET_DIR.exists();
    }
    private static final String initialError =
            "A Gitlet version-control system already exists in the current directory.";
    private static final String repoError =
            "Not in an initialized Gitlet directory.";
}
