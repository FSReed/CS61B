package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author FSReed
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TD: what if args is empty?
        if (args.length == 0) {
            exitWithMessage(noInputError);
        }
        String firstArg = args[0];
        if (!firstArg.equals("init") && !repoExists()) {
            exitWithMessage(noRepoError);
        }
        switch(firstArg) {
            case "init":
                // TD: handle the `init` command
                if (repoExists()) {
                    exitWithMessage(initialError);
                }
                Repository.initializeRepo();
                break;
            case "add":
                // TD: handle the `add [filename]` command
                paramCheck(args, 2);
                String fileName = args[1];
                boolean success = Repository.addToStagingArea(fileName);
                if (!success) {
                    exitWithMessage(noFileError);
                }
                break;
            // FILL THE REST IN
            case "commit":
                if (args.length < 2) {
                    exitWithMessage(noCommitMessageError);
                }
                String message = args[1];
                boolean commitSuccess = Repository.commit(message);
                if (!commitSuccess) {
                    exitWithMessage(noChangeError);
                }
                break;
            case "rm":
                paramCheck(args, 2);
                String fileToRemove = args[1];
                boolean removed = Repository.remove(fileToRemove);
                if (!removed) {
                    exitWithMessage(removeError);
                }
                break;
            case "log":
                Repository.log();
                break;
            case "global-log":
                Repository.globalLog();
                break;
            case "find":
                paramCheck(args, 2);
                String findMessage = args[1];
                boolean findCommitMessage = Repository.find(findMessage);
                if (!findCommitMessage) {
                    exitWithMessage(noFitMessageError);
                }
                break;
            case "status":
                Repository.status();
                break;
            case "checkout":
                paramCheck(args, 2);
                switch (args.length) {
                    /* checkout [branch name](args[1]) */
                    case 2:
                        int exitCode0 = Repository.checkoutToBranch(args[1]);
                        checkoutErrorCodeProcess(exitCode0);
                        break;
                    /* checkout -- [file name](args[2]) */
                    case 3:
                        int exitCode1 = Repository.checkoutOneFileToHEAD(args[2]);
                        checkoutErrorCodeProcess(exitCode1);
                        break;
                    /* checkout [commit id](args[1]) -- [file name](args[3]) */
                    case 4:
                        int exitCode2 = Repository.checkoutOneFileToCommit(args[1], args[3]);
                        checkoutErrorCodeProcess(exitCode2);
                        break;
                }
                break;
            case "branch":
                paramCheck(args, 2);
                String branchName = args[1];
                boolean newBranch = Repository.addNewBranch(branchName);
                if (!newBranch) {
                    exitWithMessage(branchAlreadyExists);
                }
                break;
            case "rm-branch":
                paramCheck(args, 2);
                String removedBranch = args[1];
                int removeBranchCode = Repository.removeBranch(removedBranch);
                rmbranchErrorCodeProcess(removeBranchCode);
                break;
            case "reset":
                paramCheck(args, 2);
                String resetCommit = args[1];
                int resetResult = Repository.reset(resetCommit);
                resetErrorCodeProcess(resetResult);
                break;
            case "merge":
                break;
            default:
                exitWithMessage("Invalid operation.");
        }
        System.exit(0);
    }

    private static void exitWithMessage(String s) {
        System.out.print(s);
        System.exit(0);
    }

    /** Check the input operands */
    private static void paramCheck(String[] args, int length) {
        if (args.length < length) {
            exitWithMessage(paramError);
        }
    }

    /** Check the existence of the repo */
    private static boolean repoExists() {
        return Repository.GITLET_DIR.exists();
    }

    /** Perform according to the exit code of checkout */
    private static void checkoutErrorCodeProcess(int exitCode) {
        switch(exitCode) {
            case Repository.CHECKOUT_NO_COMMIT:
                exitWithMessage(noCommitError);
            case Repository.CHECKOUT_NO_FILE_IN_COMMIT:
                exitWithMessage(noFileInCommitError);
            case Repository.CHECKOUT_NO_BRANCH_EXISTS:
                exitWithMessage(noBranchError);
            case Repository.CHECKOUT_SAME_BRANCH:
                exitWithMessage(sameBranchError);
            case Repository.CHECKOUT_UNTRACKED_FILE:
                exitWithMessage(untrackedFileError);
            case Repository.CHECKOUT_SUCCESS:
                System.exit(0);
        }
    }

    private static void rmbranchErrorCodeProcess(int exitCode) {
        switch(exitCode) {
            case Repository.RM_SUCCESS:
                System.exit(0);
            case Repository.RM_CURRENT_BRANCH:
                exitWithMessage(removingCurrentBranchError);
            case Repository.RM_NO_SUCH_BRANCH:
                exitWithMessage(noSuchBranchToRemoveError);
        }
    }

    private static void resetErrorCodeProcess(int exitCode) {
        switch (exitCode) {
            case Repository.RESET_SUCCESS:
                System.exit(0);
            case Repository.RESET_NO_COMMIT:
                exitWithMessage(noCommitError);
            case Repository.RESET_UNTRACKED_FILE:
                exitWithMessage(untrackedFileError);
        }
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
    private static final String removeError =
            "No reason to remove the file.";
    private static final String noFitMessageError =
            "Found no commit with that message.";
    private static final String noCommitError =
            "No commit with that id exists.";
    private static final String noCommitMessageError =
            "Please enter a commit message.";
    private static final String noChangeError =
            "No changes added to the commit.";
    private static final String noFileInCommitError =
            "File does not exist in that commit.";
    private static final String noBranchError =
            "No such branch exists.";
    private static final String sameBranchError =
            "No need to checkout the current branch.";
    private static final String untrackedFileError =
            "There is an untracked file in the way; delete it, or add and commit it first.";
    private static final String branchAlreadyExists =
            "A branch with that name already exists.";
    private static final String noSuchBranchToRemoveError =
            "A branch with that name does not exist.";
    private static final String removingCurrentBranchError =
            "Cannot remove the current branch.";
}
