public class LocationComparator {
    public static int compare(Location a, Location b) {
        // Higher priority score should come first.
        if (a.getPriorityScore() != b.getPriorityScore()) {
            return b.getPriorityScore() - a.getPriorityScore();
        }

        // If scores are the same, sort by location ID alphabetically.
        return a.getLocationId().compareTo(b.getLocationId());
    }
}
