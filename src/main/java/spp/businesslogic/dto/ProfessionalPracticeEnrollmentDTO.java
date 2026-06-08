package spp.businesslogic.dto;


public class ProfessionalPracticeEnrollmentDTO {

    private CourseDTO courseDTO;
    private InternDTO internDTO;
    private int finalGrade;
    private ProjectDTO projectDTO;
    private int coveredHours;


    public ProfessionalPracticeEnrollmentDTO() {

    }

    public void setInternDTO(InternDTO internDTO) {
        this.internDTO = internDTO;
    }

    public InternDTO getInternDTO() {
        return internDTO;
    }

    public void setFinalGrade(int finalGrade) {
        this.finalGrade = finalGrade;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    public void setProjectDTO(ProjectDTO projectDTO) {
        this.projectDTO = projectDTO;
    }

    public ProjectDTO getProjectDTO() {
        return projectDTO;
    }

    public void setCoveredHours(int coveredHours) {
        this.coveredHours = coveredHours;
    }

    public int getCoveredHours() {
        return coveredHours;
    }

    public void setCourseDTO(CourseDTO courseDTO) {
        this.courseDTO = courseDTO;
    }

    public CourseDTO getCourseDTO() {
        return courseDTO;
    }
}
