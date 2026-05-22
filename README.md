
   RESTAURANT RESERVATION SYSTEM
   Laporan Project UAS - Algoritma dan Struktur Data 


----------------------------------------------------------------
DESKRIPSI PROGRAM
----------------------------------------------------------------
Restaurant Reservation System adalah aplikasi manajemen reservasi
restoran berbasis console yang dibangun menggunakan bahasa Java.
Sistem ini dirancang untuk mengelola data reservasi pelanggan
dengan menerapkan berbagai algoritma dan struktur data secara
manual tanpa menggunakan library sorting/searching bawaan Java.

Struktur data yang digunakan:
  - Array Paralel       : penyimpanan utama data reservasi
  - ArrayList           : log aktivitas, customer loyal, waiting list

Algoritma yang diimplementasi:
  - Linear Search       : pencarian berdasarkan nama customer (O(n))
  - Binary Search       : pencarian berdasarkan ID (O(log n))
  - Bubble Sort         : pengurutan berdasarkan ID ascending (O(n^2))
  - Selection Sort      : pengurutan berdasarkan nama A-Z (O(n^2))
  - Insertion Sort      : pengurutan berdasarkan jumlah tamu desc (O(n^2))

----------------------------------------------------------------
PERSYARATAN PENGGUNAAN SISTEM
----------------------------------------------------------------
  - Java Development Kit (JDK) versi 8 atau lebih baru
  - Sistem operasi: Windows / Linux / macOS
  - Terminal / Command Prompt / PowerShell
 
Cek versi Java yang terinstall:
  java -version
 
Jika belum terinstall, unduh di:
  https://www.oracle.com/java/technologies/downloads/
  
----------------------------------------------------------------
STRUKTUR FILE
----------------------------------------------------------------
  SistemRestaurant.java   -> Source code utama program
  reservasi.txt           -> File penyimpanan data reservasi
                            (dibuat otomatis saat pertama kali run)
  waitinglist.txt         -> File penyimpanan data waiting list
                            (dibuat otomatis saat pertama kali run)
  README.txt              -> Panduan instalasi dan penggunaan ini

----------------------------------------------------------------
CARA MENJALANKAN PROGRAM
----------------------------------------------------------------

LANGKAH 1 - Buka terminal / command prompt

LANGKAH 2 - Masuk ke direktori tempat file berada
  Contoh (Windows):
    cd C:\Users\NamaKamu\project-reservasi

  Contoh (Linux/macOS):
    cd /home/namakamu/project-reservasi

LANGKAH 3 - Kompilasi source code
  javac SistemRestaurant.java

  Jika berhasil, akan muncul file SistemRestaurant.class
  di direktori yang sama.

LANGKAH 4 - Jalankan program
  java SistemRestaurant

  Program akan menampilkan:
    ==================================================
       RESTAURANT RESERVATION SYSTEM
    ==================================================

CATATAN:
  - Saat pertama kali dijalankan dan belum ada file reservasi.txt,
    program akan otomatis memuat 5 data demo.
  - Data akan tersimpan secara otomatis ke reservasi.txt
    setiap kali ada perubahan (tambah, edit, hapus, update status).

----------------------------------------------------------------
PANDUAN FITUR & MENU
----------------------------------------------------------------

[1] TAMBAH RESERVASI BARU
    Menambahkan data reservasi pelanggan baru.
    Input yang dibutuhkan:
      - Nama Customer
      - Nomor Meja (format: M01, M02, dst.)
      - Tanggal (format: DD-MM-YYYY)
      - Jam (format: HH:MM)
      - Jumlah Tamu (angka positif)
      - Kategori Meja: VIP / REGULAR / OUTDOOR

[2] TAMPILKAN SEMUA RESERVASI
    Menampilkan seluruh data reservasi yang aktif dan selesai
    (data dengan status DIHAPUS tidak ditampilkan).

[3] EDIT RESERVASI
    Mengubah data reservasi berdasarkan ID.
    Input yang tidak diisi akan mempertahankan nilai lama.

[4] HAPUS RESERVASI
    Menghapus data reservasi secara soft delete
    (status berubah menjadi DIHAPUS, data tidak hilang dari file).

[5] UPDATE STATUS RESERVASI
    Mengubah status reservasi menjadi AKTIF atau SELESAI.

[6] MENU SEARCHING
    [1] Linear Search  - Cari berdasarkan nama customer
    [2] Binary Search  - Cari berdasarkan ID (data diurutkan dulu)
    [3] Cari Kategori  - Tampilkan semua data berdasarkan kategori

[7] MENU SORTING
    [1] Bubble Sort    - Urutkan berdasarkan ID (Ascending)
    [2] Selection Sort - Urutkan berdasarkan Nama (A-Z)
    [3] Insertion Sort - Urutkan berdasarkan Jumlah Tamu (Terbanyak)

[8] STATISTIK
    Menampilkan ringkasan data: total, aktif, selesai, dihapus,
    dan jumlah per kategori meja (VIP, Regular, Outdoor).

[9] SIMPAN DATA KE FILE
    Menyimpan data reservasi ke file reservasi.txt secara manual.

[10] MENU RIWAYAT DAN CUSTOMER LOYAL
    [1] Tampilkan Log Aktivitas
        Menampilkan semua aktivitas yang telah dilakukan selama
        sesi program berjalan (tambah, edit, hapus, update status).
    [2] Tampilkan Daftar Customer Loyal
        Menampilkan pelanggan yang sudah melakukan reservasi
        lebih dari 1 kali.
    [3] Hapus Semua Log Aktivitas
        Mengosongkan log aktivitas dari memori.

[11] MENU WAITING LIST
    [1] Tambah ke Waiting List
        Mendaftarkan pelanggan ke antrian saat meja tidak tersedia.
    [2] Tampilkan Waiting List
        Melihat daftar antrian yang sedang menunggu.
    [3] Promosikan ke Reservasi Aktif (FIFO)
        Memindahkan pelanggan pertama dari waiting list
        menjadi reservasi aktif dengan nomor meja yang ditentukan.
    [4] Hapus dari Waiting List
        Menghapus pelanggan dari antrian berdasarkan nomor urut.

[12] RESET DATA RESERVASI
    Menghapus SEMUA data reservasi secara permanen.
    Memerlukan konfirmasi dua tahap: ketik 'RESET' lalu 'YA'.
    PERHATIAN: Tindakan ini tidak dapat dibatalkan!

[0] KELUAR
    Menutup program.

----------------------------------------------------------------
FORMAT DATA FILE (reservasi.txt)
----------------------------------------------------------------
Setiap baris menyimpan satu data reservasi dengan format:
  ID;NamaCustomer;NomorMeja;Tanggal;Jam;JumlahTamu;Kategori;Status;Counter

Contoh:
  1;Budi Santoso;M01;10-06-2026;12:00;2;REGULAR;AKTIF;1
  2;Siti Rahayu;M05;10-06-2026;13:00;4;VIP;AKTIF;1

Format data waiting list (waitinglist.txt):
  NamaCustomer|Tanggal|Jam|JumlahTamu|Kategori

Contoh:
  Rina Kusuma|15-06-2026|19:00|3|VIP

----------------------------------------------------------------
KATEGORI MEJA YANG TERSEDIA
----------------------------------------------------------------
  VIP      : Meja premium dengan fasilitas khusus
  REGULAR  : Meja standar untuk pelanggan umum
  OUTDOOR  : Meja di area luar ruangan

----------------------------------------------------------------
CONTOH SKENARIO PENGGUNAAN
----------------------------------------------------------------

SKENARIO 1 - Reservasi baru:
  Pilih menu [1] > Isi semua data > Sistem menyimpan otomatis

SKENARIO 2 - Pelanggan tidak dapat meja, masuk waiting list:
  Pilih menu [11] > [1] > Isi data > Sistem memberi nomor antrian
  Saat meja tersedia: menu [11] > [3] > Masukkan nomor meja

SKENARIO 3 - Mencari reservasi pelanggan tertentu:
  Pilih menu [6] > [1] > Masukkan nama (bisa sebagian)

SKENARIO 4 - Melihat pelanggan yang sering reservasi:
  Pilih menu [10] > [2]

----------------------------------------------------------------
INFORMASI KELOMPOK
----------------------------------------------------------------
Anggota Kelompok:
  1. Nikel Pancaran Suryaman - [2510631250042]
  2. Mu’taz Al Ghaniy- [2510631250072] 
  3. Nadia Herawati - [2510631250015]
  4. Rachmawati Satya Lestari - [2510631250006]

----------------------------------------------------------------
CATATAN TEKNIS
----------------------------------------------------------------
  - Program menggunakan Scanner untuk input dari console.
    Pastikan tidak menutup terminal saat program berjalan.
  - Kapasitas maksimum data reservasi: 100 entri (MAX_DATA = 100).
  - Binary Search membutuhkan data dalam kondisi terurut.
    Program akan mengurut data sementara secara otomatis
    sebelum melakukan Binary Search.
  - Log aktivitas hanya tersimpan selama sesi program berjalan
    dan akan hilang saat program ditutup.

================================================================
   Terima kasih telah menggunakan Restaurant Reservation System
================================================================
