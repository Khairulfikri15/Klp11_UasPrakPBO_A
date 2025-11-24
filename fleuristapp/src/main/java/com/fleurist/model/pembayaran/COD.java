/**
 * Kelas COD (Cash on Delivery) adalah salah satu implementasi Polymorphism 
 * dari interface Pembayaran.
 * @author Kelompok 11
 */
package com.fleurist.model.pembayaran;

public class COD implements Pembayaran {

    @Override
    public String getNamaMetode() {
        return "Cash On Delivery";
    }

    @Override
    public boolean prosesPembayaran(double total) {
        return true; // simulasi
    }
}
