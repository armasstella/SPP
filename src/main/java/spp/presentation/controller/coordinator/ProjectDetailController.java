package spp.presentation.controller.coordinator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
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

        String placesAvailableString = String.valueOf(project.getPlacesAvailable());
        lblPlacesAvailable.setText(placesAvailableString);

        LinkedOrganizationDTO linkedOrganization = project.getLinkedOrganizationDTO();
        String resolvedOrganizationName = resolveOrganizationName(linkedOrganization);
        lblLinkedOrganization.setText(resolvedOrganizationName);

        ProjectManagerDTO projectManager = project.getProjectManagerDTO();
        String resolvedManagerName = resolveProjectManagerName(projectManager);
        lblProjectManager.setText(resolvedManagerName);
    }

    private String resolveOrganizationName(LinkedOrganizationDTO linkedOrganization) {
        String organizationName = "Sin organización vinculada";

        if (linkedOrganization != null) {
            organizationName = linkedOrganization.getName();
        }

        return organizationName;
    }

    private String resolveProjectManagerName(ProjectManagerDTO projectManager) {
        String managerName = "Sin encargado";

        if (projectManager != null) {
            managerName = projectManager.getFirstName();
        }

        return managerName;
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
        } else {
            boolean isConfirmed = AlertHelper.showConfirmation(
                    "Seleccionar proyecto",
                    "¿Deseas seleccionar este proyecto?"
            );

            if (isConfirmed) {
                projectSelected = true;
                closeWindow(event);
            }
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        Scene currentScene = lblName.getScene();
        Window currentWindow = currentScene.getWindow();
        Stage currentStage = (Stage) currentWindow;

        currentStage.close();
    }
}