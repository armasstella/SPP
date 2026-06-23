package spp.businesslogic.dto;

public class ProjectDTO {

    private int id;
    private String name;
    private String description;
    private String availability;
    private ProjectManagerDTO projectManagerDTO;
    private LinkedOrganizationDTO linkedOrganizationDTO;
    private int placesAvailable;

    public ProjectDTO() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
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

    public void setPlacesAvailable(int placesAvailable) {
        this.placesAvailable = placesAvailable;
    }

    public int getPlacesAvailable() {
        return placesAvailable;
    }
}
