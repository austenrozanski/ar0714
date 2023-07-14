import exceptions.CheckoutException;
import models.RentalAgreement;
import models.Tool;
import models.ToolCharges;
import org.junit.jupiter.api.Test;
import services.CheckoutService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static helpers.CurrencyHelper.roundUpToNearestPenny;
import static org.junit.jupiter.api.Assertions.*;

public class CheckoutServiceTest {

    List<Tool> tools = Initializer.getTools();
    List<ToolCharges> toolCharges = Initializer.getToolCharges();
    CheckoutService checkoutService = new CheckoutService(tools, toolCharges);

    @Test
    public void testCheckout_invalidDiscount() throws Exception {
        // ARRANGE
        String toolCode = "JAKR";
        int rentalDays = 5;
        LocalDate checkoutDate = dateFromString("09/03/15");
        int discountPercent = 101; // Invalid discount percent

        // ACT
        Exception exception = assertThrows(CheckoutException.class, () ->
                checkoutService.checkout(toolCode, rentalDays, checkoutDate, discountPercent));

        // ASSERT
        String expectedMessage = "The discount percent must be between 0 and 100.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testCheckout_invalidRentalDays() throws Exception {
        // ARRANGE
        String toolCode = "JAKR";
        int rentalDays = 0;
        LocalDate checkoutDate = dateFromString("09/03/15");
        int discountPercent = 50; // Invalid discount percent

        // ACT
        Exception exception = assertThrows(CheckoutException.class, () ->
                checkoutService.checkout(toolCode, rentalDays, checkoutDate, discountPercent));

        // ASSERT
        String expectedMessage = "Rental days needs to be greater than 0.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    public void testCheckout_noChargesForIndependenceDay() throws Exception {
        // ARRANGE

        // Should be charged for both weekdays and weekends, excluding holidays
        String toolCode = "LADW";

        // Rental period goes from Friday to Sunday, with observed Independence Day being on Friday
        int rentalDays = 3;
        LocalDate checkoutDate = dateFromString("07/02/20");
        int discountPercent = 10;

        // ACT
        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, checkoutDate, discountPercent);

        // ASSERT
        int expectedChargeDays = 2;
        assertEquals(expectedChargeDays, rentalAgreement.getChargeDays());
        verifyCharges(expectedChargeDays, discountPercent, toolCode, rentalAgreement);
    }

    @Test
    public void testCheckout_noWeekendCharges() throws Exception {
        // ARRANGE

        // Should only be charged on weekdays, including holidays
        String toolCode = "CHNS";

        // Rental period goes from Friday to Tuesday, with observed Independence Day being on Friday
        int rentalDays = 5;
        LocalDate checkoutDate = dateFromString("07/02/15");
        int discountPercent = 25;

        // ACT
        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, checkoutDate, discountPercent);

        // ASSERT
        int expectedChargeDays = 3;
        assertEquals(expectedChargeDays, rentalAgreement.getChargeDays());
        verifyCharges(expectedChargeDays, discountPercent, toolCode, rentalAgreement);
    }

    @Test
    public void testCheckout_noChargesForMemorialDay() throws Exception {
        // ARRANGE

        // Should only be charged on weekdays, excluding holidays
        String toolCode = "JAKD";

        // Rental period goes from Friday to Wednesday, with Memorial Day being on Monday
        int rentalDays = 6;
        LocalDate checkoutDate = dateFromString("09/03/15");
        int discountPercent = 0;

        // ACT
        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, checkoutDate, discountPercent);

        // ASSERT
        int expectedChargeDays = 3;
        assertEquals(expectedChargeDays, rentalAgreement.getChargeDays());
        verifyCharges(expectedChargeDays, discountPercent, toolCode, rentalAgreement);
    }

    @Test
    public void testCheckout_noChargesForIndependenceDay2() throws Exception {
        // ARRANGE

        // Should only be charged on weekdays, excluding holidays
        String toolCode = "JAKR";

        // Rental period goes from Friday to Saturday, with observed Independence Day being on Friday
        int rentalDays = 9;
        LocalDate checkoutDate = dateFromString("07/02/15");
        int discountPercent = 0;

        // ACT
        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, checkoutDate, discountPercent);

        // ASSERT
        int expectedChargeDays = 5;
        assertEquals(expectedChargeDays, rentalAgreement.getChargeDays());
        verifyCharges(expectedChargeDays, discountPercent, toolCode, rentalAgreement);
    }

    @Test
    public void testCheckout_noChargesForIndependenceDay3() throws Exception {
        // ARRANGE

        // Should only be charged on weekdays, excluding holidays
        String toolCode = "JAKR";

        // Rental period goes from Friday to Monday, with observed Independence Day being on Friday
        int rentalDays = 4;
        LocalDate checkoutDate = dateFromString("07/02/20");
        int discountPercent = 50;

        // ACT
        RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDays, checkoutDate, discountPercent);

        // ASSERT
        int expectedChargeDays = 1;
        assertEquals(expectedChargeDays, rentalAgreement.getChargeDays());
        verifyCharges(expectedChargeDays, discountPercent, toolCode, rentalAgreement);
    }

    private void verifyCharges(int expectedChargeDays, int discountPercent, String toolCode, RentalAgreement rentalAgreement) throws Exception {
        BigDecimal dailyCharges = getDailyCharges(toolCode);
        BigDecimal expectedPreDiscountCharge = dailyCharges.multiply(BigDecimal.valueOf(expectedChargeDays));
        assertEquals(expectedPreDiscountCharge, rentalAgreement.getPreDiscountCharge());

        BigDecimal discountPercentAsDecimal = BigDecimal.valueOf(discountPercent / 100d);
        BigDecimal expectedDiscountAmount = roundUpToNearestPenny(expectedPreDiscountCharge.multiply(discountPercentAsDecimal));
        assertEquals(expectedDiscountAmount, rentalAgreement.getDiscountAmount());

        BigDecimal expectedFinalCharge = expectedPreDiscountCharge.subtract(expectedDiscountAmount);
        assertEquals(expectedFinalCharge, rentalAgreement.getFinalCharge());
    }

    private LocalDate dateFromString(String dateString)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy");
        LocalDate date = LocalDate.parse(dateString, formatter);
        return date;
    }

    private BigDecimal getDailyCharges(String toolCode) throws Exception {
        Tool tool = Initializer.getTools().stream()
                .filter((t) -> t.getCode().equals(toolCode))
                .findFirst()
                .orElseThrow();

        ToolCharges toolCharges = Initializer.getToolCharges().stream()
                .filter((t) -> t.getType() == tool.getType())
                .findFirst()
                .orElseThrow();

        return toolCharges.getDailyCharges();
    }
}
