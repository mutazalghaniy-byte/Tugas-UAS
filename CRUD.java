import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

public class CRUD {

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
    static final String FILE_WAITING_LIST  = "waitinglist.txt";

    static ArrayList<String> logAktivitas  = new ArrayList<>();
    static ArrayList<String> customerLoyal = new ArrayList<>();
    static ArrayList<String> waitingList   = new ArrayList<>();

    // ==========================================================
    //  FUNGSI UTAMA (MAIN METHOD) AGAR BISA DI-RUN
    // ==========================================================
    public static void main(String[] args) {
        int pilihan = 0;
        do {
            System.out.println("\n=== SISTEM RESERVASI RESTORAN ===");
            System.out.println("1. Tambah Reservasi (Create)");
            System.out.println("2. Lihat Semua Reservasi (Read)");
            System.out.println("3. Edit Reservasi (Update)");
            System.out.println("4. Update Status Reservasi");
            System.out.println("5. Hapus Reservasi (Delete)");
            System.out.println("6. Keluar");
            System.out.print("Pilih menu (1-6): ");
            
            try {
                pilihan = Integer.parseInt(sc.nextLine().trim());
                switch (pilihan) {
                    case 1: tambahReservasi(); break;
                    case 2: tampilkanSemua(); break;
                    case 3: editReservasi(); break;
                    case 4: updateStatusReservasi(); break;
                    case 5: hapusReservasi(); break;
                    case 6: System.out.println("Terima kasih telah menggunakan sistem!"); break;
                    default: System.out.println("[ERROR] Pilihan menu tidak tersedia.");
                }
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] Masukkan input berupa angka!");
            }
        } while (pilihan != 6);
    }

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

    // ==========================================================
    //  HELPER METHODS
    // ==========================================================
    static int cariIndexById(int cariId) {
        for (int i = 0; i < jumlahData; i++) {
            if (id[i] == cariId) return i;
        }
        return -1;
    }

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

        int idxLama       = cariIndexNama(nama);
        int hitungCounter = 1;
        if (idxLama != -1) {
            hitungCounter = counter[idxLama] + 1;
        }

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

        logAktivitas.add("TAMBAH | ID:" + id[jumlahData-1] + " | Nama:" + nama + " | Meja:" + meja + " | Tgl:" + tgl);

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
            logAktivitas.add("HAPUS | ID:" + idCari + " | Nama:" + namaCustomer[idx]);
            simpanKeFile();
        } else {
            System.out.println("[BATAL] Penghapusan dibatalkan.");
        }
    }
}