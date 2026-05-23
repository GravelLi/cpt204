public class MergeSort implements SortAlgorithm {
    @Override
    public void sort(Location[] locations) {
        // No need to sort an empty array or a one-element array.
        if (locations.length <= 1) {
            return;
        }

        mergeSort(locations, 0, locations.length - 1);
    }

    private void mergeSort(Location[] locations, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            // Sort the left half and right half separately first.
            mergeSort(locations, left, middle);
            mergeSort(locations, middle + 1, right);

            // Merge the two sorted halves back together.
            merge(locations, left, middle, right);
        }
    }

    private void merge(Location[] locations, int left, int middle, int right) {
        int leftSize = middle - left + 1;
        int rightSize = right - middle;

        Location[] leftArray = new Location[leftSize];
        Location[] rightArray = new Location[rightSize];

        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = locations[left + i];
        }

        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = locations[middle + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = left;

        // Pick the smaller ordered item from the two temporary arrays.
        while (i < leftSize && j < rightSize) {
            if (LocationComparator.compare(leftArray[i], rightArray[j]) <= 0) {
                locations[k] = leftArray[i];
                i++;
            } else {
                locations[k] = rightArray[j];
                j++;
            }

            k++;
        }

        // Copy any remaining items from the left side.
        while (i < leftSize) {
            locations[k] = leftArray[i];
            i++;
            k++;
        }

        // Copy any remaining items from the right side.
        while (j < rightSize) {
            locations[k] = rightArray[j];
            j++;
            k++;
        }
    }

    @Override
    public String getName() {
        return "Merge Sort";
    }
}
