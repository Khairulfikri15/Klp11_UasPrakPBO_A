// cspell:ignore fleurist utama fallback loadProduk loadProdukFromCSV dataFromCSV isEmpty ditemukan kosong Menggunakan dummy loadDummyProduk skipHeader readLine continue split length parseDouble parseInt Gagal membaca langsung nanti aktif Mawar Merah Tulip Anggrek Ungu Tanaman Bouquet Table
package com.fleurist.util;

import com.fleurist.model.Barang;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    // Method untuk load produk ke catalog
    public static void loadProdukCSV(String filePath, com.fleurist.model.ProductCatalog catalog) {
        List<Barang> dataFromCSV = loadProdukFromCSV(filePath);
        
        if (!dataFromCSV.isEmpty()) {
            for (Barang b : dataFromCSV) {
                catalog.tambahBarang(b);
            }
            System.out.println("[INFO] Data produk berhasil dimuat dari CSV.");
        }
    }

    // Method utama dengan fallback
    public static List<Barang> loadProduk(String filePath) {
        List<Barang> dataFromCSV = loadProdukFromCSV(filePath);

        if (dataFromCSV.isEmpty()) {
            System.out.println("[INFO] File CSV tidak ditemukan atau kosong. Menggunakan data dummy...");
            return loadDummyProduk();
        }

        return dataFromCSV;
    }

    // Load dari CSV
    private static List<Barang> loadProdukFromCSV(String filePath) {
        List<Barang> list = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean skipHeader = true;

            while ((line = br.readLine()) != null) {

                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }

                String[] data = line.split(",");

                if (data.length != 7) continue;

                String id = data[0];
                String nama = data[1];
                String jenis = data[2];
                String warna = data[3];
                String tipeProduk = data[4];
                double harga = Double.parseDouble(data[5]);
                int stok = Integer.parseInt(data[6]);

                Barang barang = new Barang(
                        id, nama, harga, stok, jenis, warna, tipeProduk
                );

                list.add(barang);
            }

        } catch (Exception e) {
            System.out.println("[WARNING] Gagal membaca CSV: " + e.getMessage());
            // langsung return kosong, nanti fallback aktif
        }

        return list;
    }

    // Data dummy default (jika CSV tidak ada)
    private static List<Barang> loadDummyProduk() {
        List<Barang> list = new ArrayList<>();

        list.add(new Barang("D001", "Mawar Merah", 250000, 10, "Bunga", "Merah", "Hand Bouquet"));
        list.add(new Barang("D002", "Tulip Pink", 275000, 8, "Bunga", "Pink", "Table Bouquet"));
        list.add(new Barang("D003", "Anggrek Ungu", 180000, 5, "Tanaman", "Ungu", "Pot Plant"));

        return list;
    }
}
