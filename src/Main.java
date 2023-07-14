import exceptions.CheckoutException;
import exceptions.InputCancelledException;
import models.RentalAgreement;
import models.Tool;
import models.ToolCharges;
import services.CheckoutService;

import java.time.LocalDate;
import java.util.List;

import static helpers.InputHelper.*;

public class Main {
    public static void main(String[] args) {

        // Initial setup
        List<Tool> tools = Initializer.getTools();
        List<ToolCharges> toolCharges = Initializer.getToolCharges();
        CheckoutService checkoutService = new CheckoutService(tools, toolCharges);

        outputTitle("Welcome to the ar01714 Tool Rental Checkout");

        boolean isCheckingOut = true;

        while (isCheckingOut)
        {
            try
            {
                String toolCode = getToolCodeFromInput("Enter the tool code: ", tools);
                int rentalDayCount = getNumberFromInput("Enter the number of days the tool is being rented for: ");
                int discountPercent = getNumberFromInput("Enter the discount percent: ");
                LocalDate checkoutDate = getDateFromInput("Enter the first date the tool will be checked out for (MM/DD/YY): ");

                outputSeparator();
                System.out.println("\nProcessing checkout...");

                RentalAgreement rentalAgreement = checkoutService.checkout(toolCode, rentalDayCount, checkoutDate, discountPercent);

                System.out.println("Checkout completed!\n");
                outputTitle("Rental Agreement");
                System.out.println(rentalAgreement.toString());
            }
            catch(InputCancelledException e)
            {
                System.out.println("Checkout cancelled.");
            }
            catch(CheckoutException e)
            {
                System.out.println("An error occurred during checkout. " + e.getMessage());
            }
            catch (Exception e)
            {
                System.out.println("An error occurred while processing the checkout.");
            }

            try
            {
                isCheckingOut = getBooleanFromInput("Would you like to checkout another item? (y/n): ");
            }
            catch (Exception e)
            {
                isCheckingOut = false;
            }
        }

    }

    public static void outputTitle(String title)
    {
        outputSeparator();
        System.out.println(title);
        outputSeparator();
    }

    public static void outputSeparator()
    {
        String separator = "---------------------------------------------";
        System.out.println(separator);
    }
}