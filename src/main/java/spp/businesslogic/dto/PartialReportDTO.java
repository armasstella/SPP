package spp.businesslogic.dto;

import java.util.ArrayList;
import java.util.List;

public class PartialReportDTO {

    private String career;
    private String nrc;
    private String professorName;
    private String schoolPeriod;
    private String studentName;
    private String studentNumber;
    private String linkedOrganization;
    private String projectName;
    private String coveredHours;
    private String reportDate;
    private String reportNumber;
    private String reportPeriod;
    private String objective;
    private String methodology;
    private String results;
    private String observations;
    private List<PartialReportActivityDTO> activities = new ArrayList<PartialReportActivityDTO>();

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getSchoolPeriod() {
        return schoolPeriod;
    }

    public void setSchoolPeriod(String schoolPeriod) {
        this.schoolPeriod = schoolPeriod;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getLinkedOrganization() {
        return linkedOrganization;
    }

    public void setLinkedOrganization(String linkedOrganization) {
        this.linkedOrganization = linkedOrganization;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getCoveredHours() {
        return coveredHours;
    }

    public void setCoveredHours(String coveredHours) {
        this.coveredHours = coveredHours;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportNumber() {
        return reportNumber;
    }

    public void setReportNumber(String reportNumber) {
        this.reportNumber = reportNumber;
    }

    public String getReportPeriod() {
        return reportPeriod;
    }

    public void setReportPeriod(String reportPeriod) {
        this.reportPeriod = reportPeriod;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public String getMethodology() {
        return methodology;
    }

    public void setMethodology(String methodology) {
        this.methodology = methodology;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public List<PartialReportActivityDTO> getActivities() {
        return activities;
    }

    public void setActivities(List<PartialReportActivityDTO> activities) {
        this.activities = activities;
    }
}
