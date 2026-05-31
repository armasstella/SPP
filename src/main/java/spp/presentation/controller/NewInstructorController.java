package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InstructorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;


public class NewInstructorController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPersonalNumber;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;
    @FXML private ComboBox<String> cmbShift;

    private InstructorDTO buildInstructorDTO() {
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setFirstName(txtFirstName.getText().trim());
        instructorDTO.setSecondName(txtSecondName.getText().trim());
        instructorDTO.setFirstLastName(txtFirstLastName.getText().trim());
        instructorDTO.setSecondLastName(txtSecondLastName.getText().trim());
        instructorDTO.setEmail(txtEmail.getText().trim());
        instructorDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        instructorDTO.setPersonalNumber(txtPersonalNumber.getText().trim());
        instructorDTO.setPassword(txtPassword.getText().trim());
        instructorDTO.setShift(cmbShift.getValue());

        return instructorDTO;

    }

    @FXML
    private void saveInstructor(ActionEvent event) {
        final InstructorDAO instructorDAO = new InstructorDAO();

        if (validateRegistrationInputs()) {
            return;
        }

        try {
            if (instructorDAO.addInstructor(buildInstructorDTO())) {
                StatusLabel.showSuccess(lblStatus, "Profesor registrado correctamente.");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                "Menú Administrador", event);

    }

    private boolean validateRegistrationInputs() {
        boolean emptyFields = false;

        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtPersonalNumber.getText().isBlank() ||
                txtPassword.getText().isBlank() ||
                cmbShift.getValue() == null) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            emptyFields = true;
        }

        return emptyFields;

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
        cmbShift.setValue(null);

    }

}
