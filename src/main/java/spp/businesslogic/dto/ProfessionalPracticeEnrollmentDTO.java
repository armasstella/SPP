package spp.businesslogic.dto;

public class ProfessionalPracticeEnrollmentDTO {

    private String nrc;
    private String term;
    private InstructorDTO instructorDTO;
    private InternDTO internDTO;

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
}
