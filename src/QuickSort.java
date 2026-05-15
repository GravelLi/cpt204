public class QuickSort implements SortAlgorithm {
    @Override
    public void sort(Location[] locations) {
        quickSort(locations, 0, locations.length - 1);
    }

    private void quickSort(Location[] locations, int low, int high) {
        if (low < high) {
            int pivotIndex = partition(locations, low, high);

            quickSort(locations, low, pivotIndex - 1);
            quickSort(locations, pivotIndex + 1, high);
        }
    }

    private int partition(Location[] locations, int low, int high) {
        int middle = low + (high - low) / 2;

        Location temp = locations[middle];
        locations[middle] = locations[high];
        locations[high] = temp;

        Location pivot = locations[high];

        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (LocationComparator.compare(locations[j], pivot) <= 0) {
                i++;

                Location swapTemp = locations[i];
                locations[i] = locations[j];
                locations[j] = swapTemp;
            }
        }

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