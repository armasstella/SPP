package spp.businesslogic.dto;


public class PrioritizedProjectDTO {

    private final int ID_PROJECT;
    private final String PROJECT_NAME;
    private final int PRIORITY_LEVEL;

    public PrioritizedProjectDTO(int ID_PROJECT, String PROJECT_NAME, int PRIORITY_LEVEL) {
        this.ID_PROJECT = ID_PROJECT;
        this.PROJECT_NAME = PROJECT_NAME;
        this.PRIORITY_LEVEL = PRIORITY_LEVEL;
    }

    public int getIdProject() {
        return ID_PROJECT;
    }

    public String getProjectName() {
        return PROJECT_NAME;
    }

    public int getPriorityLevel() {
        return PRIORITY_LEVEL;
    }

    @Override
    public String toString() {
        return "[Nivel de Prioridad" + PRIORITY_LEVEL + "] " + PROJECT_NAME + " (" + ID_PROJECT + ")";
    }

}
