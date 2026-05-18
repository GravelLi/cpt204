import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Dijkstra {
    private Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public PathResult findShortestPath(String start, String destination) {
        if (start.equals(destination)) {
            String[] path = {start};
            return new PathResult(path, 0);
        }

        HashMap<String, Integer> distances = new HashMap<>();
        HashMap<String, String> previous = new HashMap<>();
        PriorityQueue<NodeDistance> priorityQueue = new PriorityQueue<>();

        ArrayList<String> allLocations = graph.getAllLocations();

        for (String location : allLocations) {
            distances.put(location, Integer.MAX_VALUE);
        }

        distances.put(start, 0);
        priorityQueue.add(new NodeDistance(start, 0));

        while (!priorityQueue.isEmpty()) {
            NodeDistance current = priorityQueue.poll();
            String currentLocation = current.getLocation();
            int currentDistance = current.getDistance();

            if (currentDistance > distances.get(currentLocation)) {
                continue;
            }

            if (currentLocation.equals(destination)) {
                break;
            }

            ArrayList<Edge> edges = graph.getEdges(currentLocation);

            for (Edge edge : edges) {
                String neighbor = edge.getToLocation();
                int newDistance = currentDistance + edge.getWeight();

                if (newDistance < distances.getOrDefault(neighbor, Integer.MAX_VALUE)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, currentLocation);
                    priorityQueue.add(new NodeDistance(neighbor, newDistance));
                }
            }
        }

        if (!distances.containsKey(destination) || distances.get(destination) == Integer.MAX_VALUE) {
            String[] emptyPath = {};
            return new PathResult(emptyPath, Integer.MAX_VALUE);
        }

        String[] path = buildPath(previous, start, destination);
        int totalCost = distances.get(destination);

        return new PathResult(path, totalCost);
    }

    /**
     * Finds the shortest path that visits a sequence of locations in
     * order (start, waypoint_1, ..., destination) by chaining one
     * Dijkstra call per leg. The per-leg sub-results are attached to
     * the returned PathResult so that callers can render them
     * separately while still seeing the concatenated full path.
     */
    public PathResult findPathThroughWaypoints(String[] locations) {
        ArrayList<String> fullPath = new ArrayList<>();
        ArrayList<PathResult> segments = new ArrayList<>();
        int totalCost = 0;

        for (int i = 0; i < locations.length - 1; i++) {
            PathResult segmentResult = findShortestPath(locations[i], locations[i + 1]);

            if (segmentResult.getTotalCost() == Integer.MAX_VALUE) {
                String[] emptyPath = {};
                return new PathResult(emptyPath, Integer.MAX_VALUE);
            }

            segments.add(segmentResult);
            totalCost += segmentResult.getTotalCost();

            String[] segmentPath = segmentResult.getPath();

            for (int j = 0; j < segmentPath.length; j++) {
                // skip the shared join node so it is not duplicated
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

    private static class NodeDistance implements Comparable<NodeDistance> {
        private String location;
        private int distance;

        public NodeDistance(String location, int distance) {
            this.location = location;
            this.distance = distance;
        }

        public String getLocation() {
            return location;
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public int compareTo(NodeDistance other) {
            return this.distance - other.distance;
        }
    }
}