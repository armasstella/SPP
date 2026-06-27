package spp.presentation.controller.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.InstructorDAO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.ResourceBundle;

public class NewInstructorController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPersonalNumber;
    @FXML private TextField txtPassword;
    @FXML private ComboBox<String> cmbShift;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtFirstName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtSecondName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtFirstLastName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtSecondLastName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtEmail,
                ViewConstant.PATTERN_EMAIL_CHARS, ViewConstant.MAX_LENGTH_EMAIL);
        InputFilter.applyFormatFilter(txtPhoneNumber,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_PHONE);
        InputFilter.applyFormatFilter(txtPersonalNumber,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_PERSONAL_NUMBER);
        InputFilter.applyFormatFilter(txtPassword,
                ViewConstant.PATTERN_PASSWORD_CHARS, ViewConstant.MAX_LENGTH_PASSWORD);
    }

    private void setAllInstructor(InstructorDTO instructorDTO) {
        instructorDTO.setFirstName(txtFirstName.getText().trim());
        instructorDTO.setSecondName(txtSecondName.getText().trim());
        instructorDTO.setFirstLastName(txtFirstLastName.getText().trim());
        instructorDTO.setSecondLastName(txtSecondLastName.getText().trim());
        instructorDTO.setEmail(txtEmail.getText().trim());
        instructorDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        instructorDTO.setPersonalNumber(txtPersonalNumber.getText().trim());
        instructorDTO.setPassword(txtPassword.getText().trim());
        instructorDTO.setShift(cmbShift.getValue().trim());
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtPersonalNumber.getText().isBlank() ||
                txtPassword.getText().isBlank() ||
                cmbShift.getValue() == null) {

            emptyFields = true;
        }

        return emptyFields;
    }

    private boolean hasValidMinimumLengths() {
        boolean validLengths = false;

        boolean validFirstName = InputFilter.hasMinimumLength(txtFirstName,
                ViewConstant.MIN_LENGTH_NAME);

        boolean validFirstLastName = InputFilter.hasMinimumLength(txtFirstLastName,
                ViewConstant.MIN_LENGTH_NAME);

        boolean validSecondName = true;
        if (!txtSecondName.getText().isBlank()) {
            validSecondName = InputFilter.hasMinimumLength(txtSecondName,
                    ViewConstant.MIN_LENGTH_NAME);
        }

        boolean validSecondLastName = true;
        if (!txtSecondLastName.getText().isBlank()) {
            validSecondLastName = InputFilter.hasMinimumLength(txtSecondLastName,
                    ViewConstant.MIN_LENGTH_NAME);
        }

        boolean validPassword = InputFilter.hasMinimumLength(txtPassword,
                ViewConstant.MIN_LENGTH_PASSWORD);

        boolean validPersonalNumber = InputFilter.hasMinimumLength(txtPersonalNumber,
                ViewConstant.MIN_LENGTH_PERSONAL_NUMBER);

        if (validFirstName && validSecondName && validFirstLastName && validSecondLastName && validPassword && validPersonalNumber) {
            validLengths = true;
        }

        return validLengths;
    }

    private boolean areValidFields() {
        boolean validFields = false;

        if (hasEmptyFields()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
        } else {
            if (hasValidMinimumLengths()) {
                validFields = true;
            } else {
                StatusLabel.showError(lblStatus, "La longitud de los campos debe cumplir con el mínimo de caracteres.");
            }
        }

        return validFields;
    }

    @FXML
    private void saveInstructor(ActionEvent event) {
        if (areValidFields()) {
            InstructorDTO instructorDTO = new InstructorDTO();
            setAllInstructor(instructorDTO);

            if (instructorDTO.isValid()) {
                InstructorDAO instructorDAO = new InstructorDAO();
                try {
                    if (instructorDAO.registerInstructor(instructorDTO)) {
                        StatusLabel.showSuccess(lblStatus, "Profesor registrado correctamente.");
                        clearInputFields();
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, "No se pudo registrar el profesor.");
                }
            } else {
                String errorMessages = String.join("\n• ", instructorDTO.getErrors());
                StatusLabel.showError(lblStatus, "Corrige los siguientes formatos:\n• " + errorMessages);
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/admin/AdminMenuView.fxml",
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
        cmbShift.setValue(null);
    }
}
