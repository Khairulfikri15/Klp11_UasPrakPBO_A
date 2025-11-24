package com.fleurist.model.pembayaran;

public class QRIS implements Pembayaran {

    @Override
    public boolean prosesPembayaran(double jumlah) {
        System.out.println("-------------------------------------------");
        System.out.println("   SCAN QR CODE DI BAWAH UNTUK MEMBAYAR    ");
        System.out.println("-------------------------------------------");
        System.out.println("       [  QR CODE  ]       ");
        System.out.println("       [  GENERATE ]       ");
        System.out.println("       [  SUCCESS  ]       ");
        System.out.println("-------------------------------------------");
        // Di sini kita memanggil variabel 'jumlah' yang dideklarasikan di atas
        System.out.println("Membayar Rp " + jumlah + " via QRIS...");
        System.out.println("Pembayaran Berhasil!");
        
        return true;
    }

    @Override
    public String getNamaMetode() {
        return "QRIS (E-Wallet/M-Banking)";
    }

    @Override
    public String toString() {
        return getNamaMetode();
    }
}