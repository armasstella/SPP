package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectController implements Initializable {

    @FXML private TextField txtDescription;
    @FXML private TextField txtDisponibility;

    @FXML private Label lblStatus;

    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
    }

    @FXML
    private void setAllProject(ActionEvent event, ProjectDTO projectDTO) {
        projectDTO.setDescription(txtDescription.getText().trim());
        projectDTO.setDisponibility(Boolean.parseBoolean(txtDisponibility.getText().trim()));
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
                showSuccess("Practicante registrado correctamente.");
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
        if (txtDescription.getText().isBlank() ||
                txtDisponibility.getText().isBlank()){
            showError("Completa todos los campos obligatorios.");
            emptyFields = true;
        }
        return emptyFields;
    }

    private void clearInputFields() {
        txtDescription.clear();
        txtDisponibility.clear();
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