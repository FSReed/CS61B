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
            exitWithMessage(noInputError);
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
                paramCheck(args, 2);
                String fileName = args[1];
                boolean success = Repository.addToStagingArea(fileName);
                if (!success) {
                    exitWithMessage(noFileError);
                }
                break;
            // TODO: FILL THE REST IN
            case "log":
                if (!repoExists()) {
                    exitWithMessage(noRepoError);
                }
                Repository.log();
                break;
            /* TODO: Just for log test. Modify later */
            case "commit":
                String message = args[1];
                Repository.commit(message);
                break;
            case "test":
                System.out.println(Utils.sha1(""));
        }
    }

    private static void exitWithMessage(String s) {
        System.out.print(s);
        System.exit(0);
    }

    /** Check the input operands */
    private static void paramCheck(String[] args, int length) {
        if (args.length != length) {
            exitWithMessage(paramError);
        }
    }

    /** Check the existence of the repo */
    private static boolean repoExists() {
        return Repository.GITLET_DIR.exists();
    }

    /** All error messages */
    private static final String noInputError =
            "Please enter a command.";
    private static final String initialError =
            "A Gitlet version-control system already exists in the current directory.";
    private static final String noRepoError =
            "Not in an initialized Gitlet directory.";
    private static final String paramError =
            "Incorrect operands.";
    private static final String noFileError =
            "File does not exist.";
}
