public class Edge {
    private String toLocation;
    private int weight;

    public Edge(String toLocation, int weight) {
        // Store the target node and the cost of travelling to it.
        this.toLocation = toLocation;
        this.weight = weight;
    }

    public String getToLocation() {
        return toLocation;
    }

    public int getWeight() {
        return weight;
    }
}
