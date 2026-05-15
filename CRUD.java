import java.util.Scanner;
import java.io.*;

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

        if (tamu <= 0)
            throw new NumberFormatException();

    } catch (NumberFormatException e) {

        System.out.println("[ERROR] Jumlah tamu harus angka positif!");
        return;
    }

    System.out.print("Kategori Meja (VIP / Regular / Outdoor) : ");
    String kategori = sc.nextLine().trim().toUpperCase();

    if (!kategori.equals("VIP") &&
        !kategori.equals("REGULAR") &&
        !kategori.equals("OUTDOOR")) {

        System.out.println("[ERROR] Kategori tidak valid! Pilih: VIP, Regular, atau Outdoor.");
        return;
    }

    // Cek apakah customer sudah pernah reservasi => update counter
    int idxLama = cariIndexNama(nama);

    Reservasi baru = new Reservasi(
            nextId++,
            nama,
            meja,
            tanggal,
            jam,
            tamu,
            kategori
    );

    if (idxLama != -1) {

        // counter merupakan akumulasi dari seluruh reservasi atas nama yang sama
        baru.counter = data[idxLama].counter + 1;
    }

    data[jumlahData++] = baru;

    System.out.println("[OK] Reservasi berhasil ditambahkan dengan ID: " + baru.id);

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

        if (!data[i].status.equals("DIHAPUS")) {

            cetakBaris(data[i]);

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

    if (!input.isEmpty())
        r.namaCustomer = input;

    System.out.print("Nomor Meja      [" + r.nomorMeja + "] : ");
    input = sc.nextLine().trim().toUpperCase();

    if (!input.isEmpty())
        r.nomorMeja = input;

    System.out.print("Tanggal         [" + r.tanggal + "] : ");
    input = sc.nextLine().trim();

    if (!input.isEmpty())
        r.tanggal = input;

    System.out.print("Jam             [" + r.jam + "] : ");
    input = sc.nextLine().trim();

    if (!input.isEmpty())
        r.jam = input;

    System.out.print("Jumlah Tamu     [" + r.jumlahTamu + "] : ");
    input = sc.nextLine().trim();

    if (!input.isEmpty()) {

        try {

            int tamu = Integer.parseInt(input);

            if (tamu > 0) {

                r.jumlahTamu = tamu;

            } else {

                System.out.println("[PERINGATAN] Jumlah tamu tidak valid, nilai lama dipertahankan.");
            }

        } catch (NumberFormatException e) {

            System.out.println("[PERINGATAN] Input bukan angka, nilai lama dipertahankan.");
        }
    }

    System.out.print("Kategori Meja   [" + r.kategoriMeja + "] (VIP/Regular/Outdoor) : ");
    input = sc.nextLine().trim().toUpperCase();

    if (!input.isEmpty()) {

        if (input.equals("VIP") ||
            input.equals("REGULAR") ||
            input.equals("OUTDOOR")) {

            r.kategoriMeja = input;

        } else {

            System.out.println("[PERINGATAN] Kategori tidak valid, nilai lama dipertahankan.");
        }
    }

    System.out.print("Status          [" + r.status + "] (AKTIF/SELESAI) : ");
    input = sc.nextLine().trim().toUpperCase();

    if (!input.isEmpty()) {

        if (input.equals("AKTIF") ||
            input.equals("SELESAI")) {

            r.status = input;

        } else {

            System.out.println("[PERINGATAN] Status tidak valid, nilai lama dipertahankan.");
        }
    }

    System.out.println("[OK] Data reservasi ID " + idCari + " berhasil diperbarui.");

    simpanKeFile();
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

        // Soft delete: status diubah, data tetap ada
        data[idx].status = "DIHAPUS";

        System.out.println("[OK] Reservasi ID " + idCari + " berhasil dihapus (soft delete).");

        simpanKeFile();

    } else {

        System.out.println("[BATAL] Penghapusan dibatalkan.");
    }
}