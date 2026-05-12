import java.util.Scanner;

class Reservasi {
    int    id;
    String namaCustomer;
    String nomorMeja;      // contoh: "M01", "M02", dst.
    String tanggal;        // format: DD-MM-YYYY
    String jam;            // format: HH:MM
    int    jumlahTamu;
    String kategoriMeja;   // "VIP", "Regular", "Outdoor"
    String status;         // "AKTIF" | "SELESAI" | "DIHAPUS"  (soft-delete)
    int    counter;        // berapa kali customer ini pernah reservasi


    Reservasi(int id, String namaCustomer, String nomorMeja,
              String tanggal, String jam, int jumlahTamu,
              String kategoriMeja) {
        this.id             = id;
        this.namaCustomer   = namaCustomer;
        this.nomorMeja      = nomorMeja;
        this.tanggal        = tanggal;
        this.jam            = jam;
        this.jumlahTamu     = jumlahTamu;
        this.kategoriMeja   = kategoriMeja;
        this.status         = "AKTIF";
        this.counter        = 1;
    }
}

// ============================================================
//  KELAS UTAMA: CRUD + entry point
// ============================================================
public class RestaurantCRUD {

    // ---------- Array manual sebagai penyimpanan data ----------
    static final int MAX_DATA   = 100;
    static Reservasi[] data     = new Reservasi[MAX_DATA];
    static int jumlahData       = 0;          // total slot terpakai (termasuk yg dihapus)
    static int nextId           = 1;          // auto-increment ID

    static Scanner sc = new Scanner(System.in);

    // ==========================================================
    //  UTILITY: cetak garis pemisah & header tabel
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

    static void cetakBaris(Reservasi r) {
        System.out.printf("%-5d %-20s %-6s %-12s %-6s %-6d %-10s %-10s %-5d%n",
                r.id, r.namaCustomer, r.nomorMeja, r.tanggal,
                r.jam, r.jumlahTamu, r.kategoriMeja, r.status, r.counter);
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

        System.out.print("Nama Customer   : ");
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
        String tanggal = sc.nextLine().trim();

        System.out.print("Jam (HH:MM)           : ");
        String jam = sc.nextLine().trim();

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
        String kategori = sc.nextLine().trim().toUpperCase();
        if (!kategori.equals("VIP") && !kategori.equals("REGULAR") && !kategori.equals("OUTDOOR")) {
            System.out.println("[ERROR] Kategori tidak valid! Pilih: VIP, Regular, atau Outdoor.");
            return;
        }

        // Cek apakah customer sudah pernah reservasi => update counter
        int idxLama = cariIndexNama(nama);

        Reservasi baru = new Reservasi(nextId++, nama, meja, tanggal, jam, tamu, kategori);

        if (idxLama != -1) {
            // counter merupakan akumulasi dari seluruh reservasi atas nama yang sama
            baru.counter = data[idxLama].counter + 1;
        }

        data[jumlahData++] = baru;

        System.out.println("[OK] Reservasi berhasil ditambahkan dengan ID: " + baru.id);
    }

    // ==========================================================
    //  2. READ – Tampilkan Semua Data
    // ==========================================================
    static void tampilkanSemua() {
        System.out.println("\n>>> DAFTAR SEMUA RESERVASI <<<");
        cetakHeader();

        int tampil = 0;
        for (int i = 0; i < jumlahData; i++) {
            if (!data[i].status.equals("DIHAPUS")) {
                cetakBaris(data[i]);
                tampil++;
            }
        }

        if (tampil == 0) {
            System.out.println("  (Belum ada data reservasi aktif.)");
        }
        cetakGaris();

        // Statistik sederhana
        hitungStatistik();
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
        if (data[idx].status.equals("DIHAPUS")) {
            System.out.println("[ERROR] Data dengan ID " + idCari + " sudah dihapus!");
            return;
        }

        Reservasi r = data[idx];
        System.out.println("Data ditemukan:");
        cetakHeader();
        cetakBaris(r);
        cetakGaris();
        System.out.println("(Kosongkan input untuk mempertahankan nilai lama)\n");

        System.out.print("Nama Customer   [" + r.namaCustomer + "] : ");
        String input = sc.nextLine().trim();
        if (!input.isEmpty()) r.namaCustomer = input;

        System.out.print("Nomor Meja      [" + r.nomorMeja + "] : ");
        input = sc.nextLine().trim().toUpperCase();
        if (!input.isEmpty()) r.nomorMeja = input;

        System.out.print("Tanggal         [" + r.tanggal + "] : ");
        input = sc.nextLine().trim();
        if (!input.isEmpty()) r.tanggal = input;

        System.out.print("Jam             [" + r.jam + "] : ");
        input = sc.nextLine().trim();
        if (!input.isEmpty()) r.jam = input;

        System.out.print("Jumlah Tamu     [" + r.jumlahTamu + "] : ");
        input = sc.nextLine().trim();
        if (!input.isEmpty()) {
            try {
                int tamu = Integer.parseInt(input);
                if (tamu > 0) r.jumlahTamu = tamu;
                else System.out.println("[PERINGATAN] Jumlah tamu tidak valid, nilai lama dipertahankan.");
            } catch (NumberFormatException e) {
                System.out.println("[PERINGATAN] Input bukan angka, nilai lama dipertahankan.");
            }
        }

        System.out.print("Kategori Meja   [" + r.kategoriMeja + "] (VIP/Regular/Outdoor) : ");
        input = sc.nextLine().trim().toUpperCase();
        if (!input.isEmpty()) {
            if (input.equals("VIP") || input.equals("REGULAR") || input.equals("OUTDOOR")) {
                r.kategoriMeja = input;
            } else {
                System.out.println("[PERINGATAN] Kategori tidak valid, nilai lama dipertahankan.");
            }
        }

        System.out.print("Status          [" + r.status + "] (AKTIF/SELESAI) : ");
        input = sc.nextLine().trim().toUpperCase();
        if (!input.isEmpty()) {
            if (input.equals("AKTIF") || input.equals("SELESAI")) {
                r.status = input;
            } else {
                System.out.println("[PERINGATAN] Status tidak valid, nilai lama dipertahankan.");
            }
        }

        System.out.println("[OK] Data reservasi ID " + idCari + " berhasil diperbarui.");
    }

    // ==========================================================
    //  4. DELETE – Hapus Data (Soft Delete via Status)
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
        if (data[idx].status.equals("DIHAPUS")) {
            System.out.println("[INFO] Data ini sudah berstatus DIHAPUS sebelumnya.");
            return;
        }

        // Konfirmasi sebelum hapus
        System.out.println("Data yang akan dihapus:");
        cetakHeader();
        cetakBaris(data[idx]);
        cetakGaris();
        System.out.print("Apakah Anda yakin ingin menghapus? (y/n): ");
        String konfirmasi = sc.nextLine().trim().toLowerCase();

        if (konfirmasi.equals("y")) {
            data[idx].status = "DIHAPUS";   // Soft delete: status diubah, data tetap ada
            System.out.println("[OK] Reservasi ID " + idCari + " berhasil dihapus (soft delete).");
        } else {
            System.out.println("[BATAL] Penghapusan dibatalkan.");
        }
    }

    // ==========================================================
    //  HELPER: Statistik Data
    // ==========================================================
    static void hitungStatistik() {
        int total    = 0;
        int aktif    = 0;
        int selesai  = 0;
        int dihapus  = 0;
        int vip      = 0;
        int regular  = 0;
        int outdoor  = 0;

        for (int i = 0; i < jumlahData; i++) {
            Reservasi r = data[i];
            total++;
            switch (r.status) {
                case "AKTIF":    aktif++;   break;
                case "SELESAI":  selesai++; break;
                case "DIHAPUS":  dihapus++; break;
            }
            if (!r.status.equals("DIHAPUS")) {
                switch (r.kategoriMeja) {
                    case "VIP":     vip++;     break;
                    case "REGULAR": regular++; break;
                    case "OUTDOOR": outdoor++; break;
                }
            }
        }

        System.out.println("\n--- STATISTIK ---");
        System.out.println("Total data    : " + total);
        System.out.println("Aktif         : " + aktif);
        System.out.println("Selesai       : " + selesai);
        System.out.println("Dihapus       : " + dihapus);
        System.out.println("Meja VIP      : " + vip);
        System.out.println("Meja Regular  : " + regular);
        System.out.println("Meja Outdoor  : " + outdoor);
    }

    // ==========================================================
    //  HELPER: Cari index array berdasarkan ID (untuk CRUD)
    //  Linear search – O(n) – dipakai secara internal CRUD
    // ==========================================================
    static int cariIndexById(int id) {
        for (int i = 0; i < jumlahData; i++) {
            if (data[i].id == id) return i;
        }
        return -1;
    }

    // ==========================================================
    //  HELPER: Cari index terakhir berdasarkan nama (untuk counter)
    // ==========================================================
    static int cariIndexNama(String nama) {
        int idx = -1;
        for (int i = 0; i < jumlahData; i++) {
            if (data[i].namaCustomer.equalsIgnoreCase(nama)) {
                idx = i;
            }
        }
        return idx;
    }

    // ==========================================================
    //  HELPER: Isi data dummy untuk demo/pengujian
    // ==========================================================
    static void isiDataDemo() {
        data[jumlahData++] = new Reservasi(nextId++, "Budi Santoso",  "M01", "10-06-2026", "12:00", 2, "REGULAR");
        data[jumlahData++] = new Reservasi(nextId++, "Siti Rahayu",   "M05", "10-06-2026", "13:00", 4, "VIP");
        data[jumlahData++] = new Reservasi(nextId++, "Ahmad Fauzi",   "M03", "11-06-2026", "19:00", 3, "OUTDOOR");
        data[jumlahData++] = new Reservasi(nextId++, "Dewi Lestari",  "M02", "11-06-2026", "20:00", 6, "VIP");
        data[jumlahData++] = new Reservasi(nextId++, "Rizky Pratama", "M04", "12-06-2026", "18:30", 2, "REGULAR");

        // Simulasi counter: Budi sudah pernah reservasi sebelumnya
        data[jumlahData - 1].counter = 1;
        System.out.println("[INFO] 5 data demo berhasil dimuat.");
    }

    // ==========================================================
    //  MAIN MENU
    // ==========================================================
    public static void main(String[] args) {

        System.out.println("\n" + "=".repeat(50));
        System.out.println("   RESTAURANT RESERVATION SYSTEM");
        System.out.println("   Modul CRUD – ASD SYS61618");
        System.out.println("=".repeat(50));

        isiDataDemo();

        boolean jalan = true;
        while (jalan) {
            System.out.println("\n===== MENU UTAMA =====");
            System.out.println("[1] Tambah Reservasi Baru");
            System.out.println("[2] Tampilkan Semua Reservasi");
            System.out.println("[3] Edit Reservasi");
            System.out.println("[4] Hapus Reservasi");
            System.out.println("[0] Keluar");
            System.out.print("Pilih menu: ");

            String pilihan = sc.nextLine().trim();

            switch (pilihan) {
                case "1": tambahReservasi();  break;
                case "2": tampilkanSemua();   break;
                case "3": editReservasi();    break;
                case "4": hapusReservasi();   break;
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