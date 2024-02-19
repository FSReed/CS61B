package gitlet;

public class MergeCommit extends Commit{
    private final String commitToMerge;

    MergeCommit(String m, String parentCommit, String targetCommit) {
        super(m, parentCommit);
        this.commitToMerge = targetCommit;
    }

    @Override
    protected String getMetadata() {
        String origin = super.getMetadata();
        return origin + this.commitToMerge;
    }

    public String getMergeCommit() {
        return this.commitToMerge;
    }
}
