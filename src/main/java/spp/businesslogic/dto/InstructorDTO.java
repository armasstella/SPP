package spp.businesslogic.dto;

public class InstructorDTO extends UserDTO {

    private int id;
    private String personalNumber;
    private String shift;

    public InstructorDTO() {
        super();
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public int getId() {
        return id;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getShift() {
        return shift;
    }

    @Override
    public String toString() {
        return personalNumber + " | " + getFullName();
    }
}
