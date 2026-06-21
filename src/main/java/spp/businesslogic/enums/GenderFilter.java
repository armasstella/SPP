package spp.businesslogic.enums;


public enum GenderFilter {
    TODOS("Todos"),
    MASCULINO("Masculino"),
    FEMENINO("Femenino");

    private final String value;

    GenderFilter(String value) {
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