package spp.businesslogic.dto;


public class IndicatorReportDTO {


    private int totalStudents;
    private int totalMale;
    private int totalFemale;
    private int totalIndigenous;
    private int totalNonIndigenous;
    private String generationDate;
    private IndicatorFilterDTO appliedFilters;

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalMale() {
        return totalMale;
    }

    public void setTotalMale(int totalMale) {
        this.totalMale = totalMale;
    }

    public int getTotalFemale() {
        return totalFemale;
    }

    public void setTotalFemale(int totalFemale) {
        this.totalFemale = totalFemale;
    }

    public int getTotalIndigenous() {
        return totalIndigenous;
    }

    public void setTotalIndigenous(int totalIndigenous) {
        this.totalIndigenous = totalIndigenous;
    }

    public int getTotalNonIndigenous() {
        return totalNonIndigenous;
    }

    public void setTotalNonIndigenous(int totalNonIndigenous) {
        this.totalNonIndigenous = totalNonIndigenous;
    }

    public String getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(String generationDate) {
        this.generationDate = generationDate;
    }

    public IndicatorFilterDTO getAppliedFilters() {
        return appliedFilters;
    }

    public void setAppliedFilters(IndicatorFilterDTO appliedFilters) {
        this.appliedFilters = appliedFilters;
    }
}
