package com.fleurist.driver;

import com.fleurist.account.Admin;
import com.fleurist.model.Barang;
import com.fleurist.model.ProductCatalog;
import com.fleurist.model.Diskon;
import com.fleurist.model.Transaksi;
import java.util.Scanner;

public class AdminDriver {

    public static void run(Admin admin, ProductCatalog catalog) {
        Scanner input = new Scanner(System.in);

        System.out.println("Selamat datang, Admin " + admin.getUsername());

        while (true) {
            System.out.println("\n=== MENU ADMIN ===");
            System.out.println("1. Lihat Katalog");
            System.out.println("2. Kelola Barang");
            System.out.println("3. Kelola Diskon");
            System.out.println("4. Lihat & Terima Transaksi");
            System.out.println("5. Logout");
            System.out.print("Pilih menu: ");
            String pilih = input.nextLine();

            switch (pilih) {
                case "1":
                    System.out.println("=== KATALOG PRODUK ===");
                    for (Barang b : catalog.getBarangList()) {
                        System.out.println(b.toString());
                    }
                    break;

                case "2":
                    kelolaBarang(catalog, input);
                    break;

                case "3":
                    kelolaDiskon(catalog, input);
                    break;

                case "4":
                    lihatTransaksi(input);
                    break;

                case "5":
                    System.out.println("Logout berhasil.");
                    return;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void kelolaBarang(ProductCatalog catalog, Scanner input) {
        while (true) {
            System.out.println("\n--- KELOLA BARANG ---");
            System.out.println("1. Tambah Barang");
            System.out.println("2. Edit Barang");
            System.out.println("3. Hapus Barang");
            System.out.println("4. Kembali");
            System.out.print("Pilih menu: ");
            String pilih = input.nextLine();

            switch (pilih) {
                case "1":
                    System.out.println("\n=== KATALOG PRODUK (TABEL) ===");
                    if (catalog.getBarangList().isEmpty()) {
                        System.out.println("Data barang kosong.");
                    } else {
                        // Header Tabel (Total lebar +- 88 karakter)
                        System.out.println("+-------+----------------------+-------------+------------+------------+------+---------------+");
                        System.out.printf("| %-5s | %-20s | %-11s | %-10s | %-10s | %-4s | %-13s |\n", 
                                "ID", "NAMA BARANG", "JENIS", "WARNA", "TIPE", "STOK", "HARGA");
                        System.out.println("+-------+----------------------+-------------+------------+------------+------+---------------+");

                        for (Barang b : catalog.getBarangList()) {
                            // Potong nama jika lebih dari 20 huruf agar tabel tidak berantakan
                            String namaFixed = b.getNama().length() > 20 ? b.getNama().substring(0, 17) + "..." : b.getNama();
                            
                            System.out.printf("| %-5s | %-20s | %-11s | %-10s | %-10s | %-4d | Rp %,-10.0f |\n",
                                    b.getId(),
                                    namaFixed,
                                    b.getJenis(),
                                    b.getWarna(),
                                    b.getTipeProduk(), // Pastikan di Barang.java namanya getTipeProduk()
                                    b.getStok(),
                                    b.getHarga());
                        }
                        System.out.println("+-------+----------------------+-------------+------------+------------+------+---------------+");
                    }
                    break;

                case "2":
                    System.out.println("\n--- Edit Barang ---");
                    System.out.print("Masukkan ID Barang yang mau diedit: ");
                    String idEdit = input.nextLine();
                    Barang bLama = catalog.cariBarang(idEdit);

                    if (bLama != null) {
                        System.out.println("Data saat ini: " + bLama.toString());
                        System.out.println("(Tekan Enter jika tidak ingin mengubah data tertentu)");

                        // 1. Edit Nama (Pakai getNama)
                        System.out.print("Nama Baru (" + bLama.getNama() + "): ");
                        String inNama = input.nextLine();
                        String namaBaru = inNama.isEmpty() ? bLama.getNama() : inNama;

                        // 2. Edit Harga (Pakai getHarga)
                        System.out.print("Harga Baru (" + bLama.getHarga() + "): ");
                        String inHarga = input.nextLine();
                        double hargaBaru = inHarga.isEmpty() ? bLama.getHarga() : Double.parseDouble(inHarga);

                        // 3. Edit Stok (Pakai getStok)
                        System.out.print("Stok Baru (" + bLama.getStok() + "): ");
                        String inStok = input.nextLine();
                        int stokBaru = inStok.isEmpty() ? bLama.getStok() : Integer.parseInt(inStok);

                        // 4. Edit Warna (Pakai getWarna)
                        System.out.print("Warna Baru (" + bLama.getWarna() + "): ");
                        String inWarna = input.nextLine();
                        String warnaBaru = inWarna.isEmpty() ? bLama.getWarna() : inWarna;

                        // 5. Edit Jenis (Pakai getJenis)
                        System.out.print("Jenis Baru (" + bLama.getJenis() + "): ");
                        String inJenis = input.nextLine();
                        String jenisBaru = inJenis.isEmpty() ? bLama.getJenis() : inJenis;

                        // 6. Edit Tipe Produk (PERBAIKAN: Pakai getTipeProduk)
                        System.out.print("Tipe Baru (" + bLama.getTipeProduk() + "): ");
                        String inTipe = input.nextLine();
                        String tipeBaru = inTipe.isEmpty() ? bLama.getTipeProduk() : inTipe;

                        // Simpan update
                        Barang bBaru = new Barang(idEdit, namaBaru, hargaBaru, stokBaru, warnaBaru, jenisBaru, tipeBaru);
                        catalog.editBarang(idEdit, bBaru);
                        System.out.println("Data barang berhasil diperbarui!");
                        
                    } else {
                        System.out.println("ID Barang tidak ditemukan.");
                    }
                    break;

                case "3":
                    System.out.print("ID Barang yang akan dihapus: ");
                    String idHapus = input.nextLine();
                    if (catalog.cariBarang(idHapus) != null) {
                        catalog.hapusBarang(idHapus);
                        System.out.println("Barang berhasil dihapus.");
                    } else {
                        System.out.println("ID Barang tidak ditemukan.");
                    }
                    break;

                case "4":
                    return;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void kelolaDiskon(ProductCatalog catalog, Scanner input) {
        while (true) {
            System.out.println("\n--- KELOLA DISKON ---");

            // 1. TAMPILKAN DAFTAR (TABEL)
            if (catalog.getDiskonList().isEmpty()) {
                System.out.println("[Info] Belum ada data diskon.");
            } else {
                System.out.printf("%-10s %-25s %-10s %-10s\n", "ID", "NAMA PROMO", "PERSEN", "STATUS");
                System.out.println("-------------------------------------------------------------");
                for (Diskon d : catalog.getDiskonList()) {
                    String status = d.isAktif() ? "AKTIF" : "NONAKTIF";
                    System.out.printf("%-10s %-25s %-10s %-10s\n",
                            d.getIdDiskon(),
                            d.getNamaDiskon(),
                            d.getPersen() + "%",
                            status);
                }
            }

            System.out.println("\nMenu:");
            System.out.println("1. Tambah Diskon Baru");
            System.out.println("2. Edit Diskon (Nama & Persen)");
            System.out.println("3. Ubah Status (Aktif/Nonaktif)");
            System.out.println("4. Hapus Diskon");
            System.out.println("5. Kembali");
            System.out.print("Pilih menu: ");
            String pilih = input.nextLine();

            switch (pilih) {
                case "1":
                    System.out.println("\n--- Tambah Diskon ---");
                    System.out.print("Masukkan ID Diskon (Contoh: LEBARAN atau D001): ");
                    String id = input.nextLine();

                    if (catalog.cariDiskon(id) != null) {
                        System.out.println("Gagal! ID diskon sudah digunakan.");
                    } else {
                        System.out.print("Masukkan Nama Promo (Contoh: Diskon Hari Raya): ");
                        String nama = input.nextLine();
                        
                        System.out.print("Masukkan Persen (Contoh: 10 untuk 10%): ");
                        try {
                            double persen = Double.parseDouble(input.nextLine());
                            Diskon d = new Diskon(id, nama, persen, true);
                            catalog.tambahDiskon(d);
                            System.out.println("Sukses! Diskon berhasil ditambahkan.");
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Persen harus berupa angka!");
                        }
                    }
                    break;

                case "2":
                    System.out.println("\n--- Edit Diskon ---");
                    System.out.print("Masukkan ID Diskon yang mau diedit (Contoh: D001): ");
                    String idEdit = input.nextLine();
                    Diskon dEdit = catalog.cariDiskon(idEdit);

                    if (dEdit != null) {
                        System.out.println("Mengedit: " + dEdit.getNamaDiskon());
                        System.out.print("Nama Baru (Contoh: Promo Merdeka): ");
                        String namaBaru = input.nextLine();
                        
                        System.out.print("Persen Baru (Contoh: 20): ");
                        try {
                            double persenBaru = Double.parseDouble(input.nextLine());
                            dEdit.setNamaDiskon(namaBaru);
                            dEdit.setPersen(persenBaru);
                            System.out.println("Data diskon diperbarui.");
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Input angka salah, edit dibatalkan.");
                        }
                    } else {
                        System.out.println("ID diskon tidak ditemukan.");
                    }
                    break;

                case "3":
                    System.out.println("\n--- Ubah Status ---");
                    System.out.print("Masukkan ID Diskon (Contoh: D001): ");
                    String idStatus = input.nextLine();
                    Diskon dStatus = catalog.cariDiskon(idStatus);

                    if (dStatus != null) {
                        dStatus.setAktif(!dStatus.isAktif());
                        System.out.println("Status berhasil diubah menjadi: " 
                            + (dStatus.isAktif() ? "AKTIF" : "NONAKTIF"));
                    } else {
                        System.out.println("ID diskon tidak ditemukan.");
                    }
                    break;

                case "4":
                    System.out.println("\n--- Hapus Diskon ---");
                    System.out.print("Masukkan ID Diskon yang akan dihapus: ");
                    String idHapus = input.nextLine();
                    if (catalog.cariDiskon(idHapus) != null) {
                        catalog.hapusDiskon(idHapus);
                        System.out.println("Diskon dihapus permanen.");
                    } else {
                        System.out.println("ID tidak ditemukan.");
                    }
                    break;

                case "5":
                    return;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    private static void lihatTransaksi(Scanner input) {
        if (Driver.getTransaksiList().isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }

        for (int i = 0; i < Driver.getTransaksiList().size(); i++) {
            Transaksi t = Driver.getTransaksiList().get(i);
            System.out.println("[" + i + "] " + t.getIdTransaksi() +
                    " | Total: Rp " + t.getTotalAkhir() +
                    " | Diterima: " + (t.isDiterimaAdmin() ? "Ya" : "Belum"));
        }

        System.out.print("Pilih transaksi untuk diterima (atau tekan Enter untuk kembali): ");
        String pilih = input.nextLine();
        if (!pilih.isEmpty()) {
            try {
                int idx = Integer.parseInt(pilih);
                if (idx >= 0 && idx < Driver.getTransaksiList().size()) {
                    Driver.getTransaksiList().get(idx).terimaTransaksi();
                    System.out.println("Transaksi diterima.");
                } else {
                    System.out.println("Indeks tidak valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Input harus angka indeks.");
            }
        }
    }
}
