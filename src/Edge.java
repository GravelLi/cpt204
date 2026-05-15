public class Edge {
    private String toLocation;
    private int weight;

    public Edge(String toLocation, int weight) {
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