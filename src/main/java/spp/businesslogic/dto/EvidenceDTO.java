package spp.businesslogic.dto;

public class EvidenceDTO {
    private int evidenceId;
    private String metadata;
    private int grade;
    private InternDTO internDTO;

    public EvidenceDTO() {

    }

    public int getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(int evidenceId) {
        this.evidenceId = evidenceId;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public InternDTO getInternDTO() {
        return internDTO;
    }

    public void setInternDTO(InternDTO internDTO) {
        this.internDTO = internDTO;
    }
}
