public class TaskAProcessor {
    private static final int REPEAT_TIMES = 3;

    public TaskAResult processDataset(String datasetName, String filePath) {
        Location[] originalLocations = CSVReader.readLocations(filePath);

        SortAlgorithm bubbleSort = new BubbleSort();
        SortAlgorithm quickSort = new QuickSort();
        SortAlgorithm mergeSort = new MergeSort();

        long bubbleAverageTime = measureAverageTime(bubbleSort, originalLocations);
        long quickAverageTime = measureAverageTime(quickSort, originalLocations);
        long mergeAverageTime = measureAverageTime(mergeSort, originalLocations);

        Location[] sortedLocations = copyLocations(originalLocations);
        mergeSort.sort(sortedLocations);

        Location[] topTenLocations = getTopTen(sortedLocations);

        return new TaskAResult(datasetName, bubbleAverageTime, quickAverageTime, mergeAverageTime, topTenLocations);
    }

    private long measureAverageTime(SortAlgorithm algorithm, Location[] originalLocations) {
        long totalTime = 0;

        for (int i = 0; i < REPEAT_TIMES; i++) {
            Location[] copiedLocations = copyLocations(originalLocations);

            long startTime = System.nanoTime();
            algorithm.sort(copiedLocations);
            long endTime = System.nanoTime();

            totalTime += endTime - startTime;
        }

        return totalTime / REPEAT_TIMES;
    }

    private Location[] copyLocations(Location[] originalLocations) {
        Location[] copiedLocations = new Location[originalLocations.length];

        for (int i = 0; i < originalLocations.length; i++) {
            copiedLocations[i] = originalLocations[i];
        }

        return copiedLocations;
    }

    private Location[] getTopTen(Location[] sortedLocations) {
        int size = Math.min(10, sortedLocations.length);
        Location[] topTen = new Location[size];

        for (int i = 0; i < size; i++) {
            topTen[i] = sortedLocations[i];
        }

        return topTen;
    }
}