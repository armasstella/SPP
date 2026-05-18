package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectUpdateController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private TextField txtPlacesAvailable;
    @FXML private TextField txtLinkedOrganizationId;
    @FXML private TextField txtProjectManagerId;

    @FXML private TextField txtId;

    @FXML private Label lblStatus;

    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
    }

    @FXML
    private void updateProject(ActionEvent event) {

        clearStatus();
        if (validateUpdateInputs()) {
            return;
        }

        try {
            int projectId = Integer.parseInt(txtId.getText().trim());
            String newName = txtName.getText().trim();
            String newDescription = txtDescription.getText().trim();
            int newPlacesAvailable = Integer.parseInt(txtPlacesAvailable.getText().trim());
            int newLinkedOrganizationId = Integer.parseInt(txtLinkedOrganizationId.getText().trim());
            int newProjectManagerId = Integer.parseInt(txtProjectManagerId.getText().trim());

            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setId(projectId);
            projectDTO.setName(newName);
            projectDTO.setDescription(newDescription);
            projectDTO.setPlacesAvailable(newPlacesAvailable);

            LinkedOrganizationDTO newLinkedOrganizationDTO = new LinkedOrganizationDTO();
            newLinkedOrganizationDTO.setId(newLinkedOrganizationId);
            projectDTO.setLinkedOrganizationDTO(newLinkedOrganizationDTO);

            ProjectManagerDTO newProjectManagerDTO = new ProjectManagerDTO();
            newProjectManagerDTO.setId(newProjectManagerId);
            projectDTO.setProjectManagerDTO(newProjectManagerDTO);

            if (projectDAO.updateProject(projectDTO)) {
                showSuccess("Proyecto actualizado correctamente.");
                clearInputFields();
            }
        } catch (NumberFormatException e) {
            showError("El ID debe ser un número válido.");
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml", "Cancelar", event);
    }

    private boolean validateUpdateInputs() {
        boolean emptyFields = false;
        if (txtId.getText().isBlank()) {
            showError("Ingrese el ID del proyecto.");
            emptyFields = true;
        }
        return emptyFields;
    }

    private void clearInputFields() {
        txtId.clear();
    }

    private void showSuccess(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("success");
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("error");
    }

    private void clearStatus() {
        if (lblStatus != null) {
            lblStatus.setText("");
            lblStatus.getStyleClass().removeAll("error", "success");
        }
    }
}
