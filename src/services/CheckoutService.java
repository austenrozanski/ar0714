package services;

import exceptions.CheckoutException;
import models.RentalAgreement;
import models.Tool;
import models.ToolCharges;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static helpers.CurrencyHelper.roundUpToNearestPenny;
import static helpers.DateHelper.isDateOnWeekend;

public class CheckoutService {
    private List<Tool> _tools;
    private List<ToolCharges> _toolCharges;
    private HolidayService _holidayService;

    public CheckoutService(List<Tool> tools, List<ToolCharges> toolCharges)
    {
        _tools = tools;
        _toolCharges = toolCharges;
        _holidayService = new HolidayService();
    }

    @Test
    public RentalAgreement checkout(String toolCode, int rentalDays, LocalDate checkoutDate, int discountPercent) throws CheckoutException {

        // Verify that the rental days and discount percent are valid
        if (rentalDays < 1) { throw new CheckoutException("Rental days needs to be greater than 0."); }
        if (discountPercent < 0 || discountPercent > 100) { throw new CheckoutException("The discount percent must be between 0 and 100."); }


        Tool tool = _tools.stream()
                .filter((t) -> t.getCode().equals(toolCode))
                .findFirst()
                .orElseThrow(() -> new CheckoutException("The tool code is not valid."));

        ToolCharges toolCharges = _toolCharges.stream()
                .filter((t) -> t.getType() == tool.getType())
                .findFirst()
                .orElseThrow(() -> new CheckoutException("The tool type is not valid."));

        LocalDate dueDate = checkoutDate.plusDays(rentalDays);
        int chargeDays = calculateChargeDays(checkoutDate, rentalDays, toolCharges);
        BigDecimal preDiscountCharge = toolCharges.getDailyCharges().multiply(BigDecimal.valueOf(chargeDays));

        BigDecimal discountPercentAsDecimal = BigDecimal.valueOf(discountPercent / 100d);
        BigDecimal discountAmount = roundUpToNearestPenny(preDiscountCharge.multiply(discountPercentAsDecimal));
        BigDecimal finalCharge = preDiscountCharge.subtract(discountAmount);

        RentalAgreement rentalAgreement = new RentalAgreement(toolCode, tool.getType(), tool.getBrand(),
                rentalDays, checkoutDate, dueDate, toolCharges.getDailyCharges(), chargeDays, preDiscountCharge,
                discountPercent, discountAmount, finalCharge);

        return rentalAgreement;
    }

    private int calculateChargeDays(LocalDate checkoutDate, int rentalDays, ToolCharges toolCharges)
    {
        int chargeDays = 0;

        // Loop through all days of the rental period
        for (int i = 1; i <= rentalDays; i++)
        {
            LocalDate dateToCheck = checkoutDate.plusDays(i);

            boolean isWeekend = isDateOnWeekend(dateToCheck);
            if (isWeekend && !toolCharges.isChargedOnWeekends())
                continue;

            boolean isWeekday = !isWeekend;
            if (isWeekday && !toolCharges.isChargedOnWeekdays())
                continue;

            boolean isHoliday = _holidayService.isObservedHoliday(dateToCheck);
            if (isHoliday && !toolCharges.isChargedOnHolidays())
                continue;

            chargeDays++;
        }

        return chargeDays;
    }
}
