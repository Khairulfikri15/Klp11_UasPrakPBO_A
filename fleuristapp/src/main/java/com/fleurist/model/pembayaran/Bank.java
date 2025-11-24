// cspell:ignore fleurist bankName getNamaMetode Transfer prosesPembayaran simulasi
package com.fleurist.model.pembayaran;

public class Bank implements Pembayaran {

    private String bankName;

    public Bank() {
        this.bankName = "BCA";
    }

    public Bank(String bankName) {
        this.bankName = bankName;
    }

    @Override
    public String getNamaMetode() {
        return "Transfer Bank - " + bankName;
    }

    @Override
    public boolean prosesPembayaran(double total) {
        return true; // simulasi
    }
}
