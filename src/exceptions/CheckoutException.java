package exceptions;

public class CheckoutException extends Exception {
    public CheckoutException(String errorMessage)
    {
        super(errorMessage);
    }
}