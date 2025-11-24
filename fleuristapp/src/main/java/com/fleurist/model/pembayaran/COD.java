// cspell:ignore fleurist getNamaMetode Delivery prosesPembayaran simulasi
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
