// ==========================================================
// MENU SEARCHING
// ==========================================================
static void menuSearching() {

    System.out.println("\n===== MENU SEARCHING =====");
    System.out.println("[1] Linear Search  - Cari berdasarkan Nama Customer");
    System.out.println("[2] Binary Search  - Cari berdasarkan ID");
    System.out.println("[3] Cari berdasarkan Kategori");
    System.out.println("[0] Kembali");

    System.out.print("Pilih menu: ");
    String pilihan = sc.nextLine().trim();

    switch (pilihan) {

        case "1":
            linearSearchByNama();
            break;

        case "2":
            binarySearchById();
            break;

        case "3":
            cariByKategori();
            break;

        case "0":
            System.out.println("[INFO] Kembali ke menu utama.");
            break;

        default:
            System.out.println("[ERROR] Pilihan tidak valid!");
    }
}


// ==========================================================
// SEARCHING 1 : Linear Search berdasarkan Nama
// Kompleksitas Waktu : O(n)
// Kompleksitas Ruang : O(1)
// ==========================================================
static void linearSearchByNama() {

    System.out.println("\n>>> LINEAR SEARCH : Cari Berdasarkan Nama <<<");

    System.out.print("Masukkan nama customer : ");
    String keyword = sc.nextLine().trim();

    if (keyword.isEmpty()) {
        System.out.println("[ERROR] Nama tidak boleh kosong!");
        return;
    }

    boolean ditemukan = false;

    cetakHeader();

    for (int i = 0; i < jumlahData; i++) {

        if (data[i] == null) {
            continue;
        }

        if (data[i].status.equals("DIHAPUS")) {
            continue;
        }

        if (data[i].namaCustomer
                .toLowerCase()
                .contains(keyword.toLowerCase())) {

            cetakBaris(data[i]);
            ditemukan = true;
        }
    }

    cetakGaris();

    if (!ditemukan) {
        System.out.println("[INFO] Data tidak ditemukan.");
    }
}


// ==========================================================
// SEARCHING 2 : Binary Search berdasarkan ID
// Kompleksitas Waktu : O(log n)
// Kompleksitas Ruang : O(1)
// ==========================================================
static void binarySearchById() {

    System.out.println("\n>>> BINARY SEARCH : Cari Berdasarkan ID <<<");

    Reservasi[] temp = salinDataAktif();

    if (temp.length == 0) {
        System.out.println("[INFO] Belum ada data aktif.");
        return;
    }

    // Bubble Sort berdasarkan ID
    for (int i = 0; i < temp.length - 1; i++) {

        for (int j = 0; j < temp.length - 1 - i; j++) {

            if (temp[j].id > temp[j + 1].id) {

                Reservasi swap = temp[j];
                temp[j] = temp[j + 1];
                temp[j + 1] = swap;
            }
        }
    }

    System.out.print("Masukkan ID yang dicari : ");

    int idCari;

    try {

        idCari = Integer.parseInt(sc.nextLine().trim());

    } catch (NumberFormatException e) {

        System.out.println("[ERROR] ID harus berupa angka!");
        return;
    }

    int kiri = 0;
    int kanan = temp.length - 1;

    boolean ditemukan = false;

    while (kiri <= kanan) {

        int tengah = (kiri + kanan) / 2;

        if (temp[tengah].id == idCari) {

            cetakHeader();
            cetakBaris(temp[tengah]);
            cetakGaris();

            ditemukan = true;
            break;

        } else if (temp[tengah].id < idCari) {

            kiri = tengah + 1;

        } else {

            kanan = tengah - 1;
        }
    }

    if (!ditemukan) {
        System.out.println("[INFO] Data dengan ID " + idCari + " tidak ditemukan.");
    }
}
static Reservasi[] salinDataAktif() {

    int count = 0;

    for (int i = 0; i < jumlahData; i++) {

        if (data[i] != null &&
            !data[i].status.equals("DIHAPUS")) {

            count++;
        }
    }

    Reservasi[] temp = new Reservasi[count];

    int idx = 0;

    for (int i = 0; i < jumlahData; i++) {

        if (data[i] != null &&
            !data[i].status.equals("DIHAPUS")) {

            temp[idx++] = data[i];
        }
    }

    return temp;
}


// ==========================================================
// SEARCHING 3 : Cari berdasarkan Kategori
// Kompleksitas Waktu : O(n)
// Kompleksitas Ruang : O(1)
// ==========================================================
static void cariByKategori() {

    System.out.println("\n>>> SEARCH BY KATEGORI <<<");

    System.out.print("Masukkan kategori (VIP/REGULAR/OUTDOOR) : ");

    String kategori = sc.nextLine().trim().toUpperCase();

    if (!kategori.equals("VIP") &&
        !kategori.equals("REGULAR") &&
        !kategori.equals("OUTDOOR")) {

        System.out.println("[ERROR] Kategori tidak valid!");
        return;
    }

    boolean ditemukan = false;
    int jumlah = 0;

    cetakHeader();

    for (int i = 0; i < jumlahData; i++) {

        if (data[i] == null) {
            continue;
        }

        if (data[i].status.equals("DIHAPUS")) {
            continue;
        }

        if (data[i].kategoriMeja.equals(kategori)) {

            cetakBaris(data[i]);

            ditemukan = true;
            jumlah++;
        }
    }

    cetakGaris();

    if (!ditemukan) {

        System.out.println("[INFO] Data kategori tidak ditemukan.");

    } else {

        System.out.println("Total data kategori " + kategori + " : " + jumlah);
    }
}


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
