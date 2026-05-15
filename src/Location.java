public class Location {
    private String locationId;
    private int priorityScore;

    public Location(String locationId, int priorityScore) {
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
        return locationId + "(" + priorityScore + ")";
    }
}