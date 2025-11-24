// cspell:ignore fleurist idTransaksi metodePembayaran tanggal diterimaAdmin getTotalAkhir isAktif getPersen terimaTransaksi isDiterimaAdmin getIdTransaksi getKeranjang getMetodePembayaran getDiskon getTanggal
package com.fleurist.model;

import java.time.LocalDateTime;
import java.util.UUID;
import com.fleurist.model.pembayaran.Pembayaran;


public class Transaksi {

    private String idTransaksi;
    private Keranjang keranjang;
    private Pembayaran metodePembayaran;
    private Diskon diskon;
    private LocalDateTime tanggal;
    private boolean diterimaAdmin;

    public Transaksi(Keranjang keranjang, Pembayaran pembayaran, Diskon diskon) {
        this.idTransaksi = "TRX-" + UUID.randomUUID().toString().substring(0, 6);
        this.keranjang = keranjang;
        this.metodePembayaran = pembayaran;
        this.diskon = diskon;
        this.tanggal = LocalDateTime.now();
        this.diterimaAdmin = false;
    }

    public double getTotalAkhir() {
        double total = keranjang.totalKeranjang();
        if (diskon != null && diskon.isAktif()) {
            total -= total * (diskon.getPersen() / 100.0);
        }
        return total;
    }

    public void terimaTransaksi() {
        this.diterimaAdmin = true;
    }

    public boolean isDiterimaAdmin() {
        return diterimaAdmin;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public Keranjang getKeranjang() { return keranjang; }

    public Pembayaran getMetodePembayaran() { return metodePembayaran; }

    public Diskon getDiskon() { return diskon; }

    public LocalDateTime getTanggal() { return tanggal; }
}
