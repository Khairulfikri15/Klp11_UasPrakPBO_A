package com.fleurist.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class CurrencyUtil {

    private static final DecimalFormat format;

    static {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        format = new DecimalFormat("Rp #,###", symbols);
    }

    public static String toRupiah(double angka) {
        return format.format(angka);
    }
}
