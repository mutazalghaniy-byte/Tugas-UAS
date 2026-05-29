import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class SistemRestaurant {

    // ---------- Array Paralel sebagai penyimpanan data ----------
    static final int MAX_DATA    = 100;
    static int[]     id          = new int[MAX_DATA];
    static String[]  namaCustomer= new String[MAX_DATA];
    static String[]  nomorMeja   = new String[MAX_DATA];
    static String[]  tanggal     = new String[MAX_DATA];
    static String[]  jam         = new String[MAX_DATA];
    static int[]     jumlahTamu  = new int[MAX_DATA];
    static String[]  kategoriMeja= new String[MAX_DATA];
    static String[]  status      = new String[MAX_DATA];
    static int[]     counter     = new int[MAX_DATA];

    static int jumlahData = 0;
    static int nextId     = 1;

    static Scanner sc = new Scanner(System.in);
    static final String FILE_NAME          = "reservasi.txt";
    static final String FILE_WAITING_LIST  = "waitinglist.txt"; // [TAMBAHAN]

    // ==========================================================
    // ArrayList untuk Log Aktivitas & Customer Loyal
    // ==========================================================
    static ArrayList<String> logAktivitas    = new ArrayList<>();
    static ArrayList<String> customerLoyal   = new ArrayList<>();

    // ==========================================================
    //  [TAMBAHAN FITUR BARU] ArrayList untuk Waiting List
    //  Format setiap elemen: "namaCustomer|tanggal|jam|jumlahTamu|kategori"
    // ==========================================================
    static ArrayList<String> waitingList = new ArrayList<>();

    // ==========================================================
    //  UTILITY: Cetak Garis & Header Tabel
    // ==========================================================
    static void cetakGaris() {
        System.out.println("=".repeat(90));
    }

    static void cetakHeader() {
        cetakGaris();
        System.out.printf("%-5s %-20s %-6s %-12s %-6s %-6s %-10s %-10s %-5s%n",
                "ID", "Nama Customer", "Meja", "Tanggal", "Jam",
                "Tamu", "Kategori", "Status", "Ctr");
        cetakGaris();
    }

    static void cetakBaris(int idx) {
        System.out.printf("%-5d %-20s %-6s %-12s %-6s %-6d %-10s %-10s %-5d%n",
                id[idx], namaCustomer[idx], nomorMeja[idx], tanggal[idx],
                jam[idx], jumlahTamu[idx], kategoriMeja[idx], status[idx], counter[idx]);
    }

    // ==========================================================
    //  1. CREATE – Tambah Reservasi Baru
    // ==========================================================
    static void tambahReservasi() {
        System.out.println("\n>>> TAMBAH RESERVASI BARU <<<");

        if (jumlahData >= MAX_DATA) {
            System.out.println("[ERROR] Kapasitas data penuh!");
            return;
        }

        System.out.print("Nama Customer         : ");
        String nama = sc.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("[ERROR] Nama tidak boleh kosong!");
            return;
        }

        System.out.print("Nomor Meja (cth: M01) : ");
        String meja = sc.nextLine().trim().toUpperCase();
        if (meja.isEmpty()) {
            System.out.println("[ERROR] Nomor meja tidak boleh kosong!");
            return;
        }

        System.out.print("Tanggal (DD-MM-YYYY)  : ");
        String tgl = sc.nextLine().trim();

        System.out.print("Jam (HH:MM)           : ");
        String waktu = sc.nextLine().trim();

        System.out.print("Jumlah Tamu           : ");
        int tamu = 0;
        try {
            tamu = Integer.parseInt(sc.nextLine().trim());
            if (tamu <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Jumlah tamu harus angka positif!");
            return;
        }

        System.out.print("Kategori Meja (VIP / Regular / Outdoor) : ");
        String kat = sc.nextLine().trim().toUpperCase();
        if (!kat.equals("VIP") && !kat.equals("REGULAR") && !kat.equals("OUTDOOR")) {
            System.out.println("[ERROR] Kategori tidak valid! Pilih: VIP, Regular, atau Outdoor.");
            return;
        }

        // Cek counter: apakah customer sudah pernah reservasi
        int idxLama      = cariIndexNama(nama);
        int hitungCounter = 1;
        if (idxLama != -1) {
            hitungCounter = counter[idxLama] + 1;
        }

        // Simpan ke semua array pada index jumlahData
        id[jumlahData]            = nextId++;
        namaCustomer[jumlahData]  = nama;
        nomorMeja[jumlahData]     = meja;
        tanggal[jumlahData]       = tgl;
        jam[jumlahData]           = waktu;
        jumlahTamu[jumlahData]    = tamu;
        kategoriMeja[jumlahData]  = kat;
        status[jumlahData]        = "AKTIF";
        counter[jumlahData]       = hitungCounter;
        jumlahData++;

        System.out.println("[OK] Reservasi berhasil ditambahkan dengan ID: " + id[jumlahData - 1]);

        // [TAMBAHAN] Catat ke log aktivitas
        logAktivitas.add("TAMBAH | ID:" + id[jumlahData-1] + " | Nama:" + nama + " | Meja:" + meja + " | Tgl:" + tgl);

        // [TAMBAHAN] Cek apakah customer layak masuk daftar loyal (counter >= 2)
        if (hitungCounter >= 2 && !customerLoyal.contains(nama)) {
            customerLoyal.add(nama);
            System.out.println("[INFO] " + nama + " telah masuk daftar Customer Loyal!");
        }

        simpanKeFile();
    }

    // ==========================================================
    //  2. READ – Tampilkan Semua Data
    // ==========================================================
    static void tampilkanSemua() {
        System.out.println("\n>>> DAFTAR SEMUA RESERVASI <<<");
        cetakHeader();

        int tampil = 0;
        for (int i = 0; i < jumlahData; i++) {
            if (!status[i].equals("DIHAPUS")) {
                cetakBaris(i);
                tampil++;
            }
        }

        if (tampil == 0) {
            System.out.println("  (Belum ada data reservasi aktif.)");
        }
        cetakGaris();
    }

    // ==========================================================
    //  3. UPDATE – Edit Data Berdasarkan ID
    // ==========================================================
    static void editReservasi() {
        System.out.println("\n>>> EDIT RESERVASI <<<");
        System.out.print("Masukkan ID yang ingin diedit: ");

        int idCari;
        try {
            idCari = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] ID harus berupa angka!");
            return;
        }

        int idx = cariIndexById(idCari);

        if (idx == -1) {
            System.out.println("[ERROR] ID " + idCari + " tidak ditemukan!");
            return;
        }
        if (status[idx].equals("DIHAPUS")) {
            System.out.println("[ERROR] Data dengan ID " + idCari + " sudah dihapus!");
            return;
        }

        System.out.println("Data ditemukan:");
        cetakHeader();
        cetakBaris(idx);
        cetakGaris();
        System.out.println("(Kosongkan input untuk mempertahankan nilai lama)\n");

        System.out.print("Nama Customer   [" + namaCustomer[idx] + "] : ");
        String input = sc.nextLine().trim();
        if (!input.isEmpty()) namaCustomer[idx] = input;

        System.out.print("Nomor Meja      [" + nomorMeja[idx] + "] : ");
        input = sc.nextLine().trim().toUpperCase();
        if (!input.isEmpty()) nomorMeja[idx] = input;

        System.out.print("Tanggal         [" + tanggal[idx] + "] : ");
        input = sc.nextLine().trim();
        if (!input.isEmpty()) tanggal[idx] = input;

        System.out.print("Jam             [" + jam[idx] + "] : ");
        input = sc.nextLine().trim();
        if (!input.isEmpty()) jam[idx] = input;

        System.out.print("Jumlah Tamu     [" + jumlahTamu[idx] + "] : ");
        input = sc.nextLine().trim();
        if (!input.isEmpty()) {
            try {
                int t = Integer.parseInt(input);
                if (t > 0) jumlahTamu[idx] = t;
                else System.out.println("[PERINGATAN] Jumlah tamu tidak valid, nilai lama dipertahankan.");
            } catch (NumberFormatException e) {
                System.out.println("[PERINGATAN] Input bukan angka, nilai lama dipertahankan.");
            }
        }

        System.out.print("Kategori Meja   [" + kategoriMeja[idx] + "] (VIP/Regular/Outdoor) : ");
        input = sc.nextLine().trim().toUpperCase();
        if (!input.isEmpty()) {
            if (input.equals("VIP") || input.equals("REGULAR") || input.equals("OUTDOOR")) {
                kategoriMeja[idx] = input;
            } else {
                System.out.println("[PERINGATAN] Kategori tidak valid, nilai lama dipertahankan.");
            }
        }

        System.out.print("Status          [" + status[idx] + "] (AKTIF/SELESAI) : ");
        input = sc.nextLine().trim().toUpperCase();
        if (!input.isEmpty()) {
            if (input.equals("AKTIF") || input.equals("SELESAI")) {
                status[idx] = input;
            } else {
                System.out.println("[PERINGATAN] Status tidak valid, nilai lama dipertahankan.");
            }
        }

        System.out.println("[OK] Data reservasi ID " + idCari + " berhasil diperbarui.");

        // [TAMBAHAN] Catat ke log aktivitas
        logAktivitas.add("EDIT | ID:" + idCari + " | Nama:" + namaCustomer[idx]);

        simpanKeFile();
    }

    // ==========================================================
    //  UPDATE STATUS RESERVASI
    // ==========================================================
    static void updateStatusReservasi() {
        System.out.println("\n>>> UPDATE STATUS RESERVASI <<<");
        System.out.print("Masukkan ID Reservasi : ");

        int idCari;
        try {
            idCari = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] ID harus angka!");
            return;
        }

        int idx = cariIndexById(idCari);

        if (idx == -1) {
            System.out.println("[ERROR] ID tidak ditemukan!");
            return;
        }
        if (status[idx].equals("DIHAPUS")) {
            System.out.println("[ERROR] Reservasi sudah dihapus!");
            return;
        }

        System.out.println("Status saat ini : " + status[idx]);
        System.out.print("Masukkan status baru (AKTIF/SELESAI): ");
        String statusBaru = sc.nextLine().trim().toUpperCase();

        if (!statusBaru.equals("AKTIF") && !statusBaru.equals("SELESAI")) {
            System.out.println("[ERROR] Status tidak valid!");
            return;
        }

        String statusLama = status[idx];
        status[idx]       = statusBaru;

        System.out.println("\n=== STATUS BERHASIL DIUPDATE ===");
        System.out.println("ID Reservasi : " + id[idx]);
        System.out.println("Nama Customer: " + namaCustomer[idx]);
        System.out.println("Status Lama  : " + statusLama);
        System.out.println("Status Baru  : " + statusBaru);
        System.out.println("Counter      : " + counter[idx]);

        // [TAMBAHAN] Catat ke log aktivitas
        logAktivitas.add("UPDATE STATUS | ID:" + idCari + " | " + statusLama + " -> " + statusBaru);

        simpanKeFile();
    }

    // ==========================================================
    //  4. DELETE – Hapus Data (Soft Delete)
    // ==========================================================
    static void hapusReservasi() {
        System.out.println("\n>>> HAPUS RESERVASI (Soft Delete) <<<");
        System.out.print("Masukkan ID yang ingin dihapus: ");

        int idCari;
        try {
            idCari = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] ID harus berupa angka!");
            return;
        }

        int idx = cariIndexById(idCari);

        if (idx == -1) {
            System.out.println("[ERROR] ID " + idCari + " tidak ditemukan!");
            return;
        }
        if (status[idx].equals("DIHAPUS")) {
            System.out.println("[INFO] Data ini sudah berstatus DIHAPUS sebelumnya.");
            return;
        }

        System.out.println("Data yang akan dihapus:");
        cetakHeader();
        cetakBaris(idx);
        cetakGaris();
        System.out.print("Apakah Anda yakin ingin menghapus? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();

        if (konfirmasi.equals("y")) {
            status[idx] = "DIHAPUS";
            System.out.println("[OK] Reservasi ID " + idCari + " berhasil dihapus (soft delete).");

            // [TAMBAHAN] Catat ke log aktivitas
            logAktivitas.add("HAPUS | ID:" + idCari + " | Nama:" + namaCustomer[idx]);

            simpanKeFile();
        } else {
            System.out.println("[BATAL] Penghapusan dibatalkan.");
        }
    }

    // ==========================================================
    //  MENU SEARCHING
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
            case "1": linearSearchByNama(); break;
            case "2": binarySearchById();   break;
            case "3": cariByKategori();     break;
            case "0": System.out.println("[INFO] Kembali ke menu utama."); break;
            default:  System.out.println("[ERROR] Pilihan tidak valid!");
        }
    }

    // ==========================================================
    //  SEARCHING 1: Linear Search berdasarkan Nama – O(n)
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
            if (status[i].equals("DIHAPUS")) continue;
            if (namaCustomer[i].toLowerCase().contains(keyword.toLowerCase())) {
                cetakBaris(i);
                ditemukan = true;
            }
        }

        cetakGaris();
        if (!ditemukan) System.out.println("[INFO] Data tidak ditemukan.");
    }

    // ==========================================================
    //  SEARCHING 2: Binary Search berdasarkan ID – O(log n)
    // ==========================================================
    static void binarySearchById() {
        System.out.println("\n>>> BINARY SEARCH : Cari Berdasarkan ID <<<");

        // Salin index data aktif ke array sementara
        int[] idxAktif = new int[jumlahData];
        int count = 0;
        for (int i = 0; i < jumlahData; i++) {
            if (!status[i].equals("DIHAPUS")) {
                idxAktif[count++] = i;
            }
        }

        if (count == 0) {
            System.out.println("[INFO] Belum ada data aktif.");
            return;
        }

        // Bubble Sort index berdasarkan nilai id[]
        for (int i = 0; i < count - 1; i++) {
            for (int j = 0; j < count - 1 - i; j++) {
                if (id[idxAktif[j]] > id[idxAktif[j + 1]]) {
                    int swap      = idxAktif[j];
                    idxAktif[j]   = idxAktif[j + 1];
                    idxAktif[j + 1] = swap;
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

        int kiri = 0, kanan = count - 1;
        boolean ditemukan = false;

        while (kiri <= kanan) {
            int tengah = (kiri + kanan) / 2;
            int idx    = idxAktif[tengah];

            if (id[idx] == idCari) {
                cetakHeader();
                cetakBaris(idx);
                cetakGaris();
                ditemukan = true;
                break;
            } else if (id[idx] < idCari) {
                kiri = tengah + 1;
            } else {
                kanan = tengah - 1;
            }
        }

        if (!ditemukan) System.out.println("[INFO] Data dengan ID " + idCari + " tidak ditemukan.");
    }

    // ==========================================================
    //  SEARCHING 3: Cari berdasarkan Kategori – O(n)
    // ==========================================================
    static void cariByKategori() {
        System.out.println("\n>>> SEARCH BY KATEGORI <<<");
        System.out.print("Masukkan kategori (VIP/REGULAR/OUTDOOR) : ");
        String kat = sc.nextLine().trim().toUpperCase();

        if (!kat.equals("VIP") && !kat.equals("REGULAR") && !kat.equals("OUTDOOR")) {
            System.out.println("[ERROR] Kategori tidak valid!");
            return;
        }

        boolean ditemukan = false;
        int jumlah = 0;
        cetakHeader();

        for (int i = 0; i < jumlahData; i++) {
            if (status[i].equals("DIHAPUS")) continue;
            if (kategoriMeja[i].equals(kat)) {
                cetakBaris(i);
                ditemukan = true;
                jumlah++;
            }
        }

        cetakGaris();
        if (!ditemukan) {
            System.out.println("[INFO] Data kategori tidak ditemukan.");
        } else {
            System.out.println("Total data kategori " + kat + " : " + jumlah);
        }
    }

    // ==========================================================
    //  MENU SORTING
    // ==========================================================
    static void menuSorting() {
        System.out.println("\n===== MENU SORTING =====");
        System.out.println("[1] Bubble Sort    - Urutkan berdasarkan ID (Ascending)");
        System.out.println("[2] Selection Sort - Urutkan berdasarkan Nama Customer (A-Z)");
        System.out.println("[3] Insertion Sort - Urutkan berdasarkan Jumlah Tamu (Terbanyak)");
        System.out.println("[0] Kembali");
        System.out.print("Pilih metode sorting: ");
        String p = sc.nextLine().trim();

        // Salin index data aktif untuk diurutkan
        int[] idxAktif = new int[jumlahData];
        int count = 0;
        for (int i = 0; i < jumlahData; i++) {
            if (!status[i].equals("DIHAPUS")) {
                idxAktif[count++] = i;
            }
        }

        if (count == 0 && !p.equals("0")) {
            System.out.println("[INFO] Belum ada data aktif untuk diurutkan.");
            return;
        }

        switch (p) {
            case "1":
                bubbleSortById(idxAktif, count);
                System.out.println("\n>>> HASIL BUBBLE SORT: ID Ascending <<<");
                System.out.println("Kompleksitas Waktu: O(n^2) worst, O(n) best | Ruang: O(1)");
                tampilkanHasilSort(idxAktif, count);
                break;
            case "2":
                selectionSortByNama(idxAktif, count);
                System.out.println("\n>>> HASIL SELECTION SORT: Nama A-Z <<<");
                System.out.println("Kompleksitas Waktu: O(n^2) | Kompleksitas Ruang: O(1)");
                tampilkanHasilSort(idxAktif, count);
                break;
            case "3":
                insertionSortByJumlahTamu(idxAktif, count);
                System.out.println("\n>>> HASIL INSERTION SORT: Jumlah Tamu Terbanyak ke Tersedikit <<<");
                System.out.println("Kompleksitas Waktu: O(n^2) worst, O(n) best | Ruang: O(1)");
                tampilkanHasilSort(idxAktif, count);
                break;
            case "0":
                System.out.println("[INFO] Kembali ke menu utama.");
                break;
            default:
                System.out.println("[ERROR] Pilihan tidak valid!");
        }
    }

    // SORTING 1: Bubble Sort berdasarkan ID (Ascending)
    static void bubbleSortById(int[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (id[arr[j]] > id[arr[j + 1]]) {
                    int tmp  = arr[j];
                    arr[j]   = arr[j + 1];
                    arr[j + 1] = tmp;
                    swapped  = true;
                }
            }
            if (!swapped) break;
        }
    }

    // SORTING 2: Selection Sort berdasarkan Nama Customer (A-Z)
    static void selectionSortByNama(int[] arr, int n) {
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                if (namaCustomer[arr[j]].compareToIgnoreCase(namaCustomer[arr[minIdx]]) < 0) {
                    minIdx = j;
                }
            }
            int tmp    = arr[minIdx];
            arr[minIdx] = arr[i];
            arr[i]     = tmp;
        }
    }

    // SORTING 3: Insertion Sort berdasarkan Jumlah Tamu (Descending)
    static void insertionSortByJumlahTamu(int[] arr, int n) {
        for (int i = 1; i < n; i++) {
            int kunci = arr[i];
            int j     = i - 1;
            while (j >= 0 && jumlahTamu[arr[j]] < jumlahTamu[kunci]) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = kunci;
        }
    }

    static void tampilkanHasilSort(int[] arr, int n) {
        cetakHeader();
        for (int i = 0; i < n; i++) {
            cetakBaris(arr[i]);
        }
        cetakGaris();
    }

    // ==========================================================
    //  STATISTIK
    // ==========================================================
    static void hitungStatistik() {
        int aktif = 0, selesai = 0, dihapus = 0;
        int vip   = 0, regular = 0, outdoor = 0;

        for (int i = 0; i < jumlahData; i++) {
            switch (status[i]) {
                case "AKTIF":   aktif++;   break;
                case "SELESAI": selesai++; break;
                case "DIHAPUS": dihapus++; break;
            }
            if (!status[i].equals("DIHAPUS")) {
                switch (kategoriMeja[i]) {
                    case "VIP":     vip++;     break;
                    case "REGULAR": regular++; break;
                    case "OUTDOOR": outdoor++; break;
                }
            }
        }

        System.out.println("\n--- STATISTIK ---");
        System.out.println("Total data    : " + jumlahData);
        System.out.println("Aktif         : " + aktif);
        System.out.println("Selesai       : " + selesai);
        System.out.println("Dihapus       : " + dihapus);
        System.out.println("Meja VIP      : " + vip);
        System.out.println("Meja Regular  : " + regular);
        System.out.println("Meja Outdoor  : " + outdoor);
    }

    // ==========================================================
    //  [TAMBAHAN] LOG AKTIVITAS – menggunakan ArrayList
    // ==========================================================
    static void tampilkanLogAktivitas() {
        System.out.println("\n>>> LOG AKTIVITAS <<<");
        if (logAktivitas.isEmpty()) {
            System.out.println("[INFO] Belum ada aktivitas yang tercatat.");
            cetakGaris();
            return;
        }
        cetakGaris();
        for (int i = 0; i < logAktivitas.size(); i++) {
            System.out.println((i + 1) + ". " + logAktivitas.get(i));
        }
        cetakGaris();
        System.out.println("Total log aktivitas : " + logAktivitas.size());
    }

    // ==========================================================
    //  CUSTOMER LOYAL – menggunakan ArrayList
    // ==========================================================
    static void tampilkanCustomerLoyal() {
        System.out.println("\n>>> DAFTAR CUSTOMER LOYAL <<<");
        System.out.println("(Customer yang telah melakukan reservasi lebih dari 1 kali)");
        if (customerLoyal.isEmpty()) {
            System.out.println("[INFO] Belum ada customer loyal.");
            cetakGaris();
            return;
        }
        cetakGaris();
        for (int i = 0; i < customerLoyal.size(); i++) {
            System.out.println((i + 1) + ". " + customerLoyal.get(i));
        }
        cetakGaris();
        System.out.println("Total customer loyal : " + customerLoyal.size());
    }

    // ==========================================================
    //  MENU ARRAYLIST
    // ==========================================================
    static void menuArrayList() {
        System.out.println("\n===== Riwayat Aktivitas dan Customer loyal =====");
        System.out.println("[1] Tampilkan Log Aktivitas");
        System.out.println("[2] Tampilkan Daftar Customer Loyal");
        System.out.println("[3] Hapus Semua Log Aktivitas");
        System.out.println("[0] Kembali");
        System.out.print("Pilih menu: ");
        String pilihan = sc.nextLine().trim();

        switch (pilihan) {
            case "1":
                tampilkanLogAktivitas();
                break;
            case "2":
                tampilkanCustomerLoyal();
                break;
            case "3":
                logAktivitas.clear();
                System.out.println("[OK] Semua log aktivitas berhasil dihapus.");
                break;
            case "0":
                System.out.println("[INFO] Kembali ke menu utama.");
                break;
            default:
                System.out.println("[ERROR] Pilihan tidak valid!");
        }
    }

    // ==========================================================
    //  HELPER: Cari index berdasarkan ID
    // ==========================================================
    static int cariIndexById(int cariId) {
        for (int i = 0; i < jumlahData; i++) {
            if (id[i] == cariId) return i;
        }
        return -1;
    }

    // ==========================================================
    //  HELPER: Cari index terakhir berdasarkan nama
    // ==========================================================
    static int cariIndexNama(String nama) {
        int idx = -1;
        for (int i = 0; i < jumlahData; i++) {
            if (namaCustomer[i].equalsIgnoreCase(nama)) {
                idx = i;
            }
        }
        return idx;
    }

    // ==========================================================
    //  SAVE DATA KE FILE
    // ==========================================================
    static void simpanKeFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < jumlahData; i++) {
                bw.write(id[i] + ";" + namaCustomer[i] + ";" + nomorMeja[i] + ";" +
                         tanggal[i] + ";" + jam[i] + ";" + jumlahTamu[i] + ";" +
                         kategoriMeja[i] + ";" + status[i] + ";" + counter[i]);
                bw.newLine();
            }
            bw.close();
            System.out.println("[OK] Data berhasil disimpan ke file.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan file!");
        }
    }

    // ==========================================================
    //  LOAD DATA DARI FILE
    // ==========================================================
    static void loadDariFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("[INFO] File data belum tersedia.");
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            jumlahData = 0;
            while ((line = br.readLine()) != null) {
                String[] bagian    = line.split(";");
                id[jumlahData]           = Integer.parseInt(bagian[0]);
                namaCustomer[jumlahData] = bagian[1];
                nomorMeja[jumlahData]    = bagian[2];
                tanggal[jumlahData]      = bagian[3];
                jam[jumlahData]          = bagian[4];
                jumlahTamu[jumlahData]   = Integer.parseInt(bagian[5]);
                kategoriMeja[jumlahData] = bagian[6];
                status[jumlahData]       = bagian[7];
                counter[jumlahData]      = Integer.parseInt(bagian[8]);

                // [TAMBAHAN] Sinkronisasi customer loyal dari file saat load
                if (counter[jumlahData] >= 2 && !customerLoyal.contains(namaCustomer[jumlahData])) {
                    customerLoyal.add(namaCustomer[jumlahData]);
                }

                if (id[jumlahData] >= nextId) nextId = id[jumlahData] + 1;
                jumlahData++;
            }
            br.close();
            System.out.println("[OK] Data berhasil dimuat dari file.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal membaca file!");
        } catch (Exception e) {
            System.out.println("[ERROR] Format file tidak valid!");
        }
    }

    // ==========================================================
    //  DATA DEMO
    // ==========================================================
    static void isiDataDemo() {
        id[jumlahData] = nextId++;  namaCustomer[jumlahData] = "Budi Santoso";
        nomorMeja[jumlahData] = "M01";  tanggal[jumlahData] = "10-06-2026";
        jam[jumlahData] = "12:00";  jumlahTamu[jumlahData] = 2;
        kategoriMeja[jumlahData] = "REGULAR";  status[jumlahData] = "AKTIF";
        counter[jumlahData] = 1;  jumlahData++;

        id[jumlahData] = nextId++;  namaCustomer[jumlahData] = "Siti Rahayu";
        nomorMeja[jumlahData] = "M05";  tanggal[jumlahData] = "10-06-2026";
        jam[jumlahData] = "13:00";  jumlahTamu[jumlahData] = 4;
        kategoriMeja[jumlahData] = "VIP";  status[jumlahData] = "AKTIF";
        counter[jumlahData] = 1;  jumlahData++;

        id[jumlahData] = nextId++;  namaCustomer[jumlahData] = "Ahmad Fauzi";
        nomorMeja[jumlahData] = "M03";  tanggal[jumlahData] = "11-06-2026";
        jam[jumlahData] = "19:00";  jumlahTamu[jumlahData] = 3;
        kategoriMeja[jumlahData] = "OUTDOOR";  status[jumlahData] = "AKTIF";
        counter[jumlahData] = 1;  jumlahData++;

        id[jumlahData] = nextId++;  namaCustomer[jumlahData] = "Dewi Lestari";
        nomorMeja[jumlahData] = "M02";  tanggal[jumlahData] = "11-06-2026";
        jam[jumlahData] = "20:00";  jumlahTamu[jumlahData] = 6;
        kategoriMeja[jumlahData] = "VIP";  status[jumlahData] = "AKTIF";
        counter[jumlahData] = 1;  jumlahData++;

        id[jumlahData] = nextId++;  namaCustomer[jumlahData] = "Rizky Pratama";
        nomorMeja[jumlahData] = "M04";  tanggal[jumlahData] = "12-06-2026";
        jam[jumlahData] = "18:30";  jumlahTamu[jumlahData] = 2;
        kategoriMeja[jumlahData] = "REGULAR";  status[jumlahData] = "AKTIF";
        counter[jumlahData] = 1;  jumlahData++;

        System.out.println("[INFO] 5 data demo berhasil dimuat.");
    }

    // ==========================================================
    //  [FITUR BARU] RESET DATA RESERVASI
    //  Menghapus semua data reservasi dari memori dan file,
    //  serta mereset ID counter ke awal.
    // ==========================================================
    static void resetDataReservasi() {
        System.out.println("\n>>> RESET DATA RESERVASI <<<");
        System.out.println("[PERINGATAN] Fitur ini akan menghapus SEMUA data reservasi secara permanen!");
        System.out.println("             Data yang dihapus tidak dapat dikembalikan.");
        cetakGaris();
        System.out.print("Ketik 'RESET' untuk konfirmasi pertama: ");
        String konfirmasi1 = sc.nextLine().trim();

        if (!konfirmasi1.equals("RESET")) {
            System.out.println("[BATAL] Reset dibatalkan. Konfirmasi tidak sesuai.");
            return;
        }

        System.out.print("Ketik 'YA' untuk konfirmasi kedua (terakhir): ");
        String konfirmasi2 = sc.nextLine().trim().toUpperCase();

        if (!konfirmasi2.equals("YA")) {
            System.out.println("[BATAL] Reset dibatalkan.");
            return;
        }

        // Reset semua array paralel
        for (int i = 0; i < jumlahData; i++) {
            id[i]             = 0;
            namaCustomer[i]   = null;
            nomorMeja[i]      = null;
            tanggal[i]        = null;
            jam[i]            = null;
            jumlahTamu[i]     = 0;
            kategoriMeja[i]   = null;
            status[i]         = null;
            counter[i]        = 0;
        }

        // Reset counter dan ID
        jumlahData = 0;
        nextId     = 1;

        // Reset ArrayList customer loyal (counter kembali ke 0)
        customerLoyal.clear();

        // Catat ke log sebelum dihapus total, lalu kosongkan file
        logAktivitas.add("RESET | Semua data reservasi dihapus dan ID direset ke 1");

        // Hapus isi file reservasi
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME));
            bw.close();
            System.out.println("[OK] File reservasi.txt berhasil dikosongkan.");
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal mengosongkan file reservasi!");
        }

        System.out.println("[OK] Semua data reservasi berhasil direset.");
        System.out.println("[INFO] ID akan dimulai dari 1 kembali.");
        System.out.println("[INFO] Daftar customer loyal juga telah dikosongkan.");
    }

    // ==========================================================
    //  [FITUR BARU] WAITING LIST – menggunakan ArrayList
    // ==========================================================

    // Tambah pelanggan ke waiting list
    static void tambahWaitingList() {
        System.out.println("\n>>> TAMBAH KE WAITING LIST <<<");
        System.out.println("[INFO] Waiting list digunakan saat meja tidak tersedia.");

        System.out.print("Nama Customer         : ");
        String nama = sc.nextLine().trim();
        if (nama.isEmpty()) {
            System.out.println("[ERROR] Nama tidak boleh kosong!");
            return;
        }

        System.out.print("Tanggal (DD-MM-YYYY)  : ");
        String tgl = sc.nextLine().trim();
        if (tgl.isEmpty()) {
            System.out.println("[ERROR] Tanggal tidak boleh kosong!");
            return;
        }

        System.out.print("Jam (HH:MM)           : ");
        String waktu = sc.nextLine().trim();
        if (waktu.isEmpty()) {
            System.out.println("[ERROR] Jam tidak boleh kosong!");
            return;
        }

        System.out.print("Jumlah Tamu           : ");
        int tamu = 0;
        try {
            tamu = Integer.parseInt(sc.nextLine().trim());
            if (tamu <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Jumlah tamu harus angka positif!");
            return;
        }

        System.out.print("Kategori Meja (VIP / Regular / Outdoor) : ");
        String kat = sc.nextLine().trim().toUpperCase();
        if (!kat.equals("VIP") && !kat.equals("REGULAR") && !kat.equals("OUTDOOR")) {
            System.out.println("[ERROR] Kategori tidak valid! Pilih: VIP, Regular, atau Outdoor.");
            return;
        }

        String entri = nama + "|" + tgl + "|" + waktu + "|" + tamu + "|" + kat;
        waitingList.add(entri);

        int nomorAntrian = waitingList.size();
        System.out.println("[OK] " + nama + " berhasil masuk Waiting List.");
        System.out.println("[INFO] Nomor antrian Anda: " + nomorAntrian);

        logAktivitas.add("WAITING LIST TAMBAH | No:" + nomorAntrian + " | Nama:" + nama + " | Tgl:" + tgl);

        simpanWaitingListKeFile();
    }

    // Tampilkan semua isi waiting list
    static void tampilkanWaitingList() {
        System.out.println("\n>>> DAFTAR WAITING LIST <<<");

        if (waitingList.isEmpty()) {
            System.out.println("[INFO] Waiting list kosong.");
            cetakGaris();
            return;
        }

        cetakGaris();
        System.out.printf("%-5s %-20s %-12s %-6s %-6s %-10s%n",
                "No.", "Nama Customer", "Tanggal", "Jam", "Tamu", "Kategori");
        cetakGaris();

        for (int i = 0; i < waitingList.size(); i++) {
            String[] bagian = waitingList.get(i).split("\\|");
            System.out.printf("%-5d %-20s %-12s %-6s %-6s %-10s%n",
                    (i + 1), bagian[0], bagian[1], bagian[2], bagian[3], bagian[4]);
        }

        cetakGaris();
        System.out.println("Total dalam waiting list: " + waitingList.size() + " orang");
    }

    // Promosikan pelanggan pertama dari waiting list menjadi reservasi aktif
    static void promosiWaitingList() {
        System.out.println("\n>>> PROMOSI WAITING LIST KE RESERVASI AKTIF <<<");

        if (waitingList.isEmpty()) {
            System.out.println("[INFO] Waiting list kosong. Tidak ada yang bisa dipromosikan.");
            return;
        }

        if (jumlahData >= MAX_DATA) {
            System.out.println("[ERROR] Kapasitas reservasi penuh! Tidak bisa mempromosikan.");
            return;
        }

        // Ambil pelanggan pertama (FIFO – First In First Out)
        String entri    = waitingList.get(0);
        String[] bagian = entri.split("\\|");

        String namaWL  = bagian[0];
        String tglWL   = bagian[1];
        String waktuWL = bagian[2];
        int    tamuWL  = Integer.parseInt(bagian[3]);
        String katWL   = bagian[4];

        System.out.println("Pelanggan berikut akan dipromosikan ke reservasi aktif:");
        cetakGaris();
        System.out.printf("Nama     : %s%n", namaWL);
        System.out.printf("Tanggal  : %s%n", tglWL);
        System.out.printf("Jam      : %s%n", waktuWL);
        System.out.printf("Tamu     : %d%n", tamuWL);
        System.out.printf("Kategori : %s%n", katWL);
        cetakGaris();

        System.out.print("Masukkan Nomor Meja yang tersedia (cth: M01) : ");
        String meja = sc.nextLine().trim().toUpperCase();
        if (meja.isEmpty()) {
            System.out.println("[ERROR] Nomor meja tidak boleh kosong!");
            return;
        }

        System.out.print("Konfirmasi promosi? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();

        if (!konfirmasi.equals("y")) {
            System.out.println("[BATAL] Promosi dibatalkan.");
            return;
        }

        // Cek apakah nama sudah pernah reservasi sebelumnya
        int idxLama       = cariIndexNama(namaWL);
        int hitungCounter = 1;
        if (idxLama != -1) {
            hitungCounter = counter[idxLama] + 1;
        }

        // Masukkan ke array reservasi
        id[jumlahData]           = nextId++;
        namaCustomer[jumlahData] = namaWL;
        nomorMeja[jumlahData]    = meja;
        tanggal[jumlahData]      = tglWL;
        jam[jumlahData]          = waktuWL;
        jumlahTamu[jumlahData]   = tamuWL;
        kategoriMeja[jumlahData] = katWL;
        status[jumlahData]       = "AKTIF";
        counter[jumlahData]      = hitungCounter;
        jumlahData++;

        // Hapus dari waiting list (posisi 0, FIFO)
        waitingList.remove(0);

        System.out.println("[OK] " + namaWL + " berhasil dipromosikan ke Reservasi Aktif dengan ID: " + id[jumlahData - 1]);
        System.out.println("[INFO] Sisa antrian waiting list: " + waitingList.size() + " orang");

        // Cek customer loyal
        if (hitungCounter >= 2 && !customerLoyal.contains(namaWL)) {
            customerLoyal.add(namaWL);
            System.out.println("[INFO] " + namaWL + " telah masuk daftar Customer Loyal!");
        }

        logAktivitas.add("WAITING LIST PROMOSI | ID:" + id[jumlahData-1] + " | Nama:" + namaWL + " | Meja:" + meja);

        simpanKeFile();
        simpanWaitingListKeFile();
    }

    // Hapus pelanggan dari waiting list berdasarkan nomor urut
    static void hapusWaitingList() {
        System.out.println("\n>>> HAPUS DARI WAITING LIST <<<");

        if (waitingList.isEmpty()) {
            System.out.println("[INFO] Waiting list kosong.");
            return;
        }

        tampilkanWaitingList();

        System.out.print("Masukkan nomor urut yang ingin dihapus: ");
        int nomorUrut;
        try {
            nomorUrut = Integer.parseInt(sc.nextLine().trim());
            if (nomorUrut < 1 || nomorUrut > waitingList.size()) {
                System.out.println("[ERROR] Nomor urut tidak valid!");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("[ERROR] Input harus berupa angka!");
            return;
        }

        String[] bagianHapus = waitingList.get(nomorUrut - 1).split("\\|");
        System.out.print("Hapus [" + bagianHapus[0] + "] dari waiting list? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();

        if (konfirmasi.equals("y")) {
            String namaHapus = bagianHapus[0];
            waitingList.remove(nomorUrut - 1);
            System.out.println("[OK] " + namaHapus + " berhasil dihapus dari waiting list.");
            logAktivitas.add("WAITING LIST HAPUS | Nama:" + namaHapus);
            simpanWaitingListKeFile();
        } else {
            System.out.println("[BATAL] Penghapusan dibatalkan.");
        }
    }

    // Simpan waiting list ke file
    static void simpanWaitingListKeFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_WAITING_LIST));
            for (int i = 0; i < waitingList.size(); i++) {
                bw.write(waitingList.get(i));
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal menyimpan waiting list ke file!");
        }
    }

    // Load waiting list dari file saat program dimulai
    static void loadWaitingListDariFile() {
        File file = new File(FILE_WAITING_LIST);
        if (!file.exists()) return;
        try {
            BufferedReader br = new BufferedReader(new FileReader(FILE_WAITING_LIST));
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    waitingList.add(line.trim());
                }
            }
            br.close();
            if (!waitingList.isEmpty()) {
                System.out.println("[OK] " + waitingList.size() + " data waiting list dimuat dari file.");
            }
        } catch (IOException e) {
            System.out.println("[ERROR] Gagal membaca file waiting list!");
        }
    }

    // Menu Waiting List
    static void menuWaitingList() {
        System.out.println("\n===== MENU WAITING LIST =====");
        System.out.println("[1] Tambah ke Waiting List");
        System.out.println("[2] Tampilkan Waiting List");
        System.out.println("[3] Promosikan ke Reservasi Aktif");
        System.out.println("[4] Hapus dari Waiting List");
        System.out.println("[0] Kembali");
        System.out.print("Pilih menu: ");
        String pilihan = sc.nextLine().trim();

        switch (pilihan) {
            case "1": tambahWaitingList();   break;
            case "2": tampilkanWaitingList(); break;
            case "3": promosiWaitingList();  break;
            case "4": hapusWaitingList();    break;
            case "0": System.out.println("[INFO] Kembali ke menu utama."); break;
            default:  System.out.println("[ERROR] Pilihan tidak valid!");
        }
    }

    // ==========================================================
    //  MAIN
    // ==========================================================
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("   RESTAURANT RESERVATION SYSTEM");
        System.out.println("=".repeat(50));

        loadDariFile();
        loadWaitingListDariFile(); // [FITUR BARU] Load waiting list
        if (jumlahData == 0) {
            isiDataDemo();
            simpanKeFile();
        }

        boolean jalan = true;
        while (jalan) {
            System.out.println("\n===== MENU UTAMA =====");
            System.out.println("[1] Tambah Reservasi Baru");
            System.out.println("[2] Tampilkan Semua Reservasi");
            System.out.println("[3] Edit Reservasi");
            System.out.println("[4] Hapus Reservasi");
            System.out.println("[5] Update Status Reservasi");
            System.out.println("[6] Menu Searching");
            System.out.println("[7] Menu Sorting");
            System.out.println("[8] Statistik");
            System.out.println("[9] Simpan Data ke File");
            System.out.println("[10] Menu Riwayat dan Customer loyal");
            System.out.println("[11] Menu Waiting List");                     
            System.out.println("[12] Reset Data Reservasi");                  
            System.out.println("[0] Keluar");
            System.out.print("Pilih menu: ");

            String pilihan = sc.nextLine().trim();

            switch (pilihan) {
                case "1": tambahReservasi();       break;
                case "2": tampilkanSemua();        break;
                case "3": editReservasi();         break;
                case "4": hapusReservasi();        break;
                case "5": updateStatusReservasi(); break;
                case "6": menuSearching();         break;
                case "7": menuSorting();           break;
                case "8": hitungStatistik();       break;
                case "9": simpanKeFile();          break;
                case "10": menuArrayList();        break; 
                case "11": menuWaitingList();      break; 
                case "12": resetDataReservasi();   break; 
                case "0":
                    System.out.println("Terima kasih! Program selesai.");
                    jalan = false;
                    break;
                default:
                    System.out.println("[ERROR] Pilihan tidak valid!");
            }
        }
        sc.close();
    }
}