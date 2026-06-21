package spp.businesslogic.enums;


public enum YesNoAllFilter {
    TODOS("Todos"),
    SI("Sí"),
    NO("No");

    private final String value;

    YesNoAllFilter(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;

    }

    @Override
    public String toString() {
        return value;

    }

}