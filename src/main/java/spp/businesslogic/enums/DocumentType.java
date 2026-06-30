package spp.businesslogic.enums;

public enum DocumentType {
    CLASS_SCHEDULE("Horario de clases"),
    ACTIVITIES_SCHEDULE("Calendarización de actividades"),
    PARTIAL_REPORT("Reporte Parcial"),
    MONTHLY_REPORT("Reporte Mensual"),
    ACTIVITIES_PLAN("Plan de Actividades"),
    SELF_EVALUATION("Autoevaluación"),
    EVALUATION_LINKED_ORGANIZATION("Evaluación de Organización Vinculada"),
    FINAL_REPORT("Reporte Final"),
    PSP("PSP"),
    INDICATOR_REPORT("Reporte de Indicadores"),
    PRESENTATION_TEMPLATE("Plantilla de Presentación"),
    RELEASE_LETTER("Carta de Liberación");

    private final String value;

    DocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;

    }
}