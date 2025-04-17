package himma.pendidikan.util;

import java.text.*;
import java.util.*;

public class Formatter {
    // Format: Rp. 1.000.000,00 / 1.000.000
    public static String separatorRupiah(double amount, boolean withRp) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');

        DecimalFormat formatter;
        if (withRp) {
            formatter = new DecimalFormat("#,##0.00", symbols);
            return "Rp. " + formatter.format(amount);
        } else {
            formatter = new DecimalFormat("#,##0", symbols);
            return formatter.format(amount);
        }
    }

    // Format: 23-Jan-2025
    public static String formatTanggalShortMonth(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }

    // Format: 23/01/2025
    public static String formatTanggalSlash(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(date);
    }
}
