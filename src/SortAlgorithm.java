public interface SortAlgorithm {
    // All sorting algorithms should provide the same sort method.
    void sort(Location[] locations);

    // This name is used when printing the algorithm result.
    String getName();
}
