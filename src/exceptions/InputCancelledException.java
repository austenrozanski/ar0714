package exceptions;

public class InputCancelledException extends Exception {
    public InputCancelledException(String errorMessage) {
        super(errorMessage);
    }
    public InputCancelledException() {
        super();
    }
}
