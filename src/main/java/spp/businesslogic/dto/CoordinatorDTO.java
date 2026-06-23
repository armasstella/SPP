package spp.businesslogic.dto;

public class CoordinatorDTO extends UserDTO {

    private String personalNumber;

    public CoordinatorDTO() {
        super();
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    @Override
    public String toString() {
        return personalNumber + " | " + super.getFullName();
    }
}
