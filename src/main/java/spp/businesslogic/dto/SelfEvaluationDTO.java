package spp.businesslogic.dto;


public class SelfEvaluationDTO {
    private String studentName;
    private String studentNumber;
    private String linkedOrganization;
    private String department;
    private String projectManager;
    private String projectName;
    private int[] scores;
    private int finalScore;

    public SelfEvaluationDTO() {
        this.scores = new int[10];
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int[] getScores() {
        return scores;
    }

    public void setScores(int[] scores) {
        this.scores = scores;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

}