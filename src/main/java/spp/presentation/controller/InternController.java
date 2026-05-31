package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InternDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    @FXML private ComboBox<String> cmbGender;
    @FXML private RadioButton rbYes;
    @FXML private RadioButton rbNo;
    @FXML private VBox vbLanguageDetail;
    @FXML private TextField txtIndigenousLanguage;
    @FXML private Label lblStatus;
    @FXML private DatePicker dpBirthDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final InternDAO internDAO = new InternDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureDatePicker();
        toggleIndigenousLanguageField();
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);
    }

    @FXML
    private void setAllIntern(InternDTO internDTO) {
        internDTO.setFirstName(txtFirstName.getText().trim());
        internDTO.setSecondName(txtSecondName.getText().trim());
        internDTO.setFirstLastName(txtFirstLastName.getText().trim());
        internDTO.setSecondLastName(txtSecondLastName.getText().trim());
        internDTO.setEmail(txtEmail.getText().trim());
        internDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        internDTO.setStudentNumber(txtStudentNumber.getText().trim());
        internDTO.setPassword(txtPassword.getText().trim());
        internDTO.setGender(cmbGender.getValue());
        internDTO.setSpeaksIndigenousLanguage(rbYes.isSelected());
        internDTO.setIndigenousLanguage(txtIndigenousLanguage.getText().trim());
        LocalDate selectedDate = dpBirthDate.getValue();
        internDTO.setBirthDate(selectedDate.atStartOfDay());
    }

    @FXML
    private void toggleIndigenousLanguageField() {
        boolean isVisible = rbYes.isSelected();
        vbLanguageDetail.setVisible(isVisible);
        vbLanguageDetail.setManaged(isVisible);

        if (!isVisible) {
            txtIndigenousLanguage.clear();
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Cancelar", event);
    }

    @FXML
    private void saveIntern(ActionEvent event) {

        if (validateRegistrationInputs()) {
            return;
        }

        InternDTO internDTO = new InternDTO();
        setAllIntern(internDTO);

        try {
            if (internDAO.addIntern(internDTO)) {
                StatusLabel.showSuccess(lblStatus, "Practicante registrado correctamente.");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private boolean validateRegistrationInputs() {
        boolean validFields = false;
        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtStudentNumber.getText().isBlank() ||
                txtPassword.getText().isBlank() ||
                cmbGender.getValue() == null ||
                dpBirthDate.getValue() == null) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            validFields = true;
        }
        return validFields;
    }

    private void clearInputFields() {
        txtFirstName.clear();
        txtSecondName.clear();
        txtFirstLastName.clear();
        txtSecondLastName.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtStudentNumber.clear();
        txtPassword.clear();
        cmbGender.setValue(null);
        dpBirthDate.setValue(null);
        rbNo.setSelected(true);
        toggleIndigenousLanguageField();
    }

    private void configureDatePicker() {
        dpBirthDate.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    try {
                        return LocalDate.parse(string, dateFormatter);
                    } catch (Exception e) {
                        return null;
                    }
                }
                return null;
            }
        });
    }
}