package cl.rgonzalez.memoria.core;

public enum RSPayStyle {
    EFECTIVO("Efectivo"),
    CREDITO("Credito"),
    DEBITO("Debito"),
    OTRO("Otro");

    private String name;

    private RSPayStyle(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RSPayStyle getOrNull(String strPayStyle) {
        for (RSPayStyle style : values()) {
            if (style.equals(strPayStyle)) {
                return style;
            }
        }
        return null;
    }
}
