package spp.presentation.controller.intern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.enums.ActivityType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.utils.view.datepicker.DateValidator;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;
import spp.utils.view.datepicker.DatePickerConfigurator;
import spp.utils.view.datepicker.DateValidationMode;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FinalActivityRegistrationController implements Initializable {

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
        setUpFields();
    }

    private void setUpFields() {
        DatePickerConfigurator.configureSmartDatePicker(dpStartDate, DateValidationMode.ANY_DATE);
        DatePickerConfigurator.configureSmartDatePicker(dpEndDate, DateValidationMode.ANY_DATE);

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

    @FXML
    private void saveActivity(ActionEvent event) {
        boolean hasInputFieldsErrors = hasValidationErrors();

        if (!hasInputFieldsErrors) {
            ActivityDAO activityDAO = new ActivityDAO();
            InternDAO internDAO = new InternDAO();

            try {
                String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
                ActivityDTO finalActivity = buildActivityDTO();

                if (activityDAO.saveActivityForIntern(studentNumber, finalActivity, ActivityType.FINAL)) {
                    StatusLabel.showSuccess(lblStatus, "Actividad final registrada correctamente.");
                    clearFields();
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, e.getMessage());
            }
        }
    }

    private boolean hasValidationErrors() {
        boolean hasErrors = false;

        String title = txtTitle.getText().trim();
        String description = taDescription.getText().trim();
        String observations = taObservations.getText().trim();
        String estimatedTimeString = txtEstimatedTime.getText().trim();
        String effectiveTimeString = txtEffectiveTime.getText().trim();
        String progressString = txtProgress.getText().trim();
        LocalDate startDate = dpStartDate.getValue();
        LocalDate endDate = dpEndDate.getValue();

        boolean areFieldsEmpty = title.isEmpty() || description.isEmpty() || observations.isEmpty() ||
                estimatedTimeString.isEmpty() || effectiveTimeString.isEmpty() || progressString.isEmpty() ||
                startDate == null || endDate == null;

        if (areFieldsEmpty) {
            StatusLabel.showError(lblStatus, "Complete todos los campos.");
            hasErrors = true;
        } else if (!DateValidator.isDateValid(startDate, DateValidationMode.ANY_DATE) ||
                !DateValidator.isDateValid(endDate, DateValidationMode.ANY_DATE)) {
            StatusLabel.showError(lblStatus, "Una o ambas fechas ingresadas son inválidas.");
            hasErrors = true;
        } else if (endDate.isBefore(startDate)) {
            StatusLabel.showError(lblStatus, "La fecha fin no puede ser anterior a la fecha inicio.");
            hasErrors = true;
        } else {
            Integer estimatedTime = Integer.parseInt(estimatedTimeString);
            Integer effectiveTime = Integer.parseInt(effectiveTimeString);
            Integer progress = Integer.parseInt(progressString);

            boolean hasInvalidNumbers = estimatedTime == null || effectiveTime == null || progress == null;

            if (hasInvalidNumbers) {
                StatusLabel.showError(lblStatus, "Tiempo estimado, tiempo efectivo y avance deben ser números válidos.");
                hasErrors = true;
            } else if (progress > ViewConstant.MAX_PROGRESS) {
                StatusLabel.showError(lblStatus, "El avance debe estar entre 0 y 100.");
                hasErrors = true;
            }
        }

        return hasErrors;
    }

    private ActivityDTO buildActivityDTO() {
        ActivityDTO activityDTO = new ActivityDTO();

        String title = txtTitle.getText().trim();
        String description = taDescription.getText().trim();
        LocalDate startDate = dpStartDate.getValue();
        LocalDate endDate = dpEndDate.getValue();
        int estimatedTime = Integer.parseInt(txtEstimatedTime.getText().trim());
        int effectiveTime = Integer.parseInt(txtEffectiveTime.getText().trim());
        int progress = Integer.parseInt(txtProgress.getText().trim());
        String observations = taObservations.getText().trim();

        activityDTO.setTitle(title);
        activityDTO.setDescription(description);
        activityDTO.setStartDate(startDate);
        activityDTO.setEndDate(endDate);
        activityDTO.setEstimatedTime(estimatedTime);
        activityDTO.setEffectiveTime(effectiveTime);
        activityDTO.setProgress(progress);
        activityDTO.setObservations(observations);

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
        ViewNavigator.loadView("/spp/presentation/view/intern/FinalReportMenu.fxml",
                "Reporte Final", event);
    }
}