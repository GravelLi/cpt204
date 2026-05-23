public class TaskAResult {
    private String datasetName;
    private double bubbleTime;
    private double quickTime;
    private double mergeTime;
    private Location[] topTenLocations;

    public TaskAResult(String datasetName, double bubbleTime, double quickTime, double mergeTime, Location[] topTenLocations) {
        // Store all Task A results together for easier printing later.
        this.datasetName = datasetName;
        this.bubbleTime = bubbleTime;
        this.quickTime = quickTime;
        this.mergeTime = mergeTime;
        this.topTenLocations = topTenLocations;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public double getBubbleTime() {
        return bubbleTime;
    }

    public double getQuickTime() {
        return quickTime;
    }

    public double getMergeTime() {
        return mergeTime;
    }

    public Location[] getTopTenLocations() {
        return topTenLocations;
    }
}
