package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ProjectDAO;
import spp.utils.logger.AppLogger;

import java.io.IOException;
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
        if (!validateAddFields()) {
            return;
        }

        ProjectDTO projectDTO = new ProjectDTO();
        setAllProject(event, projectDTO);

        try {
            if (projectDAO.addProject(projectDTO)) {
                showSuccess("Practicante registrado correctamente.");
                clearAddFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        loadView("/spp/presentation/view/CoordinatorMenuView.fxml", "Cancelar", event);
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root, 480, 520));
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            AppLogger.logError(e);
        }
    }

    private boolean validateAddFields() {
        boolean validFields = true;
        if (txtDescription.getText().isBlank() ||
                txtDisponibility.getText().isBlank()){
            showError("Completa todos los campos obligatorios.");
            validFields = false;
        }
        return validFields;
    }

    private void clearAddFields() {
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