package com.fleurist.driver;

import com.fleurist.account.Akun;
import com.fleurist.account.Admin;
import com.fleurist.account.Customer;
import com.fleurist.model.Barang;
import com.fleurist.model.ProductCatalog;
import com.fleurist.model.Transaksi;
// import com.fleurist.util.DataLoader; // Aktifkan jika nanti pakai CSV

import java.util.ArrayList;
import java.util.List;

public class Driver {

    // STORAGE GLOBAL
    private static List<Akun> akunList = new ArrayList<>();
    private static ProductCatalog catalog = new ProductCatalog();
    private static List<Transaksi> transaksiList = new ArrayList<>();

    // --- GEMBOK PENGAMAN (PENTING) ---
    private static boolean isInitialized = false;

    public static ProductCatalog getCatalog() {
        return catalog;
    }

    public static List<Transaksi> getTransaksiList() {
        return transaksiList;
    }

    public static List<Akun> getAkunList() {
        return akunList;
    }

    // ---------------------------------------------------------
    // INITIALIZER
    // ---------------------------------------------------------
    public static void init() {
        // CEK: Jika sudah pernah dijalankan (true), langsung berhenti.
        if (isInitialized) {
            return;
        }

        // Tambah akun default
        akunList.add(new Admin("admin", "admin123"));
        akunList.add(new Customer("user", "user123", "Default User"));
        
        // --- TAMBAH 15 DATA DUMMY
        // Urutan Konstruktor: (id, nama, harga, stok, warna, jenis, tipe, fileGambar)
        catalog.tambahBarang(new Barang("P001", "Mawar Merah", 250000, 10, "Merah", "Bunga", "Hand Bouquet", "mawar.jpg"));
        catalog.tambahBarang(new Barang("P002", "Tulip Putih", 300000, 8, "Putih", "Bunga", "Table Bouquet", "tulip.jpg"));
        catalog.tambahBarang(new Barang("P003", "Anggrek Ungu", 150000, 5, "Ungu", "Tanaman", "Pot Plant", "anggrek.jpg"));
        catalog.tambahBarang(new Barang("P004", "Krisan Kuning", 200000, 12, "Kuning", "Bunga", "Hand Bouquet", "krisan.jpg"));
        catalog.tambahBarang(new Barang("P005", "Lili Pink", 275000, 7, "Pink", "Bunga", "Table Bouquet", "lili.jpg"));
        catalog.tambahBarang(new Barang("P006", "Bunga Matahari", 180000, 6, "Kuning", "Bunga", "Pot Plant", "sunflower.jpg"));
        catalog.tambahBarang(new Barang("P007", "Melati Putih", 220000, 15, "Putih", "Bunga", "Hand Bouquet", "melati.jpg"));
        catalog.tambahBarang(new Barang("P008", "Cactus Hijau", 120000, 10, "Hijau", "Tanaman", "Pot Plant", "cactus.jpg"));
        catalog.tambahBarang(new Barang("P009", "Rose Orange", 260000, 9, "Oranye", "Bunga", "Hand Bouquet", "rose_o.jpg"));
        catalog.tambahBarang(new Barang("P010", "Anggrek Putih", 160000, 8, "Putih", "Tanaman", "Pot Plant", "anggrek_p.jpg"));
        catalog.tambahBarang(new Barang("P011", "Tulip Merah", 290000, 5, "Merah", "Bunga", "Table Bouquet", "tulip_r.jpg"));
        catalog.tambahBarang(new Barang("P012", "Bonsai Hijau", 350000, 4, "Hijau", "Tanaman", "Pot Plant", "bonsai.jpg"));
        catalog.tambahBarang(new Barang("P013", "Gerbera Pink", 210000, 11, "Pink", "Bunga", "Hand Bouquet", "gerbera.jpg"));
        catalog.tambahBarang(new Barang("P014", "Kaktus Kecil", 95000, 20, "Hijau", "Tanaman", "Pot Plant", "kaktus_k.jpg"));
        catalog.tambahBarang(new Barang("P015", "Lavender Ungu", 175000, 7, "Ungu", "Tanaman", "Pot Plant", "lavender.jpg"));
        // -----------------------------------------------------------------------

        // Jika mau pakai CSV, panggil DataLoader di sini (Opsional)

        //DataLoader.loadProdukCSV("src/main/resources/com/fleurist/produk_bunga_tanaman.csv", catalog);

        //DataLoader.loadAkunCSV("src/main/resources/com/fleurist/akun_users.csv", akunList);

        //DataLoader.loadTransaksiCSV("src/main/resources/com/fleurist/transaksi_history.csv", transaksiList, catalog, akunList);

        System.out.println("===== Sistem berhasil diinisialisasi =====");
        isInitialized = true;
    }

    // ---------------------------------------------------------
    // LOGIN SYSTEM
    // ---------------------------------------------------------
    public static Akun login(String username, String password) {
        for (Akun a : akunList) {
            if (a.getUsername().equals(username) && a.authenticate(password)) {
                return a;
            }
        }
        return null;
    }
}
