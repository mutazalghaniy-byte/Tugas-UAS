public void updateStatus(int id, String statusBaru) {
            Reservation temp = head;
            while (temp != null) {
                if (temp.id == id && temp.isDeleted == false) {
                    temp.status = statusBaru;

            System.out.println("=== Status reservasi berhasil diubah ===");
            System.out.println("ID Reservasi : " + id);
            System.out.println("Status Baru  : " + statusBaru);

            return;
        }

        temp = temp.next;
    }

    System.out.println("ID reservasi tidak ditemukan");
}




public void statistik() {

    int totalAktif = 0;
    int aktif = 0;
    int selesai = 0;
    int dibatalkan = 0;
    int totalTamu = 0;
    int maxTamu = 0;

    Reservation temp = head;

    while (temp != null) {

        if (temp.isDeleted == false) {

            totalAktif = totalAktif + 1;

            totalTamu = totalTamu + temp.jumlahTamu;

            if (temp.jumlahTamu > maxTamu) {
                maxTamu = temp.jumlahTamu;
            }

            if (temp.status.equals("AKTIF")) {
                aktif++;
            }
            else if (temp.status.equals("SELESAI")) {
                selesai++;
            }
            else if (temp.status.equals("DIBATALKAN")) {
                dibatalkan++;
            }
        }

        temp = temp.next;
    }

    System.out.println("\n ====== STATISTIK RESERVASI ======");
    System.out.println("   Total Reservasi (termasuk dihapus): " + size);
    System.out.println("   Total Reservasi Aktif             : " + totalAktif);
    System.out.println("   • AKTIF                           : " + aktif);
    System.out.println("   • SELESAI                         : " + selesai);
    System.out.println("   • DIBATALKAN                      : " + dibatalkan);
    System.out.println("   Total Tamu                        : " + totalTamu + " orang");

    if (totalAktif > 0) {

        double rataRata = (double) totalTamu / totalAktif;

        System.out.printf("   Rata-rata Tamu                    : %.1f orang%n", rataRata);

        System.out.println("   Tamu Terbanyak                    : " + maxTamu + " orang");
    }

    System.out.println("====================================");
}}






