package com.zpedroo.rankuplevels.utils.formatter;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.file.FileConfiguration;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.*;

import static com.zpedroo.rankuplevels.utils.config.Settings.PROGRESS_DIGITS;

public class NumberFormatter {

    private static NumberFormatter instance;
    public static NumberFormatter getInstance() { return instance; }

    private final BigInteger THOUSAND = BigInteger.valueOf(1000);
    private final NavigableMap<BigInteger, String> FORMATS = new TreeMap<>();
    private final List<String> NAMES = new LinkedList<>();

    public NumberFormatter(FileConfiguration file) {
        instance = this;
        NAMES.addAll(file.getStringList("Number-Formatter"));

        for (int i = 0; i < NAMES.size(); i++) {
            FORMATS.put(THOUSAND.pow(i+1), NAMES.get(i));
        }
    }

    public BigInteger filter(String str) {
        String onlyNumbers = str.replaceAll("[^0-9]+", "");
        if (onlyNumbers.isEmpty()) return BigInteger.ZERO; // invalid amount

        BigInteger number = new BigInteger(onlyNumbers);

        String onlyLetters = str.replaceAll("[^A-Za-z]+", "");

        int i = -1;
        if (NAMES.contains(onlyLetters)) {
            for (String format : NAMES) {
                ++i;

                if (StringUtils.equals(format, onlyLetters)) break;
            }
        }

        if (i != -1) number = number.multiply(THOUSAND.pow(i + 1));

        return number;
    }

    public String format(double number) {
        return format(new BigInteger(String.format("%.0f", number)));
    }

    public String format(BigInteger number) {
        Map.Entry<BigInteger, String> entry = FORMATS.floorEntry(number);
        if (entry == null) return number.toString();

        BigInteger key = entry.getKey();
        BigInteger divide = key.divide(THOUSAND);
        BigInteger divide1 = number.divide(divide);
        float f = divide1.floatValue() / 1000f;
        float rounded = ((int)(f * 100))/100f;

        if (rounded % 1 == 0) return ((int) rounded) + "" + entry.getValue();

        return rounded + "" + entry.getValue();
    }

    public String formatDecimal(double number) {
        return formatDecimal(number, PROGRESS_DIGITS);
    }

    public String formatDecimal(double number, int digitsAmount) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < digitsAmount; ++i) {
            builder.append("#");
        }

        DecimalFormat formatter = new DecimalFormat("##." + builder);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(number);
    }

    public String formatThousand(double number) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

        symbols.setGroupingSeparator('.');
        formatter.setDecimalFormatSymbols(symbols);
        return formatter.format(number);
    }
}