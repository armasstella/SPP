package spp.presentation.controller.admin;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.CoordinatorDAO;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;


public class NewCoordinatorController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPersonalNumber;
    @FXML private TextField txtPassword;

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
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_PERSONAL_NUMBER );
        InputFilter.applyFormatFilter(txtPassword,
                ViewConstant.PATTERN_PASSWORD_CHARS , ViewConstant.MAX_LENGTH_PASSWORD);

    }

    private void setAllCoordinator(CoordinatorDTO coordinatorDTO) {
        coordinatorDTO.setFirstName(txtFirstName.getText().trim());
        coordinatorDTO.setSecondName(txtSecondName.getText().trim());
        coordinatorDTO.setFirstLastName(txtFirstLastName.getText().trim());
        coordinatorDTO.setSecondLastName(txtSecondLastName.getText().trim());
        coordinatorDTO.setEmail(txtEmail.getText().trim());
        coordinatorDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        coordinatorDTO.setPersonalNumber(txtPersonalNumber.getText().trim());
        coordinatorDTO.setPassword(txtPassword.getText().trim());

    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtPersonalNumber.getText().isBlank() ||
                txtPassword.getText().isBlank()) {
            emptyFields = true;
        }

        return emptyFields;

    }

    private boolean hasValidMinimumLengths() {
        boolean validLengths = false;

        boolean validFirstName = InputFilter.hasMinimumLength(txtFirstName,
                ViewConstant.MIN_LENGTH_NAME);

        boolean validSecondName = true;
        if (!txtSecondName.getText().isBlank()) {
            validSecondName = InputFilter.hasMinimumLength(txtSecondName,
                    ViewConstant.MIN_LENGTH_NAME);
        }
        boolean validFirstLastName = InputFilter.hasMinimumLength(txtFirstLastName,
                ViewConstant.MIN_LENGTH_NAME);

        boolean validPersonalNumber = InputFilter.hasMinimumLength(txtPersonalNumber,
                ViewConstant.MIN_LENGTH_PERSONAL_NUMBER);

        boolean validSecondLastName = true;
        if (!txtSecondLastName.getText().isBlank()) {
            validSecondLastName = InputFilter.hasMinimumLength(txtSecondLastName,
                    ViewConstant.MIN_LENGTH_NAME);
        }

        if (validFirstName && validSecondName && validFirstLastName && validSecondLastName && validPersonalNumber) {
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
    private void saveCoordinator(ActionEvent event) {

        if (areValidFields()) {
            CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
            setAllCoordinator(coordinatorDTO);

            if (coordinatorDTO.isValid()) {
                CoordinatorDAO coordinatorDAO = new CoordinatorDAO();
                try {
                    if (coordinatorDAO.registerCoordinator(coordinatorDTO)) {
                        StatusLabel.showSuccess(lblStatus, "Coordinador registrado correctamente.");
                        clearInputFields();
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, "No se pudo registrar el coordinador.");
                }
            } else {
                String errorMessages = String.join("\n• ", coordinatorDTO.getErrors());
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

    }

}
