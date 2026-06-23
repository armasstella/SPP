package spp.presentation.controller.coordinator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InternDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
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

    private void loadCourseCodeInComboBox() {
        try {
            CourseDAO courseDAO = new CourseDAO();
            List<CourseDTO> courses = courseDAO.getCourseCodesForActiveTerm();
            ObservableList<CourseDTO> courseObservableList =
                    FXCollections.observableArrayList(courses);
            cmbCourseCode.setItems(courseObservableList);

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "FATAL: Error al cargar cursos");
        }

    }

    private void setUpFields() {
        InputFilter.applyFilter(txtFirstName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtSecondName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtFirstLastName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtSecondLastName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtEmail, InputFilter.EMAIL_CHARS_PATTERN, 40);
        InputFilter.applyFilter(txtPhoneNumber, InputFilter.NUMERIC_PATTERN, 10);

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
        internDTO.setSex(cmbSex.getValue());
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
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Cancelar", event);

    }

    @FXML
    private void saveIntern(ActionEvent event) {
        if (validateRegistrationInputs()) {
            return;
        }

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
                AppLogger.logError(e);
                StatusLabel.showError(lblStatus, e.getMessage());
            }
        } else {
            String errorMessages = String.join("\n. ", internDTO.getErrors());
            StatusLabel.showError(lblStatus, "Corrige los siguientes formatos:\n. " + errorMessages);
        }
    }

    private boolean validateRegistrationInputs() {
        boolean emptyFields = false;
        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtStudentNumber.getText().isBlank() ||
                txtPassword.getText().isBlank() ||
                cmbSex.getValue() == null ||
                dpBirthDate.getValue() == null) {
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
        txtStudentNumber.clear();
        txtPassword.clear();
        cmbSex.setValue(null);
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
