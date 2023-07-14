package helpers;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyHelper {

    public static BigDecimal roundUpToNearestPenny(BigDecimal amount)
    {
        return amount.setScale(2, RoundingMode.CEILING);
    }

    public static String formatCurrency(BigDecimal amount)
    {
        BigDecimal roundedAmount = roundUpToNearestPenny(amount);
        return "$" + String.format("%,.2f", roundedAmount);
    }
}
