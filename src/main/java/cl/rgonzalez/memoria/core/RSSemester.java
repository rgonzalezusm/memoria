package cl.rgonzalez.memoria.core;

import java.util.Optional;

/**
 *
 */
public enum RSSemester {

    FIRST(1, "1er Semestre"),
    SECOND(2, "2do Semestre");

    private int value;
    private String name;

    RSSemester(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static Optional<RSSemester> getById(int id) {
        if (id >= 1 && id <= 2) {
            return Optional.of(values()[id - 1]);
        }
        return Optional.empty();
    }
}
