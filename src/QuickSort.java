public class QuickSort implements SortAlgorithm {
    @Override
    public void sort(Location[] locations) {
        // Start quick sort on the whole array.
        quickSort(locations, 0, locations.length - 1);
    }

    private void quickSort(Location[] locations, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(locations, low, high);

            // Recursively sort the items before and after the pivot.
            quickSort(locations, low, pivotIndex - 1);
            quickSort(locations, pivotIndex + 1, high);
        }
    }

    private int partition(Location[] locations, int low, int high) {
        int middle = low + (high - low) / 2;

        // Move the middle item to the end and use it as the pivot.
        Location temp = locations[middle];
        locations[middle] = locations[high];
        locations[high] = temp;

        Location pivot = locations[high];

        int i = low - 1;

        // Put items smaller than or equal to the pivot on the left side.
        for (int j = low; j < high; j++) {
            if (LocationComparator.compare(locations[j], pivot) <= 0) {
                i++;

                Location swapTemp = locations[i];
                locations[i] = locations[j];
                locations[j] = swapTemp;
            }
        }

        // Put the pivot into its final sorted position.
        Location swapTemp = locations[i + 1];
        locations[i + 1] = locations[high];
        locations[high] = swapTemp;

        return i + 1;
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }
}
