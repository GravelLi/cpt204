public class Location {
    private String locationId;
    private int priorityScore;

    public Location(String locationId, int priorityScore) {
        // Each location has an ID and a priority score from the dataset.
        this.locationId = locationId;
        this.priorityScore = priorityScore;
    }

    public String getLocationId() {
        return locationId;
    }

    public int getPriorityScore() {
        return priorityScore;
    }

    @Override
    public String toString() {
        // This format makes the output easier to read in the console.
        return locationId + "(" + priorityScore + ")";
    }
}
