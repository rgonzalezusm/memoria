package cl.rgonzalez.memoria.exceptions;

public class RSException extends Exception {

    public RSException() {
    }

    public RSException(String message) {
        super(message);
    }

    public RSException(String message, Throwable cause) {
        super(message, cause);
    }
}
