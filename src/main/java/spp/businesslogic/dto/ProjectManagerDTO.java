package spp.businesslogic.dto;

public class ProjectManagerDTO {
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String responsability;
    private String role;

    public ProjectManagerDTO() {

    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public void setResponsability(String responsability) {
        this.responsability = responsability;
    }

    public String getResponsability() {
        return responsability;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
