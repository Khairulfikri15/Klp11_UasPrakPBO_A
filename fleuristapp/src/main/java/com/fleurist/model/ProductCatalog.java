/**
 * Kelas ProductCatalog berfungsi sebagai database lokal 
 * yang menyimpan list semua Barang dan Diskon.
 * @author Kelompok 11
 */

package com.fleurist.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductCatalog {

    // === ATTRIBUTES ===
    private List<Barang> barangList = new ArrayList<>();
    private List<Diskon> diskonList = new ArrayList<>();

    // === FILTER METHODS ===
    public Set<String> getJenisUnik() {
        return barangList.stream()
                .map(Barang::getJenis)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<String> getTipeUnik() {
        return barangList.stream()
                .map(Barang::getTipeProduk)
                .collect(Collectors.toCollection(HashSet::new));
    }

    public List<Barang> getBarangByFilter(String jenis, String tipe) {
        return barangList.stream()
                .filter(b -> jenis == null || jenis.isEmpty() || b.getJenis().equalsIgnoreCase(jenis))
                .filter(b -> tipe == null || tipe.isEmpty() || b.getTipeProduk().equalsIgnoreCase(tipe))
                .collect(Collectors.toList());
    }

    // === BARANG METHODS ===
    public void tambahBarang(Barang barang) {
        barangList.add(barang);
    }

    public void editBarang(String id, Barang barangBaru) {
        for (int i = 0; i < barangList.size(); i++) {
            if (barangList.get(i).getId().equals(id)) {
                barangList.set(i, barangBaru);
                return;
            }
        }
    }

    public void hapusBarang(String id) {
        barangList.removeIf(b -> b.getId().equals(id));
    }

    public Barang cariBarang(String id) {
        for (Barang b : barangList) {
            if (b.getId().equals(id)) {
                return b;
            }
        }
        return null;
    }

    public List<Barang> getBarangList() {
        return barangList;
    }

    // === DISKON METHODS ===
    public List<Diskon> getDiskonList() {
        return diskonList;
    }

    public void tambahDiskon(Diskon diskon) {
        diskonList.add(diskon);
    }

    public Diskon cariDiskon(String idDiskon) {
        for (Diskon d : diskonList) {
            if (d.getIdDiskon().equalsIgnoreCase(idDiskon)) {
                return d;
            }
        }
        return null;
    }

    public void hapusDiskon(String idDiskon) {
        diskonList.removeIf(d -> d.getIdDiskon().equals(idDiskon));
    }
}
