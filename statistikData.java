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