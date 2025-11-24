/**
 * Kelas ItemKeranjang merepresentasikan satu baris item di keranjang 
 * yang mencakup objek Barang dan Jumlah yang dibeli.
 * @author Nama Anda
 */

// cspell:ignore fleurist ItemKeranjang getBarang getJumlah setJumlah getSubtotal tambahBarang hapusBarang idBarang getItems totalKeranjang kosongkan
package com.fleurist.model;

import java.util.ArrayList;
import java.util.List;

public class Keranjang {

    public static class ItemKeranjang {
        private Barang barang;
        private int jumlah;

        public ItemKeranjang(Barang barang, int jumlah) {
            this.barang = barang;
            this.jumlah = jumlah;
        }

        public Barang getBarang() { return barang; }
        public int getJumlah() { return jumlah; }
        public void setJumlah(int jumlah) { this.jumlah = jumlah; }

        public double getSubtotal() {
            return barang.getHarga() * jumlah;
        }
    }

    private List<ItemKeranjang> items = new ArrayList<>();

    public void tambahBarang(Barang barang, int jumlah) {
        for (ItemKeranjang item : items) {
            if (item.getBarang().getId().equals(barang.getId())) {
                item.setJumlah(item.getJumlah() + jumlah);
                return;
            }
        }
        items.add(new ItemKeranjang(barang, jumlah));
    }

    public void hapusBarang(String idBarang) {
        items.removeIf(item -> item.getBarang().getId().equals(idBarang));
    }

    public List<ItemKeranjang> getItems() {
        return items;
    }

    public double totalKeranjang() {
        double total = 0;
        for (ItemKeranjang item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void kosongkan() {
        items.clear();
    }

    @Override
    public String toString() {
        if (items.isEmpty()) {
            return "Keranjang kosong";
        }
        StringBuilder sb = new StringBuilder();
        for (ItemKeranjang item : items) {
            sb.append(item.getBarang().getNama())
              .append(" x ").append(item.getJumlah())
              .append(" = Rp ").append(item.getSubtotal())
              .append("\n");
        }
        sb.append("Total: Rp ").append(totalKeranjang());
        return sb.toString();
    }
}
