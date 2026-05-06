package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.CoordinatorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.ResourceBundle;

public class NewCoordinatorController implements Initializable {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPersonalNumber;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;

    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
    }

    private CoordinatorDTO buildCoordinatorDTO() {
        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        coordinatorDTO.setFirstName(txtFirstName.getText().trim());
        coordinatorDTO.setSecondName(txtSecondName.getText().trim());
        coordinatorDTO.setFirstLastName(txtFirstLastName.getText().trim());
        coordinatorDTO.setSecondLastName(txtSecondLastName.getText().trim());
        coordinatorDTO.setEmail(txtEmail.getText().trim());
        coordinatorDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        coordinatorDTO.setPersonalNumber(txtPersonalNumber.getText().trim());
        coordinatorDTO.setPassword(txtPassword.getText().trim());
        return coordinatorDTO;
    }

    private boolean validateRegistrationInputs() {
        boolean emptyFields = false;
        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtPersonalNumber.getText().isBlank() ||
                txtPassword.getText().isBlank()) {
            showError("Completa todos los campos obligatorios.");
            emptyFields = true;
        }
        return emptyFields;
    }

    @FXML
    private void saveCoordinator(ActionEvent event) {
        clearStatus();
        if (validateRegistrationInputs()) {
            return;
        }

        try {
            if (coordinatorDAO.addCoordinator(buildCoordinatorDTO())) {
                showSuccess("Coordinador registrado correctamente.");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                "Menú Administrador", event);
    }

    private void clearInputFields() {
        txtFirstName.clear();
        txtSecondName.clear();
        txtFirstLastName.clear();
        txtSecondLastName.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtPersonalNumber.clear();
        txtPassword.clear();
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
