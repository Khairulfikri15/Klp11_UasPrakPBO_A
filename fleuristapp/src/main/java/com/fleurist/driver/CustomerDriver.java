// cspell:ignore fleurist Selamat datang Lihat Katalog Tambah Barang Keranjang Checkout Riwayat Transaksi Logout Pilih Masukkan ditemukan Jumlah ditambahkan kosong metode pembayaran Transfer Pilihan aktif dikembangkan getDiskonByKode Buat bayar berhasil Belum ada generate
package com.fleurist.driver;

import com.fleurist.account.Customer;
import com.fleurist.model.Barang;
import com.fleurist.model.ProductCatalog;
import com.fleurist.model.Keranjang;
import com.fleurist.model.Transaksi;
import com.fleurist.model.Invoice;
import com.fleurist.model.Diskon;
import com.fleurist.model.pembayaran.Pembayaran;
import com.fleurist.model.pembayaran.QRIS;
import com.fleurist.model.pembayaran.COD;
import com.fleurist.model.pembayaran.Bank;

import java.util.List;
import java.util.Scanner;

public class CustomerDriver {

    public static void run(Customer customer, ProductCatalog catalog) {
        Scanner input = new Scanner(System.in);
        Keranjang keranjang = new Keranjang();

        System.out.println("Selamat datang, " + customer.getUsername());

        while (true) {
            System.out.println("\n=== MENU CUSTOMER ===");
            System.out.println("1. Lihat Katalog");
            System.out.println("2. Tambah Barang ke Keranjang");
            System.out.println("3. Lihat Keranjang");
            System.out.println("4. Checkout");
            System.out.println("5. Riwayat Transaksi");
            System.out.println("6. Logout");
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
                    System.out.print("Masukkan ID Barang: ");
                    String id = input.nextLine();
                    Barang b = catalog.cariBarang(id);
                    if (b == null) {
                        System.out.println("Barang tidak ditemukan.");
                        break;
                    }
                    System.out.print("Jumlah: ");
                    int jumlah = Integer.parseInt(input.nextLine());
                    customer.tambahKeKeranjang(b, jumlah);
                    System.out.println("Barang ditambahkan ke keranjang.");
                    break;

                case "3":
                    System.out.println("=== ISI KERANJANG ===");
                    System.out.println(customer.getKeranjang().toString());
                    break;

                case "4":
                    if (customer.getKeranjang().getItems().isEmpty()) {
                        System.out.println("Keranjang kosong!");
                        break;
                    }

                    // 1. Pilih Metode Pembayaran
                    System.out.println("\n--- Pilih Metode Pembayaran ---");
                    System.out.println("1. COD (Bayar di Tempat)");
                    System.out.println("2. Bank Transfer");
                    System.out.println("3. QRIS (Gopay/Ovo/Dana/BCA)");
                    System.out.print("Pilihan: ");
                    String metode = input.nextLine();

                    Pembayaran pembayaran = null;

                    if (metode.equals("1")) {
                        pembayaran = new COD();
                    } else if (metode.equals("2")) {
                        pembayaran = new Bank();
                    } else if (metode.equals("3")) {
                        pembayaran = new QRIS();
                    } else {
                        System.out.println("Pilihan pembayaran salah! Transaksi DIBATALKAN.");
                        break; 
                    }

                    // 2. LOGIKA DISKON (DIPERBAIKI TAMPILANNYA)
                    Diskon diskon = null;
                    System.out.print("Punya kode voucher/diskon? (y/n): ");
                    String jawab = input.nextLine();

                    if (jawab.equalsIgnoreCase("y")) {
                        System.out.print("Masukkan Kode: ");
                        String kodeInput = input.nextLine();
                        
                        Diskon dFound = catalog.cariDiskon(kodeInput);
                        
                        if (dFound != null && dFound.isAktif()) {
                            diskon = dFound;
                            // PERBAIKAN 1: Hapus "* 100" jika kamu menginput angka bulat (misal 50)
                            // PERBAIKAN 2: Gunakan (%.0f%%) agar tidak ada koma di persen
                            System.out.printf("Voucher VALID! %s (Diskon %.1f%%)\n", 
                                    dFound.getNamaDiskon(), 
                                    dFound.getPersen());
                        } else {
                            System.out.println("------------------------------------------------");
                            System.out.println("Maaf, kode voucher tidak ditemukan atau tidak aktif.");
                            System.out.print("Apakah Anda yakin ingin lanjut Checkout dengan HARGA NORMAL? (y/n): ");
                            String konfirmasi = input.nextLine();
                            
                            if (!konfirmasi.equalsIgnoreCase("y")) {
                                System.out.println("Transaksi Dibatalkan.");
                                break; 
                            }
                        }
                    }
                    // 3. Proses Checkout Akhir
                    Transaksi transaksi = customer.checkout(pembayaran, diskon);
                    Driver.getTransaksiList().add(transaksi);
                    System.out.printf("Checkout berhasil! Total yang harus dibayar: Rp %.0f\n", transaksi.getTotalAkhir());
                    break;

                case "5":
                    List<Transaksi> riwayat = Driver.getTransaksiList();
                    if (riwayat.isEmpty()) {
                        System.out.println("Belum ada transaksi.");
                    } else {
                        System.out.println("\n--- Riwayat Transaksi ---");
                        for (Transaksi t : riwayat) {
                            if (t.getKeranjang() != null) {
                                Invoice inv = new Invoice(t);
                                System.out.println(inv.generate());
                            }
                        }
                    }
                    break;

                case "6":
                    System.out.println("Logout berhasil.");
                    return;

                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }
}
