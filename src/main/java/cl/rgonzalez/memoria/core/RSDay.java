package cl.rgonzalez.memoria.core;

import java.util.Optional;

/**
 *
 */
public enum RSDay {

    LUNES(1, "Lunes"),
    MARTES(2, "Martes"),
    MIERCOLES(3, "Miércoles"),
    JUEVES(4, "Jueves"),
    VIERNES(5, "Viernes"),
    SABADO(6, "Sabado");

    private int value;
    private String name;

    RSDay(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Optional<RSDay> getById(int day) {
        if (day >= 1 && day <= 6) {
            return Optional.of(values()[day - 1]);
        }
        return Optional.empty();
    }
}
