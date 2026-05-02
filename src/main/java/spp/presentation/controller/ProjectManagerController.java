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
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ProjectManagerDAO;
import spp.utils.logger.AppLogger;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProjectManagerController implements Initializable {
    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtResponsability;
    @FXML private TextField txtRole;
    @FXML private TextField txtPhoneNumber;

    @FXML private Label lblStatus;

    private final ProjectManagerDAO projectManagerDAO  = new ProjectManagerDAO();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearStatus();
    }

    @FXML
    private void setAllProjectManagerDTO(ActionEvent event, ProjectManagerDTO projectManagerDTO) {
        projectManagerDTO.setFirstName(txtFirstName.getText().trim());
        projectManagerDTO.setSecondName(txtSecondName.getText().trim());
        projectManagerDTO.setFirstLastName(txtFirstLastName.getText().trim());
        projectManagerDTO.setSecondLastName(txtSecondLastName.getText().trim());
        projectManagerDTO.setRole(txtRole.getText().trim());
        projectManagerDTO.setResponsability(txtResponsability.getText().trim());
        projectManagerDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
    }

    @FXML
    private void saveProjectManager(ActionEvent event) {

        clearStatus();
        if (!validateAddFields()) {
            return;
        }

        ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
        setAllProjectManagerDTO(event, projectManagerDTO);

        try {
            if (projectManagerDAO.addProjectManagerDAO(projectManagerDTO)) {
                showSuccess("Encargado de proyecto registrado correctamente.");
                clearAddFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
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
        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtResponsability.getText().isBlank() ||
                txtRole.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank()){
            showError("Completa todos los campos obligatorios.");
            validFields = false;
        }
        return validFields;
    }

    private void clearAddFields() {
        txtFirstName.clear();
        txtSecondName.clear();
        txtFirstLastName.clear();
        txtSecondLastName.clear();
        txtResponsability.clear();
        txtRole.clear();
        txtPhoneNumber.clear();
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
