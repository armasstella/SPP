package spp.presentation.controller.intern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.ViewConstant;
import spp.utils.view.datepicker.DatePickerConfigurator;
import spp.utils.view.datepicker.DateValidationMode;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ActivityRegistrationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtTitle;
    @FXML private TextArea taDescription;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;
    @FXML private TextField txtEstimatedTime;
    @FXML private TextField txtEffectiveTime;
    @FXML private TextField txtProgress;
    @FXML private TextArea taObservations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatePickerConfigurator.configureSmartDatePicker(dpStartDate, DateValidationMode.ANY_DATE);
        DatePickerConfigurator.configureSmartDatePicker(dpEndDate, DateValidationMode.ANY_DATE);
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtTitle,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_INTERN_ACTIVITY_TITLE);
        InputFilter.applyFormatFilter(taDescription,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_ACTIVITY_DESCRIPTION);
        InputFilter.applyFormatFilter(txtEstimatedTime,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY);
        InputFilter.applyFormatFilter(txtEffectiveTime,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY);
        InputFilter.applyFormatFilter(txtProgress,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_PROGRESS);
        InputFilter.applyFormatFilter(taObservations,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_INTERN_ACTIVITY_DESCRIPTION);
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtTitle.getText().trim().isEmpty() ||
                taDescription.getText().trim().isEmpty() ||
                taObservations.getText().trim().isEmpty() ||
                dpStartDate.getValue() == null ||
                dpEndDate.getValue() == null ||
                txtEstimatedTime.getText().trim().isEmpty() ||
                txtEffectiveTime.getText().trim().isEmpty() ||
                txtProgress.getText().trim().isEmpty()) {

            emptyFields = true;
        }

        return emptyFields;
    }

    private boolean hasValidFormats() {
        boolean validFormats = true;

        LocalDate startDate = dpStartDate.getValue();
        LocalDate endDate = dpEndDate.getValue();

        if (endDate != null && startDate != null && endDate.isBefore(startDate)) {
            StatusLabel.showError(lblStatus, "La fecha fin no puede ser anterior a la fecha inicio.");
            validFormats = false;
        }

        if (validFormats) {
            Integer estimatedTime = parseNonNegativeInt(txtEstimatedTime.getText().trim());
            Integer effectiveTime = parseNonNegativeInt(txtEffectiveTime.getText().trim());
            Integer progress = parseNonNegativeInt(txtProgress.getText().trim());

            if (estimatedTime == null || effectiveTime == null || progress == null) {
                StatusLabel.showError(lblStatus, "Tiempo estimado, tiempo efectivo y avance deben ser números válidos.");
                validFormats = false;
            } else if (progress > ViewConstant.MAX_PROGRESS) {
                StatusLabel.showError(lblStatus, "El avance debe estar entre 0 y 100.");
                validFormats = false;
            }
        }

        return validFormats;
    }

    private Integer parseNonNegativeInt(String text) {
        try {
            int value = Integer.parseInt(text.trim());
            return (value >= ViewConstant.ALLOWED_POSITIVE_NUMERIC_VALUE) ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean areValidFields() {
        boolean validFields = false;

        if (hasEmptyFields()) {
            StatusLabel.showError(lblStatus, "Complete todos los campos.");
        } else {
            if (hasValidFormats()) {
                validFields = true;
            }
        }

        return validFields;
    }

    @FXML
    private void saveActivity(ActionEvent event) {
        if (areValidFields()) {
            ActivityDAO activityDAO = new ActivityDAO();
            InternDAO internDAO = new InternDAO();

            try {
                String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
                if (activityDAO.saveActivityForIntern(studentNumber, buildActivityDTO())) {
                    StatusLabel.showSuccess(lblStatus, "Actividad registrada correctamente.");
                    clearFields();
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "Error al registrar la actividad");
            }
        }
    }

    private ActivityDTO buildActivityDTO() {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setTitle(txtTitle.getText().trim());
        activityDTO.setDescription(taDescription.getText().trim());
        activityDTO.setStartDate(dpStartDate.getValue());
        activityDTO.setEndDate(dpEndDate.getValue());
        activityDTO.setEstimatedTime(Integer.parseInt(txtEstimatedTime.getText().trim()));
        activityDTO.setEffectiveTime(Integer.parseInt(txtEffectiveTime.getText().trim()));
        activityDTO.setProgress(Integer.parseInt(txtProgress.getText().trim()));
        activityDTO.setObservations(taObservations.getText().trim());
        return activityDTO;
    }

    private void clearFields() {
        txtTitle.clear();
        taDescription.clear();
        dpStartDate.setValue(null);
        dpEndDate.setValue(null);
        txtEstimatedTime.clear();
        txtEffectiveTime.clear();
        txtProgress.clear();
        taObservations.clear();
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/MonthlyActivityRegistersView.fxml",
                "Menú Practicante", event);
    }
}