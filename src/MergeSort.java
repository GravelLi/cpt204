public class MergeSort implements SortAlgorithm {
    @Override
    public void sort(Location[] locations) {
        if (locations.length <= 1) {
            return;
        }

        mergeSort(locations, 0, locations.length - 1);
    }

    private void mergeSort(Location[] locations, int left, int right) {
        if (left < right) {
            int middle = left + (right - left) / 2;

            mergeSort(locations, left, middle);
            mergeSort(locations, middle + 1, right);

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

        while (i < leftSize) {
            locations[k] = leftArray[i];
            i++;
            k++;
        }

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