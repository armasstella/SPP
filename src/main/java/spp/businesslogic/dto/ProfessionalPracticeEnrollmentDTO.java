package spp.businesslogic.dto;

public class ProfessionalPracticeEnrollmentDTO {

    private String nrc;
    private String term;
    private InstructorDTO instructorDTO;
    private InternDTO internDTO;
    private int finalGrade;
    private ProjectDTO projectDTO;
    private int coveredHours;


    public ProfessionalPracticeEnrollmentDTO() {

    }

    public String getNrc() {
        return nrc;
    }

    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setInstructorDTO(InstructorDTO instructorDTO) {
        this.instructorDTO = instructorDTO;
    }

    public InstructorDTO getInstructorDTO() {
        return instructorDTO;
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
}
