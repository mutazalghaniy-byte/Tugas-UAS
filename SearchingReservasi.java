// =========================================================
// SEARCHING - Linear Search berdasarkan Nama Tamu
// =========================================================
static void linearSearchNama(LinkedList list, String keyword) {

    System.out.println("\n=== LINEAR SEARCH ===");
    System.out.println("Mencari nama : " + keyword);

    Reservation temp = list.head;

    boolean ditemukan = false;
    int iterasi = 0;

    while (temp != null) {

        iterasi++;

        if (!temp.isDeleted &&
            temp.namaTamu.toLowerCase().contains(keyword.toLowerCase())) {

            System.out.println("\nData ditemukan!");
            System.out.println("ID       : " + temp.id);
            System.out.println("Nama     : " + temp.namaTamu);
            System.out.println("Meja     : " + temp.noMeja);
            System.out.println("Tanggal  : " + temp.tanggal);
            System.out.println("Status   : " + temp.status);

            ditemukan = true;
        }

        temp = temp.next;
    }

    if (!ditemukan) {
        System.out.println("\nData tidak ditemukan.");
    }

    System.out.println("Jumlah iterasi : " + iterasi);
}


// =========================================================
// SEARCHING - Binary Search berdasarkan ID
// =========================================================
static void binarySearchID(LinkedList list, int targetID) {

    System.out.println("\n=== BINARY SEARCH ===");
    System.out.println("Mencari ID : " + targetID);

    // Konversi LinkedList ke Array
    Reservation[] arr = list.toArray();

    // Sorting berdasarkan ID
    bubbleSortByID(arr);

    int low = 0;
    int high = arr.length - 1;

    boolean ditemukan = false;
    int iterasi = 0;

    while (low <= high) {

        iterasi++;

        int mid = (low + high) / 2;

        // Jika data ditemukan
        if (arr[mid].id == targetID) {

            System.out.println("\nData ditemukan!");
            System.out.println("ID       : " + arr[mid].id);
            System.out.println("Nama     : " + arr[mid].namaTamu);
            System.out.println("Meja     : " + arr[mid].noMeja);
            System.out.println("Tanggal  : " + arr[mid].tanggal);
            System.out.println("Jam      : " + arr[mid].jam);
            System.out.println("Status   : " + arr[mid].status);

            ditemukan = true;
            break;
        }

        // Cari ke kanan
        else if (arr[mid].id < targetID) {

            low = mid + 1;
        }

        // Cari ke kiri
        else {

            high = mid - 1;
        }
    }

    if (!ditemukan) {
        System.out.println("\nData tidak ditemukan.");
    }

    System.out.println("Jumlah iterasi : " + iterasi);
}


// =========================================================
// SEARCHING - Category Search berdasarkan Kategori
// =========================================================
static void searchByKategori(LinkedList list, String kategori) {

    System.out.println("\n=== SEARCH BY KATEGORI ===");
    System.out.println("Mencari kategori : " + kategori);

    Reservation temp = list.head;

    boolean ditemukan = false;
    int jumlahData = 0;
    int totalTamu = 0;

    while (temp != null) {

        if (!temp.isDeleted &&
            temp.kategori.equalsIgnoreCase(kategori)) {

            System.out.println("\nData ditemukan!");
            System.out.println("ID          : " + temp.id);
            System.out.println("Nama        : " + temp.namaTamu);
            System.out.println("Meja        : " + temp.noMeja);
            System.out.println("Tanggal     : " + temp.tanggal);
            System.out.println("Jam         : " + temp.jam);
            System.out.println("JumlahTamu  : " + temp.jumlahTamu);
            System.out.println("Kategori    : " + temp.kategori);
            System.out.println("Status      : " + temp.status);

            ditemukan = true;

            jumlahData++;
            totalTamu += temp.jumlahTamu;
        }

        temp = temp.next;
    }

    if (!ditemukan) {

        System.out.println("\nData tidak ditemukan.");
        System.out.println("Kategori tersedia : VIP / Reguler / Private");

    } else {

        System.out.println("\nTotal reservasi : " + jumlahData);
        System.out.println("Total tamu      : " + totalTamu);
    }
}