static void menuSorting() {
        System.out.println("\n===== MENU SORTING =====");
        System.out.println("[1] Bubble Sort    - Urutkan berdasarkan ID (Ascending)");
        System.out.println("[2] Selection Sort - Urutkan berdasarkan Nama Customer (A-Z)");
        System.out.println("[3] Insertion Sort - Urutkan berdasarkan Jumlah Tamu (Terbanyak)");
        System.out.println("[0] Kembali");
        System.out.print("Pilih metode sorting: ");
        String p = sc.nextLine().trim();

        Reservasi[] temp = salinDataAktif();
        if (temp.length == 0 && !p.equals("0")) {
            System.out.println("[INFO] Belum ada data aktif untuk diurutkan."); return;
        }

        switch (p) {
            case "1":
                bubbleSortById(temp);
                System.out.println("\n>>> HASIL BUBBLE SORT: ID Ascending <<<");
                System.out.println("Kompleksitas Waktu: O(n^2) worst, O(n) best | Ruang: O(1)");
                tampilkanHasilSort(temp);
                break;
            case "2":
                selectionSortByNama(temp);
                System.out.println("\n>>> HASIL SELECTION SORT: Nama A-Z <<<");
                System.out.println("Kompleksitas Waktu: O(n^2) | Kompleksitas Ruang: O(1)");
                tampilkanHasilSort(temp);
                break;
            case "3":
                insertionSortByJumlahTamu(temp);
                System.out.println("\n>>> HASIL INSERTION SORT: Jumlah Tamu Terbanyak ke Tersedikit <<<");
                System.out.println("Kompleksitas Waktu: O(n^2) worst, O(n) best | Ruang: O(1)");
                tampilkanHasilSort(temp);
                break;
            case "0":
                System.out.println("[INFO] Kembali ke menu utama."); break;
            default:
                System.out.println("[ERROR] Pilihan tidak valid!");
        }
    }

    // SORTING 1: Bubble Sort berdasarkan ID (Ascending)
    static void bubbleSortById(Reservasi[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (arr[j].id > arr[j + 1].id) {
                    Reservasi tmp = arr[j]; arr[j] = arr[j + 1]; arr[j + 1] = tmp;
                    swapped = true;
                }
            }
            if (!swapped) break; // optimasi: berhenti jika sudah terurut
        }
    }

    // SORTING 2: Selection Sort berdasarkan Nama Customer (A-Z)
    static void selectionSortByNama(Reservasi[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (arr[j].namaCustomer.compareToIgnoreCase(arr[minIdx].namaCustomer) < 0)
                    minIdx = j;
            }
            Reservasi tmp = arr[minIdx]; arr[minIdx] = arr[i]; arr[i] = tmp;
        }
    }

    // SORTING 3: Insertion Sort berdasarkan Jumlah Tamu (Descending)
    static void insertionSortByJumlahTamu(Reservasi[] arr) {
        int n = arr.length;
        for (int i = 1; i < n; i++) {
            Reservasi kunci = arr[i];
            int j = i - 1;
            
            while (j >= 0 && arr[j].jumlahTamu < kunci.jumlahTamu) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = kunci;
        }
    }
