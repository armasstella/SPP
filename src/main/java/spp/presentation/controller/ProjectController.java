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
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private TextField txtPlacesAvailable;
    @FXML private TextField txtProjectManager;
    @FXML private TextField txtLinkedOrganization;

    @FXML private Label lblStatus;

    ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
    LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();

    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
    }

    @FXML
    private void setAllProject(ActionEvent event, ProjectDTO projectDTO) {
        projectDTO.setName(txtName.getText().trim());
        projectDTO.setDescription(txtDescription.getText().trim());
        projectDTO.setPlacesAvailable(Integer.parseInt(txtPlacesAvailable.getText().trim()));
        projectManagerDTO.setId(Integer.parseInt(txtProjectManager.getText().trim()));
        projectDTO.setProjectManagerDTO(projectManagerDTO);
        linkedOrganizationDTO.setId(Integer.parseInt(txtLinkedOrganization.getText().trim()));
        projectDTO.setLinkedOrganizationDTO(linkedOrganizationDTO);
    }

    @FXML
    private void saveProject(ActionEvent event) {

        clearStatus();
        if (validateRegistrationInputs()) {
            return;
        }

        ProjectDTO projectDTO = new ProjectDTO();
        setAllProject(event, projectDTO);

        try {
            if (projectDAO.addProject(projectDTO)) {
                showSuccess("Proyecto registrado correctamente.");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml", "Cancelar", event);
    }

    private boolean validateRegistrationInputs() {
        boolean emptyFields = false;
        if (txtName.getText().isBlank() ||
                txtDescription.getText().isBlank() ||
                txtPlacesAvailable.getText().isBlank() ||
                txtProjectManager.getText().isBlank() ||
                txtLinkedOrganization.getText().isBlank()) {
            showError("Completa todos los campos obligatorios.");
            emptyFields = true;
        }
        return emptyFields;
    }

    private void clearInputFields() {
        txtName.clear();
        txtDescription.clear();
        txtPlacesAvailable.clear();
        txtProjectManager.clear();
        txtLinkedOrganization.clear();
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