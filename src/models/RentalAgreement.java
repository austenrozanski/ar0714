package models;

import enums.ToolType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static helpers.CurrencyHelper.formatCurrency;

@Getter
@Setter
@AllArgsConstructor
public class RentalAgreement {
    private String toolCode;
    private ToolType toolType;
    private String toolBrand;
    private int rentalDays;
    private LocalDate checkoutDate;
    private LocalDate dueDate;
    private BigDecimal dailyRentalCharge;
    private int chargeDays;
    private BigDecimal preDiscountCharge;
    private int discountPercent;
    private BigDecimal discountAmount;
    private BigDecimal finalCharge;

    public String toString()
    {
        return  "Tool code: " + getToolCode() + "\n" +
                "Tool type: " + getToolType() + "\n" +
                "Tool brand: " + getToolBrand() + "\n" +
                "Rental days: " + getRentalDays() + "\n" +
                "Checkout date: " + formatDate(getCheckoutDate()) + "\n" +
                "Due date: " + formatDate(getDueDate()) + "\n" +
                "Daily rental charge: " + formatCurrency(getDailyRentalCharge()) + "\n" +
                "Charge days: " + getChargeDays() + "\n" +
                "Pre-Discount charge: " + formatCurrency(getPreDiscountCharge()) + "\n" +
                "Discount percent: " + getDiscountPercent() + "%\n" +
                "Discount amount: " + formatCurrency(getDiscountAmount()) + "\n" +
                "Final charge: " + formatCurrency(getFinalCharge());
    }

    private String formatDate(LocalDate date)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        return formatter.format(date);
    }
}
