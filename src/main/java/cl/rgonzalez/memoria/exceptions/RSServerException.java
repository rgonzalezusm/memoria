package cl.rgonzalez.memoria.exceptions;

public class RSServerException extends RuntimeException {

    public RSServerException() {
    }

    public RSServerException(String message) {
        super(message);
    }

    public RSServerException(String message, Throwable cause) {
        super(message, cause);
    }
}
