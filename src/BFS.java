import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Unweighted shortest-path baseline using breadth-first search.
 *
 * BFS minimises the number of edges in the path, treating every edge
 * as having unit weight. The total cost reported in the returned
 * PathResult is the *real* weighted cost of the BFS-selected path,
 * computed from the original edge weights, so it can be compared
 * directly with Dijkstra's output.
 *
 * This class is used as an empirical baseline in Task B section 2.5.4
 * to demonstrate that BFS produces sub-optimal paths on weighted graphs.
 */
public class BFS {
    private Graph graph;

    public BFS(Graph graph) {
        this.graph = graph;
    }

    /**
     * Returns the path with the fewest edges from start to dest using BFS.
     * The PathResult's totalCost is the sum of the original edge weights
     * along that BFS path (not the edge count), so it can be compared
     * with Dijkstra's weighted optimum.
     */
    public PathResult findShortestPathByEdgeCount(String start, String dest) {
        if (start.equals(dest)) {
            String[] selfPath = {start};
            return new PathResult(selfPath, 0);
        }

        HashMap<String, String> previous = new HashMap<>();
        HashSet<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();

        queue.add(start);
        visited.add(start);

        boolean found = false;

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(dest)) {
                found = true;
                break;
            }

            ArrayList<Edge> edges = graph.getEdges(current);

            for (Edge edge : edges) {
                String neighbor = edge.getToLocation();

                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    previous.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        if (!found) {
            String[] emptyPath = {};
            return new PathResult(emptyPath, Integer.MAX_VALUE);
        }

        String[] path = buildPath(previous, start, dest);
        int weightedCost = computeWeightedCost(path);

        return new PathResult(path, weightedCost);
    }

    /**
     * BFS variant of waypoint pathfinding: chains BFS calls per leg and
     * concatenates them, mirroring Dijkstra.findPathThroughWaypoints so
     * the two algorithms can be compared on equal footing.
     */
    public PathResult findPathThroughWaypoints(String[] locations) {
        ArrayList<String> fullPath = new ArrayList<>();
        ArrayList<PathResult> segments = new ArrayList<>();
        int totalCost = 0;

        for (int i = 0; i < locations.length - 1; i++) {
            PathResult segmentResult = findShortestPathByEdgeCount(locations[i], locations[i + 1]);

            if (segmentResult.getTotalCost() == Integer.MAX_VALUE) {
                String[] emptyPath = {};
                return new PathResult(emptyPath, Integer.MAX_VALUE);
            }

            segments.add(segmentResult);
            totalCost += segmentResult.getTotalCost();

            String[] segmentPath = segmentResult.getPath();

            for (int j = 0; j < segmentPath.length; j++) {
                if (i > 0 && j == 0) {
                    continue;
                }

                fullPath.add(segmentPath[j]);
            }
        }

        String[] resultPath = new String[fullPath.size()];

        for (int i = 0; i < fullPath.size(); i++) {
            resultPath[i] = fullPath.get(i);
        }

        PathResult combined = new PathResult(resultPath, totalCost);
        combined.setSegments(segments);
        return combined;
    }

    private String[] buildPath(HashMap<String, String> previous, String start, String destination) {
        ArrayList<String> path = new ArrayList<>();
        String current = destination;

        while (current != null) {
            path.add(current);

            if (current.equals(start)) {
                break;
            }

            current = previous.get(current);
        }

        Collections.reverse(path);

        String[] result = new String[path.size()];

        for (int i = 0; i < path.size(); i++) {
            result[i] = path.get(i);
        }

        return result;
    }

    /**
     * Re-derives the real weighted cost of a path from the underlying
     * graph by looking up each consecutive edge in the adjacency list.
     */
    private int computeWeightedCost(String[] path) {
        int cost = 0;

        for (int i = 0; i < path.length - 1; i++) {
            ArrayList<Edge> edges = graph.getEdges(path[i]);

            for (Edge edge : edges) {
                if (edge.getToLocation().equals(path[i + 1])) {
                    cost += edge.getWeight();
                    break;
                }
            }
        }

        return cost;
    }
}
