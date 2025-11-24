/**
 * Kelas Invoice merepresentasikan dokumen bukti pembayaran 
 * yang dihasilkan setelah transaksi diterima.
 * @author Kelompok 11
 */

package com.fleurist.model;

import com.fleurist.model.Keranjang.ItemKeranjang;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Invoice {

    private Transaksi transaksi;

    public Invoice(Transaksi transaksi) {
        this.transaksi = transaksi;
    }

    public String generate() {
        StringBuilder sb = new StringBuilder();

        // Format Tanggal Cantik
        String waktuCantik = formatTanggal(transaksi.getTanggal());

        sb.append("=========================================\n");
        sb.append("           INVOICE FLEURIST              \n");
        sb.append("=========================================\n");
        sb.append("ID Transaksi : ").append(transaksi.getIdTransaksi()).append("\n");
        sb.append("Waktu        : ").append(waktuCantik).append("\n");
        sb.append("-----------------------------------------\n");

        sb.append("Daftar Belanja:\n");
        for (ItemKeranjang item : transaksi.getKeranjang().getItems()) {
            sb.append("- ").append(item.getBarang().getNama())
              .append(" (x").append(item.getJumlah()).append(")")
              .append(" : Rp ").append(formatUang(item.getSubtotal())).append("\n");
        }

        sb.append("-----------------------------------------\n");
        
        double subtotal = transaksi.getKeranjang().totalKeranjang();
        sb.append("Subtotal      : Rp ").append(formatUang(subtotal)).append("\n");

        if (transaksi.getDiskon() != null && transaksi.getDiskon().isAktif()) {
            double persen = transaksi.getDiskon().getPersen();
            double nominalDiskon = subtotal * persen / 100;
            
            sb.append(String.format("Diskon (%.1f%%)  : -Rp %s\n", persen, formatUang(nominalDiskon)));
        }

        sb.append("=========================================\n");
        sb.append("TOTAL BAYAR   : Rp ").append(formatUang(transaksi.getTotalAkhir())).append("\n");
        
        // PERBAIKAN: Menggunakan getMetodePembayaran() agar sesuai dengan Transaksi.java
        sb.append("Metode Bayar  : ").append(transaksi.getMetodePembayaran().getNamaMetode()).append("\n");
        sb.append("=========================================\n");
        sb.append("   Terima Kasih telah berbelanja!   \n");

        return sb.toString();
    }

    private String formatUang(double uang) {
        return String.format("%.0f", uang);
    }

    private String formatTanggal(Object tanggalObj) {
        if (tanggalObj == null) return "-";
        try {
            if (tanggalObj instanceof LocalDateTime) {
                LocalDateTime ldt = (LocalDateTime) tanggalObj;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                return ldt.format(formatter);
            }
            String str = tanggalObj.toString();
            if (str.contains("T")) {
                LocalDateTime ldt = LocalDateTime.parse(str);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                return ldt.format(formatter);
            }
            return str;
        } catch (Exception e) {
            return tanggalObj.toString();
        }
    }
}
