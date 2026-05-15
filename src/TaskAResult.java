public class TaskAResult {
    private String datasetName;
    private long bubbleTime;
    private long quickTime;
    private long mergeTime;
    private Location[] topTenLocations;

    public TaskAResult(String datasetName, long bubbleTime, long quickTime, long mergeTime, Location[] topTenLocations) {
        this.datasetName = datasetName;
        this.bubbleTime = bubbleTime;
        this.quickTime = quickTime;
        this.mergeTime = mergeTime;
        this.topTenLocations = topTenLocations;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public long getBubbleTime() {
        return bubbleTime;
    }

    public long getQuickTime() {
        return quickTime;
    }

    public long getMergeTime() {
        return mergeTime;
    }

    public Location[] getTopTenLocations() {
        return topTenLocations;
    }
}