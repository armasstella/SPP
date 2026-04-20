package spp.businesslogic.dto;

public class InstructorDTO extends UserDTO {

    private String personalNumber;
    private String shift;

    public InstructorDTO() {
        super();
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }
}
