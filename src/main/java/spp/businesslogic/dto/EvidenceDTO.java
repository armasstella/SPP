package spp.businesslogic.dto;

public class EvidenceDTO {

    private String metadata;
    private int grade;
    private InternDTO internDTO;

    public EvidenceDTO() {

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
}
