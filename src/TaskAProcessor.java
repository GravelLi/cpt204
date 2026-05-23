public class TaskAProcessor {
    private static final int WARM_UP_RUNS = 50;
    private static final int REPEAT_TIMES = 1000;

    public TaskAResult processDataset(String datasetName, String filePath) {
        // Read all candidate locations from the given CSV file.
        Location[] originalLocations = CSVReader.readLocations(filePath);

        SortAlgorithm bubbleSort = new BubbleSort();
        SortAlgorithm quickSort = new QuickSort();
        SortAlgorithm mergeSort = new MergeSort();

        // Measure each sorting algorithm on the same original dataset.
        double bubbleAverageTime = measureAverageTime(bubbleSort, originalLocations);
        double quickAverageTime = measureAverageTime(quickSort, originalLocations);
        double mergeAverageTime = measureAverageTime(mergeSort, originalLocations);

        Location[] sortedLocations = copyLocations(originalLocations);
        mergeSort.sort(sortedLocations);

        // The sorted array is used to select the final top 10 locations.
        Location[] topTenLocations = getTopTen(sortedLocations);

        return new TaskAResult(datasetName, bubbleAverageTime, quickAverageTime, mergeAverageTime, topTenLocations);
    }

    private double measureAverageTime(SortAlgorithm algorithm, Location[] originalLocations) {
        // Warm-up runs help reduce unstable timing at the beginning.
        for (int i = 0; i < WARM_UP_RUNS; i++) {
            Location[] copiedLocations = copyLocations(originalLocations);
            algorithm.sort(copiedLocations);
        }

        long totalTime = 0;

        // Repeat the test many times to get a more reliable average.
        for (int i = 0; i < REPEAT_TIMES; i++) {
            Location[] copiedLocations = copyLocations(originalLocations);

            long startTime = System.nanoTime();
            algorithm.sort(copiedLocations);
            long endTime = System.nanoTime();

            totalTime += endTime - startTime;
        }

        return (double) totalTime / REPEAT_TIMES / 1_000_000.0;
    }

    private Location[] copyLocations(Location[] originalLocations) {
        Location[] copiedLocations = new Location[originalLocations.length];

        // Make a new array so each algorithm sorts an unchanged copy.
        for (int i = 0; i < originalLocations.length; i++) {
            copiedLocations[i] = originalLocations[i];
        }

        return copiedLocations;
    }

    private Location[] getTopTen(Location[] sortedLocations) {
        int size = Math.min(10, sortedLocations.length);
        Location[] topTen = new Location[size];

        // Take the first 10 items after sorting.
        for (int i = 0; i < size; i++) {
            topTen[i] = sortedLocations[i];
        }

        return topTen;
    }
}
