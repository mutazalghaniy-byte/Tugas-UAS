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



    // counter 
    static int[]     counter     = new int[MAX_DATA];