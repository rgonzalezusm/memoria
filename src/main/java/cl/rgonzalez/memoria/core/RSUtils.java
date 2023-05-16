package cl.rgonzalez.memoria.core;

import cl.rgonzalez.memoria.exceptions.RSException;

import java.io.IOException;

public final class RSUtils {

    private RSUtils() {
    }

    public static void verify(boolean condition, String errMessage) throws RSException {
        if (condition) {
            throw new RSException(errMessage);
        }
    }

    public static void verifyIOEx(boolean condition, String errMessage) throws IOException {
        if (condition) {
            throw new IOException(errMessage);
        }
    }

    public static Integer parseInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
