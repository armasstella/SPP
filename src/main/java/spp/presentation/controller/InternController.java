package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.InternDAO;
import spp.utils.logger.AppLogger;

import java.net.URL;
import java.util.ResourceBundle;

public class InternController implements Initializable {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtStudentNumber;
    @FXML private TextField txtPassword;

    @FXML private Label lblStatus;

    private final InternDAO internDAO = new InternDAO();;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
    }

    @FXML
    private void setAllIntern(ActionEvent event, InternDTO internDTO) {
        internDTO.setFirstName(txtFirstName.getText().trim());
        internDTO.setSecondName(txtSecondName.getText().trim());
        internDTO.setFirstLastName(txtFirstLastName.getText().trim());
        internDTO.setSecondLastName(txtSecondLastName.getText().trim());
        internDTO.setEmail(txtEmail.getText().trim());
        internDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        internDTO.setStudentNumber(txtStudentNumber.getText().trim());
        internDTO.setPassword(txtPassword.getText().trim());
    }

    @FXML
    private void saveIntern(ActionEvent event) {

        clearStatus();
        if (!validateAddFields()) {
            return;
        }

        InternDTO internDTO = new InternDTO();
        setAllIntern(event, internDTO);

        try {
            if (internDAO.addIntern(internDTO)) {
                showSuccess("Practicante registrado correctamente.");
                clearAddFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    private boolean validateAddFields() {
        boolean validFields = true;
        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtStudentNumber.getText().isBlank() ||
                txtPassword.getText().isBlank()){
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
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtStudentNumber.clear();
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