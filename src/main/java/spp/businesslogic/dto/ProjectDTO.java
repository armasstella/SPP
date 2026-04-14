package spp.businesslogic.dto;

public class ProjectDTO {

    private String description;
    private boolean disponibility;

    public ProjectDTO() {

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDisponibility(boolean disponibility) {
        this.disponibility = disponibility;
    }

    public boolean getDisponibility() {
        return disponibility;
    }


}
