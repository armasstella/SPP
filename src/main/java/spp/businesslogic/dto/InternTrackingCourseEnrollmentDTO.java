package spp.businesslogic.dto;

public class InternTrackingCourseEnrollmentDTO {
    private int internId;
    private String studentNumber;
    private String fullName;
    private String email;
    private String nameProjectAssigned;
    private String addressProject;
    private int completedHours;
    private String enrollmentPhase;

    public InternTrackingCourseEnrollmentDTO() {
    }

    public int getInternId() {
        return internId;
    }

    public void setInternId(int internId) {
        this.internId = internId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNameProjectAssigned() {
        return nameProjectAssigned;
    }

    public void setNameProjectAssigned(String nameProjectAssigned) {
        this.nameProjectAssigned = nameProjectAssigned;
    }

    public String getAddressProject() {
        return addressProject;
    }

    public void setAddressProject(String addressProject) {
        this.addressProject = addressProject;
    }

    public int getCompletedHours() {
        return completedHours;
    }

    public void setCompletedHours(int completedHours) {
        this.completedHours = completedHours;
    }

    public String getEnrollmentPhase() {
        return enrollmentPhase;
    }

    public void setEnrollmentPhase(String enrollmentPhase) {
        this.enrollmentPhase = enrollmentPhase;
    }
}
