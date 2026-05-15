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