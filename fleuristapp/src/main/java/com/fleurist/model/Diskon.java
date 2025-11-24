// cspell:ignore fleurist idDiskon namaDiskon persen contoh getIdDiskon setIdDiskon getNamaDiskon setNamaDiskon getPersen setPersen isAktif setAktif Mengaktifkan aktifkan Menonaktifkan nonaktifkan AKTIF NONAKTIF
package com.fleurist.model;

public class Diskon {

    private String idDiskon;
    private String namaDiskon;
    private double persen;     // contoh: 10 = 10%
    private boolean aktif;

    public Diskon(String idDiskon, String namaDiskon, double persen, boolean aktif) {
        this.idDiskon = idDiskon;
        this.namaDiskon = namaDiskon;
        this.persen = persen;
        this.aktif = aktif;
    }

    // Getter & Setter
    public String getIdDiskon() {
        return idDiskon;
    }

    public void setIdDiskon(String idDiskon) {
        this.idDiskon = idDiskon;
    }

    public String getNamaDiskon() {
        return namaDiskon;
    }

    public void setNamaDiskon(String namaDiskon) {
        this.namaDiskon = namaDiskon;
    }

    public double getPersen() {
        return persen;
    }

    public void setPersen(double persen) {
        this.persen = persen;
    }

    public boolean isAktif() {
        return aktif;
    }

    public void setAktif(boolean aktif) {
        this.aktif = aktif;
    }

    // Mengaktifkan diskon
    public void aktifkan() {
        this.aktif = true;
    }

    // Menonaktifkan diskon
    public void nonaktifkan() {
        this.aktif = false;
    }

    @Override
    public String toString() {
        return idDiskon + " - " + namaDiskon + " (" + persen + "%) | "
                + (aktif ? "AKTIF" : "NONAKTIF");
    }
}
