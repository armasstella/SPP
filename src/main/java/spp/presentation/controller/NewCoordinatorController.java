package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.CoordinatorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
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
    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();

    }

    private void setUpFields() {
        InputFilter.applyFilter(txtFirstName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtSecondName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtFirstLastName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtSecondLastName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtEmail, InputFilter.EMAIL_CHARS_PATTERN, 40);
        InputFilter.applyFilter(txtPhoneNumber, InputFilter.NUMERIC_PATTERN, 10);
        InputFilter.applyFilter(txtPersonalNumber, InputFilter.NUMERIC_PATTERN, 5);
        InputFilter.applyFilter(txtPassword, InputFilter.PASSWORD_PATTERN, 12);

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
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            emptyFields = true;
        }

        return emptyFields;

    }

    @FXML
    private void saveCoordinator(ActionEvent event) {
        if (validateRegistrationInputs()) {
            return;
        }

        try {
            if (coordinatorDAO.addCoordinator(buildCoordinatorDTO())) {
                StatusLabel.showSuccess(lblStatus, "Coordinador registrado correctamente.");
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
