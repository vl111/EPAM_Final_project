package controller.exceptions;

public class MaximumPoolSizeException extends Exception {

    private String message;

    public MaximumPoolSizeException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
