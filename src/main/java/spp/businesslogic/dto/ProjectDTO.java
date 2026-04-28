package spp.businesslogic.dto;

public class ProjectDTO {

    private int id;
    private String description;
    private boolean disponibility;
    private ProjectManagerDTO projectManagerDTO;
    private LinkedOrganizationDTO linkedOrganizationDTO;

    public ProjectDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setProjectManagerDTO(ProjectManagerDTO projectManagerDTO) {
        this.projectManagerDTO = projectManagerDTO;
    }

    public ProjectManagerDTO getProjectManagerDTO() {
        return projectManagerDTO;
    }

    public void setLinkedOrganizationDTO(LinkedOrganizationDTO linkedOrganizationDTO) {
        this.linkedOrganizationDTO = linkedOrganizationDTO;
    }

    public LinkedOrganizationDTO getLinkedOrganizationDTO() {
        return linkedOrganizationDTO;
    }
}
