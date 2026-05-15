public class LocationComparator {
    public static int compare(Location a, Location b) {
        if (a.getPriorityScore() != b.getPriorityScore()) {
            return b.getPriorityScore() - a.getPriorityScore();
        }

        return a.getLocationId().compareTo(b.getLocationId());
    }
}