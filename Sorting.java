static void selectionSortByNama(Reservation[] arr) {

    int n = arr.length;

    for (int i = 0; i < n - 1; i++) {

        int minIdx = i;

        for (int j = i + 1; j < n; j++) {

            if (arr[j].namaTamu.compareToIgnoreCase(arr[minIdx].namaTamu) < 0) {
                minIdx = j;
            }
        }
