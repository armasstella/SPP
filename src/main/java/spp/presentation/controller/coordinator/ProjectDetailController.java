package spp.presentation.controller.coordinator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.utils.view.alert.AlertHelper;


public class ProjectDetailController {

    @FXML private Label lblName;
    @FXML private Label lblDescription;
    @FXML private Label lblAvailability;
    @FXML private Label lblPlacesAvailable;
    @FXML private Label lblLinkedOrganization;
    @FXML private Label lblProjectManager;
    private boolean selectionAllowed = true;
    private boolean projectSelected = false;

    public void setProject(ProjectDTO project) {
        lblName.setText(project.getName());
        lblDescription.setText(project.getDescription());
        lblAvailability.setText(project.getAvailability());
        lblPlacesAvailable.setText(String.valueOf(project.getPlacesAvailable()));
        lblLinkedOrganization.setText(resolveOrganizationName(project.getLinkedOrganizationDTO()));
        lblProjectManager.setText(resolveProjectManagerName(project.getProjectManagerDTO()));

    }

    private String resolveOrganizationName(LinkedOrganizationDTO linkedOrganization) {
        if (linkedOrganization == null) {
            return "Sin organización vinculada";
        }
        return linkedOrganization.getName();

    }

    private String resolveProjectManagerName(ProjectManagerDTO projectManager) {
        if (projectManager == null) {
            return "Sin encargado";
        }
        return projectManager.getFirstName();

    }

    public void setSelectionAllowed(boolean selectionAllowed) {
        this.selectionAllowed = selectionAllowed;

    }

    public boolean isProjectSelected() {
        return projectSelected;

    }

    @FXML
    private void selectProject(ActionEvent event) {
        if (!selectionAllowed) {
            AlertHelper.showErrorMessage("Límite alcanzado", "Ya has elegido los 3 proyectos.");
            return;
        }

        boolean isConfirmed = AlertHelper.showConfirmation("Seleccionar proyecto",
                "¿Deseas seleccionar este proyecto?");
        if (isConfirmed) {
            projectSelected = true;
            closeWindow(event);
        }

    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) lblName.getScene().getWindow();
        currentStage.close();

    }

}