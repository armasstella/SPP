package spp.businesslogic.dto;

public class ProjectDTO {

    private static final int MAX_LENGTH_NAME = 150;
    private static final int MAX_LENGTH_DESCRIPTION = 500;
    private static final int MAX_LENGTH_AVAILABILITY = 20;
    private static final int MIN_PLACES_AVAILABLE = 0;

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
        validateNotEmpty(name, "Nombre del Proyecto");
        validateStringLength(name, MAX_LENGTH_NAME, "Nombre del Proyecto");
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        validateNotEmpty(description, "Descripción");
        validateStringLength(description, MAX_LENGTH_DESCRIPTION, "Descripción");
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setAvailability(String availability) {
        if (availability != null) {
            validateStringLength(availability, MAX_LENGTH_AVAILABILITY, "Disponibilidad");
        }
        this.availability = availability;
    }

    public String getAvailability() {
        return availability;
    }

    public void setProjectManagerDTO(ProjectManagerDTO projectManagerDTO) {
        validateNotNull(projectManagerDTO, "Encargado de Proyecto");
        this.projectManagerDTO = projectManagerDTO;
    }

    public ProjectManagerDTO getProjectManagerDTO() {
        return projectManagerDTO;
    }

    public void setLinkedOrganizationDTO(LinkedOrganizationDTO linkedOrganizationDTO) {
        validateNotNull(linkedOrganizationDTO, "Organización Vinculada");
        this.linkedOrganizationDTO = linkedOrganizationDTO;
    }

    public LinkedOrganizationDTO getLinkedOrganizationDTO() {
        return linkedOrganizationDTO;
    }

    public void setPlacesAvailable(int placesAvailable) {
        if (placesAvailable < MIN_PLACES_AVAILABLE) {
            throw new IllegalArgumentException("El cupo no puede ser un número negativo.");
        }
        this.placesAvailable = placesAvailable;
    }

    public int getPlacesAvailable() {
        return placesAvailable;
    }

    protected void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe estar vacío.");
        }
    }

    protected void validateStringLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe exceder " + maxLength + " caracteres.");
        }
    }

    protected void validateNotNull(Object object, String fieldName) {
        if (object == null) {
            throw new IllegalArgumentException("Debe asignar un " + fieldName + " válido.");
        }
    }
}