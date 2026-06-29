package spp.businesslogic.enums;

public enum ActivityType {
    MONTHLY("MENSUAL"),
    FINAL("FINAL");

    private final String value;

    ActivityType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;

    }

}
