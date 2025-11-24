// cspell:ignore fleurist memiliki keranjang riwayat operasi belanja Checkout membuat penempatan dilakukan namaLengkap getNamaLengkap getKeranjang getHistory tambahKeKeranjang jumlah harus cukup hapusDariKeranjang idBarang Buat objek mengosongkan sesuai alur pengolahan lebih lanjut pembayaran verifikasi metode dipakai boleh checkout getItems isEmpty kosong salinan dalam Setelah menerima sebaiknya memanggil punya tambahHistory
package com.fleurist.account;

import com.fleurist.model.Keranjang;
import com.fleurist.model.Barang;
import com.fleurist.model.Transaksi;
import com.fleurist.model.Invoice;
import com.fleurist.model.Diskon;
import com.fleurist.model.pembayaran.Pembayaran;

import java.util.ArrayList;
import java.util.List;

/**
 * Customer: memiliki keranjang, riwayat invoice, dan operasi belanja.
 * Checkout membuat Transaksi (tapi penempatan Transaksi ke storage global
 * dilakukan oleh controller/driver).
 */
public class Customer extends Akun {

    private String namaLengkap;
    private Keranjang keranjang = new Keranjang();
    private List<Invoice> history = new ArrayList<>();

    public Customer(String username, String password, String namaLengkap) {
        super(username, password);
        this.namaLengkap = namaLengkap;
    }

    @Override
    public String getRole() {
        return "CUSTOMER";
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public Keranjang getKeranjang() {
        return keranjang;
    }

    public List<Invoice> getHistory() {
        return history;
    }

    public void tambahKeKeranjang(Barang barang, int jumlah) {
        if (barang == null) throw new IllegalArgumentException("Barang null");
        if (jumlah <= 0) throw new IllegalArgumentException("Jumlah harus > 0");
        if (jumlah > barang.getStok()) throw new IllegalArgumentException("Stok tidak cukup");
        keranjang.tambahBarang(barang, jumlah);
    }

    public void hapusDariKeranjang(String idBarang) {
        keranjang.hapusBarang(idBarang);
    }

    /**
     * Buat transaksi dari keranjang. Method ini hanya membuat objek Transaksi
     * dan mengosongkan keranjang (sesuai alur). Penempatan di storage transaksi
     * dan pengolahan lebih lanjut (pembayaran, verifikasi) dilakukan oleh controller.
     *
     * @param pembayaran metode pembayaran yang dipilih
     * @param diskon     diskon yang dipakai (boleh null)
     * @return Transaksi yang telah dibuat
     */
    public Transaksi checkout(Pembayaran pembayaran, Diskon diskon) {
        if (keranjang.getItems().isEmpty()) throw new IllegalStateException("Keranjang kosong");
        Transaksi trx = new Transaksi(keranjang, pembayaran, diskon);
        // kosongkan keranjang setelah membuat transaksi (salinan internal di dalam Transaksi)
        this.keranjang = new Keranjang();
        return trx;
    }

    /**
     * Setelah admin menerima transaksi dan membuat invoice, controller/driver
     * sebaiknya memanggil method ini agar customer punya riwayat.
     */
    public void tambahHistory(Invoice invoice) {
        if (invoice != null) history.add(invoice);
    }
}
