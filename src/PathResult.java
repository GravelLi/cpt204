import java.util.ArrayList;

/**
 * Holds the result of a single shortest-path query: the node sequence
 * and the total weighted cost. When the result is the concatenation of
 * several legs (waypoint queries), the individual segments are also
 * stored so that callers can print or analyse them separately.
 */
public class PathResult {
    private String[] path;
    private int totalCost;
    private ArrayList<PathResult> segments;

    public PathResult(String[] path, int totalCost) {
        this.path = path;
        this.totalCost = totalCost;
        this.segments = null;
    }

    public String[] getPath() {
        return path;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public String getPathString() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < path.length; i++) {
            builder.append(path[i]);

            if (i < path.length - 1) {
                builder.append(" -> ");
            }
        }

        return builder.toString();
    }

    /**
     * Attaches the per-segment results for a multi-leg (waypoint) query.
     * Each entry is itself a self-contained PathResult for one leg.
     */
    public void setSegments(ArrayList<PathResult> segments) {
        this.segments = segments;
    }

    /**
     * Returns the per-segment results, or null if the path is a single leg.
     */
    public ArrayList<PathResult> getSegments() {
        return segments;
    }

    /**
     * True when the result was assembled from two or more segments
     * (i.e. a waypoint query produced this PathResult).
     */
    public boolean hasSegments() {
        return segments != null && segments.size() > 1;
    }
}
