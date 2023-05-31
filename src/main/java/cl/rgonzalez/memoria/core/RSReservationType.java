package cl.rgonzalez.memoria.core;

public enum RSReservationType {

    SEMESTRAL(1, "Semestral"), EVENTUAL(2, "Eventual");

    private int value;
    private String name;

    RSReservationType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
