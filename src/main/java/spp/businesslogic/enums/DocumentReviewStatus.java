package spp.businesslogic.enums;

public enum DocumentReviewStatus {

    PENDING("PENDIENTE"),
    APPROVED("CALIFICADO");

    private final String value;

    DocumentReviewStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }

}
