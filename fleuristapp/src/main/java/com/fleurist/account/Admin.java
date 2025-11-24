// cspell:ignore fleurist bertanggung jawab untuk menerima transaksi menyimpan katalog parameter sehingga controller mengatur dependency injection sederhana delegated tambahBarang hapusBarang idBarang memberi menggantikan memanggil mengisi sebelum editBarang barangBaru handling lihat daftar menyediakan representasi perilaku lihatTransaksi getIdTransaksi getTotalAkhir isDiterimaAdmin Menerima menandai diterima membuat mengembalikan memastikan mengupdate setelah terimaTransaksi sudah
package com.fleurist.account;

import com.fleurist.model.ProductCatalog;
import com.fleurist.model.Barang;
import com.fleurist.model.Transaksi;
import com.fleurist.model.Invoice;

import java.util.List;

/**
 * Admin role: bertanggung jawab untuk CRUD produk dan menerima transaksi.
 * Admin tidak menyimpan data katalog/transaksi sendiri — operasi menerima
 * parameter sehingga controller/driver bisa mengatur storage (dependency injection sederhana).
 */
public class Admin extends Akun {

    public Admin(String username, String password) {
        super(username, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    // ----- Product management (delegated to ProductCatalog) -----
    public void tambahBarang(ProductCatalog catalog, Barang barang) {
        catalog.tambahBarang(barang);
    }

    public void hapusBarang(ProductCatalog catalog, String idBarang) {
        catalog.hapusBarang(idBarang);
    }

    /**
     * Edit barang: memberi barang baru yang akan menggantikan barang lama dengan id yang sama.
     * Caller dapat memanggil catalog.cariBarang untuk mengisi form edit sebelum memanggil method ini.
     */
    public void editBarang(ProductCatalog catalog, String idBarang, Barang barangBaru) {
        catalog.editBarang(idBarang, barangBaru);
    }

    // ----- Transaksi handling -----
    /**
     * Lihat daftar transaksi (controller/driver akan menyediakan list).
     * Di sini method dibuat hanya sebagai representasi perilaku admin.
     */
    public void lihatTransaksi(List<Transaksi> transaksiList) {
        System.out.println("=== Daftar Transaksi ===");
        for (Transaksi t : transaksiList) {
            System.out.println(t.getIdTransaksi() + " | Total: " + t.getTotalAkhir() + " | Diterima: " + t.isDiterimaAdmin());
        }
    }

    /**
     * Menerima transaksi: menandai diterima, membuat invoice, dan mengembalikan invoice string.
     * Controller/driver harus memastikan mengupdate storage transaksi setelah memanggil ini.
     */
    public Invoice terimaTransaksi(Transaksi t) {
        if (t == null) throw new IllegalArgumentException("Transaksi null");
        if (t.isDiterimaAdmin()) return null; // sudah diterima
        t.terimaTransaksi();
        Invoice invoice = new Invoice(t);
        return invoice;
    }
}

