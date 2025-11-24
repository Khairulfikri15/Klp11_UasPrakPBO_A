// cspell:ignore fleurist getNamaMetode prosesPembayaran
package com.fleurist.model.pembayaran;

public interface Pembayaran {
    String getNamaMetode();
    boolean prosesPembayaran(double total);
}
