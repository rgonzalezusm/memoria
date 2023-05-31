package cl.rgonzalez.memoria.core;

import cl.rgonzalez.memoria.exceptions.RSException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class RSCoreUtils {

    private RSCoreUtils() {
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

    public static RSSemester detectSemester(LocalDate date) {
        Set<Integer> firstSemester = new HashSet<>(Arrays.asList(3, 4, 5, 6, 7));
        Set<Integer> secondSemester = new HashSet<>(Arrays.asList(8, 9, 10, 11, 12));

        int dayOfMonth = date.getDayOfMonth();
        if (firstSemester.contains(dayOfMonth)) {
            return RSSemester.FIRST;
        } else if (secondSemester.contains(dayOfMonth)) {
            return RSSemester.SECOND;
        } else {
            return null;
        }
    }
}

