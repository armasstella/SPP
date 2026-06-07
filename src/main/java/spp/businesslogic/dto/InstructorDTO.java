package spp.businesslogic.dto;


public class InstructorDTO extends UserDTO {

    private int id;
    private String personalNumber;
    private String shift;

    public InstructorDTO() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
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

    @Override
    public String toString() {
        return personalNumber + " | " + getFirstName();
    }

}
