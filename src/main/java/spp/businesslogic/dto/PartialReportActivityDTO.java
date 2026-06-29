package spp.businesslogic.dto;

public class PartialReportActivityDTO implements Comparable<PartialReportActivityDTO> {

    private String name;
    private String description;
    private int weekNumber;
    private String plannedTime;
    private String realTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public void setWeekNumber(int weekNumber) {
        this.weekNumber = weekNumber;
    }

    public String getPlannedTime() {
        return plannedTime;
    }

    public void setPlannedTime(String plannedTime) {
        this.plannedTime = plannedTime;
    }

    public String getRealTime() {
        return realTime;
    }

    public void setRealTime(String realTime) {
        this.realTime = realTime;
    }

    @Override
    public int compareTo(PartialReportActivityDTO otherActivity) {
        return Integer.compare(this.weekNumber, otherActivity.weekNumber);
    }

    @Override
    public String toString() {
        return "Semana " + weekNumber + " - " + name
                + " (Planeado: " + plannedTime + ", Real: " + realTime + ")";
    }
}
