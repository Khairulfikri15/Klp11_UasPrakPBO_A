/**
 * Kelas Barang merepresentasikan objek produk yang dijual di toko bunga.
 * @author kelompok 11
 */

package com.fleurist.model;

public class Barang {
    private String id;
    private String nama;
    private double harga;
    private int stok;

    private String warna;       
    private String jenis;       
    private String tipeProduk;  
    private String imageFile; // Field ke-8 (Hanya dipakai GUI)

    // --- CONSTRUCTOR UTAMA (8 ARGUMEN) ---
    public Barang(String id, String nama, double harga, int stok, String warna, String jenis, String tipeProduk, String imageFile) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
        this.warna = warna;
        this.jenis = jenis;
        this.tipeProduk = tipeProduk;
        this.imageFile = imageFile; 
    }
    
    // --- CONSTRUCTOR OVERLOAD (7 ARGUMEN - UNTUK CLI/KODE LAMA) ---
    // Kode lama yang memanggil 7 argumen akan otomatis memanggil constructor ini.
    public Barang(String id, String nama, double harga, int stok, String warna, String jenis, String tipeProduk) {
        // Panggil constructor utama, dan set gambar default.
        this(id, nama, harga, stok, warna, jenis, tipeProduk, "cli_placeholder.png"); 
    }


    // --- Getter & Setter ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public String getWarna() { return warna; }
    public void setWarna(String warna) { this.warna = warna; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public String getTipeProduk() { return tipeProduk; }
    public void setTipeProduk(String tipeProduk) { this.tipeProduk = tipeProduk; }

    public String getImageFile() { return imageFile; }
    public void setImageFile(String imageFile) { this.imageFile = imageFile; }

    @Override
    public String toString() {
        return id + " - " + nama + " [" + jenis + ", " + warna + ", " + tipeProduk + "] : Rp" + harga;
    }
}
