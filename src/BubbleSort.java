public class BubbleSort implements SortAlgorithm {
    @Override
    public void sort(Location[] locations) {
        int n = locations.length;

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < n - 1 - i; j++) {
                if (LocationComparator.compare(locations[j], locations[j + 1]) > 0) {
                    Location temp = locations[j];
                    locations[j] = locations[j + 1];
                    locations[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    @Override
    public String getName() {
        return "Bubble Sort";
    }
}