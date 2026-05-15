public class PathResult {
    private String[] path;
    private int totalCost;

    public PathResult(String[] path, int totalCost) {
        this.path = path;
        this.totalCost = totalCost;
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
}