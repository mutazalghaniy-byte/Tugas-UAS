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


// load data dari file
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
            String[] bagian = line.split(";");

            id[jumlahData]           = Integer.parseInt(bagian[0]);
            namaCustomer[jumlahData] = bagian[1];
            nomorMeja[jumlahData]    = bagian[2];
            tanggal[jumlahData]      = bagian[3];
            jam[jumlahData]          = bagian[4];
            jumlahTamu[jumlahData]   = Integer.parseInt(bagian[5]);
            kategoriMeja[jumlahData] = bagian[6];
            status[jumlahData]       = bagian[7];
            counter[jumlahData]      = Integer.parseInt(bagian[8]);

            jumlahData++;
        }

        br.close();
        System.out.println("[OK] Data berhasil dimuat dari file.");

    } catch (IOException e) {
        System.out.println("[ERROR] Gagal membaca file!");
    }
}