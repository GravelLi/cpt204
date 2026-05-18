import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Graph {
    private HashMap<String, ArrayList<Edge>> adjacencyList;

    public Graph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Loads an undirected weighted graph from a CSV file and prints
     * the basic structural statistics (V, E, density, average degree).
     */
    public void loadFromCSV(String filePath) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                String fromLocation = parts[0].trim();
                String toLocation = parts[1].trim();
                int weight = Integer.parseInt(parts[2].trim());

                addUndirectedEdge(fromLocation, toLocation, weight);
            }

            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading graph file: " + filePath);
            e.printStackTrace();
        }

        printGraphStatistics();
    }

    /**
     * Prints |V|, |E|, density and average degree of the loaded graph.
     * Each undirected edge appears twice in the adjacency list, so we
     * divide the total directed edge count by 2.
     */
    private void printGraphStatistics() {
        int v = adjacencyList.size();

        int totalDirectedEdges = 0;
        for (ArrayList<Edge> edges : adjacencyList.values()) {
            totalDirectedEdges += edges.size();
        }
        int e = totalDirectedEdges / 2;

        double density = 0.0;
        double avgDegree = 0.0;
        if (v > 1) {
            density = (2.0 * e) / ((double) v * (v - 1));
            avgDegree = (2.0 * e) / v;
        }

        System.out.println("Graph loaded successfully:");
        System.out.printf("  Nodes (V):      %d%n", v);
        System.out.printf("  Edges (E):      %d%n", e);
        System.out.printf("  Density:        %.4f%n", density);
        System.out.printf("  Avg degree:     %.2f%n", avgDegree);
        System.out.println();
    }

    public void addUndirectedEdge(String fromLocation, String toLocation, int weight) {
        addDirectedEdge(fromLocation, toLocation, weight);
        addDirectedEdge(toLocation, fromLocation, weight);
    }

    private void addDirectedEdge(String fromLocation, String toLocation, int weight) {
        if (!adjacencyList.containsKey(fromLocation)) {
            adjacencyList.put(fromLocation, new ArrayList<>());
        }

        adjacencyList.get(fromLocation).add(new Edge(toLocation, weight));
    }

    public ArrayList<Edge> getEdges(String location) {
        if (!adjacencyList.containsKey(location)) {
            return new ArrayList<>();
        }

        return adjacencyList.get(location);
    }

    public ArrayList<String> getAllLocations() {
        return new ArrayList<>(adjacencyList.keySet());
    }
}