package spp.presentation.controller.coordinator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InternDAO;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;


public class NewInternController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtStudentNumber;
    @FXML private TextField txtPassword;
    @FXML private ComboBox<String> cmbSex;
    @FXML private RadioButton rbYes;
    @FXML private RadioButton rbNo;
    @FXML private VBox vbLanguageDetail;
    @FXML private TextField txtIndigenousLanguage;
    @FXML private DatePicker dpBirthDate;
    @FXML private ComboBox<CourseDTO> cmbCourseCode;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureDatePicker();
        toggleIndigenousLanguageField();
        setUpFields();
        loadCourseCodeInComboBox();
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
        InputFilter.applyFormatFilter(txtStudentNumber,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_STUDENT_NUMBER);
        InputFilter.applyFormatFilter(txtPassword,
                ViewConstant.PATTERN_PASSWORD_CHARS, ViewConstant.MAX_LENGTH_PASSWORD);
        InputFilter.applyFormatFilter(txtIndigenousLanguage,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_CATEGORY);
    }

    private void setAllIntern(InternDTO internDTO) {
        internDTO.setFirstName(txtFirstName.getText().trim());
        internDTO.setSecondName(txtSecondName.getText().trim());
        internDTO.setFirstLastName(txtFirstLastName.getText().trim());
        internDTO.setSecondLastName(txtSecondLastName.getText().trim());
        internDTO.setEmail(txtEmail.getText().trim());
        internDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        internDTO.setStudentNumber(txtStudentNumber.getText().trim());
        internDTO.setPassword(txtPassword.getText().trim());
        internDTO.setSex(cmbSex.getValue());

        internDTO.setSpeaksIndigenousLanguage(rbYes.isSelected());
        internDTO.setIndigenousLanguage(txtIndigenousLanguage.getText().trim());

        LocalDate selectedDate = dpBirthDate.getValue();
        if (selectedDate != null) {
            internDTO.setBirthDate(selectedDate.atStartOfDay());
        }
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        boolean isLanguageEmptyAndRequired = rbYes.isSelected() && txtIndigenousLanguage.getText().isBlank();

        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtStudentNumber.getText().isBlank() ||
                txtPassword.getText().isBlank() ||
                cmbSex.getValue() == null ||
                dpBirthDate.getValue() == null ||
                isLanguageEmptyAndRequired) {

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
        boolean validStudentNumber = InputFilter.hasMinimumLength(txtStudentNumber,
                ViewConstant.MAX_LENGTH_STUDENT_NUMBER);
        boolean validPassword = InputFilter.hasMinimumLength(txtPassword,
                ViewConstant.MIN_LENGTH_PASSWORD);

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

        boolean validLanguage = true;
        if (rbYes.isSelected() && !txtIndigenousLanguage.getText().isBlank()) {
            validLanguage = InputFilter.hasMinimumLength(txtIndigenousLanguage,
                    ViewConstant.MIN_LENGTH_INDIGENOUS_LANGUAGE_NAME);
        }

        if (validFirstName && validFirstLastName && validStudentNumber && validPassword &&
                validSecondName && validSecondLastName && validLanguage) {
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
    private void toggleIndigenousLanguageField() {
        boolean isVisible = rbYes.isSelected();
        vbLanguageDetail.setVisible(isVisible);
        vbLanguageDetail.setManaged(isVisible);

        if (!isVisible) {
            txtIndigenousLanguage.clear();
        }
    }

    @FXML
    private void saveIntern(ActionEvent event) {
        if (areValidFields()) {
            InternDTO internDTO = new InternDTO();
            setAllIntern(internDTO);

            if (internDTO.isValid()) {
                InternDAO internDAO = new InternDAO();
                try {
                    if (internDAO.registerIntern(internDTO)) {
                        StatusLabel.showSuccess(lblStatus, "Practicante registrado correctamente.");
                        clearInputFields();
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, "No se pudo registrar el practicante.");
                }
            } else {
                String errorMessages = String.join("\n• ", internDTO.getErrors());
                StatusLabel.showError(lblStatus, "Corrige los siguientes formatos:\n• " + errorMessages);
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);
    }

    private void loadCourseCodeInComboBox() {
        try {
            CourseDAO courseDAO = new CourseDAO();
            List<CourseDTO> courses = courseDAO.getCourseCodesForActiveTerm();
            ObservableList<CourseDTO> courseObservableList = FXCollections.observableArrayList(courses);
            cmbCourseCode.setItems(courseObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al cargar la lista de cursos.");
        }
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
        cmbSex.setValue(null);
        cmbCourseCode.setValue(null);
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
