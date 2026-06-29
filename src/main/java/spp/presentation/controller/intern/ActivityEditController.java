package spp.presentation.controller.intern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import spp.utils.view.datepicker.DatePickerConfigurator;
import spp.utils.view.datepicker.DateValidationMode;
import spp.utils.view.datepicker.DateValidator;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.WindowCloser;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ActivityEditController implements Initializable {

    @FXML private TextField txtTitle;
    @FXML private TextArea taDescription;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;
    @FXML private TextField txtEstimatedTime;
    @FXML private TextField txtEffectiveTime;
    @FXML private TextField txtProgress;
    @FXML private TextArea taObservations;
    @FXML private Label lblStatus;
    private ActivityDTO activity;
    private boolean updated = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatePickerConfigurator.configureSmartDatePicker(dpStartDate, DateValidationMode.ANY_DATE);
        DatePickerConfigurator.configureSmartDatePicker(dpEndDate, DateValidationMode.ANY_DATE);
    }

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;

        String title = activity.getTitle();
        String description = activity.getDescription();
        LocalDate startDate = activity.getStartDate();
        LocalDate endDate = activity.getEndDate();
        String estimatedTimeString = String.valueOf(activity.getEstimatedTime());
        String effectiveTimeString = String.valueOf(activity.getEffectiveTime());
        String progressString = String.valueOf(activity.getProgress());
        String observations = activity.getObservations();

        txtTitle.setText(title);
        taDescription.setText(description);
        dpStartDate.setValue(startDate);
        dpEndDate.setValue(endDate);
        txtEstimatedTime.setText(estimatedTimeString);
        txtEffectiveTime.setText(effectiveTimeString);
        txtProgress.setText(progressString);
        taObservations.setText(observations);
    }

    public boolean isUpdated() {
        return updated;
    }

    @FXML
    private void saveChanges(ActionEvent event) {
        boolean hasInputFieldsErrors = hasValidationErrors();

        if (!hasInputFieldsErrors) {
            ActivityDTO editedActivity = readForm();
            ActivityDAO activityDAO = new ActivityDAO();

            try {
                boolean isUpdatedSuccessfully = activityDAO.updateActivity(editedActivity);

                if (isUpdatedSuccessfully) {
                    copyInto(activity, editedActivity);
                    updated = true;
                    WindowCloser.closeWindowFromEvent(event);
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
        } else if (endDate.isBefore(startDate)) {
            StatusLabel.showError(lblStatus, "La fecha fin no puede ser anterior a la fecha inicio.");
            hasErrors = true;
        } else {
            LocalDate selectedStartDate = dpStartDate.getValue();
            LocalDate selectedFinalDate = dpStartDate.getValue();
            if (!DateValidator.isDateValid(selectedStartDate, DateValidationMode.ANY_DATE)
                    || !DateValidator.isDateValid(selectedFinalDate, DateValidationMode.ANY_DATE)) {
                StatusLabel.showError(lblStatus, "La fecha ingresada es errónea");
                hasErrors = true;
            } else {
                Integer estimatedTime = parseNonNegativeInt(estimatedTimeString);
                Integer effectiveTime = parseNonNegativeInt(effectiveTimeString);
                Integer progress = parseNonNegativeInt(progressString);

                boolean hasInvalidNumbers = estimatedTime == null || effectiveTime == null || progress == null;

                if (hasInvalidNumbers) {
                    StatusLabel.showError(lblStatus, "Tiempo estimado, tiempo efectivo y avance deben ser números válidos.");
                    hasErrors = true;
                } else if (progress > ViewConstant.MAX_PROGRESS) {
                    StatusLabel.showError(lblStatus, "El avance debe estar entre 0 y 100.");
                    hasErrors = true;
                }
            }
        }

        return hasErrors;
    }

    private Integer parseNonNegativeInt(String text) {
        Integer parsedValue = null;

        try {
            int numericValue = Integer.parseInt(text);
            if (numericValue >= 0) {
                parsedValue = numericValue;
            }
        } catch (NumberFormatException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
        }

        return parsedValue;
    }

    private ActivityDTO readForm() {
        ActivityDTO editedActivity = new ActivityDTO();

        int currentId = activity.getId();
        String title = txtTitle.getText().trim();
        String description = taDescription.getText().trim();
        LocalDate startDate = dpStartDate.getValue();
        LocalDate endDate = dpEndDate.getValue();
        String estimatedTimeString = txtEstimatedTime.getText().trim();
        int estimatedTime = Integer.parseInt(estimatedTimeString);
        String effectiveTimeString = txtEffectiveTime.getText().trim();
        int effectiveTime = Integer.parseInt(effectiveTimeString);
        String progressString = txtProgress.getText().trim();
        int progress = Integer.parseInt(progressString);
        String observations = taObservations.getText().trim();

        editedActivity.setId(currentId);
        editedActivity.setTitle(title);
        editedActivity.setDescription(description);
        editedActivity.setStartDate(startDate);
        editedActivity.setEndDate(endDate);
        editedActivity.setEstimatedTime(estimatedTime);
        editedActivity.setEffectiveTime(effectiveTime);
        editedActivity.setProgress(progress);
        editedActivity.setObservations(observations);

        return editedActivity;
    }

    private void copyInto(ActivityDTO target, ActivityDTO source) {
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setStartDate(source.getStartDate());
        target.setEndDate(source.getEndDate());
        target.setEstimatedTime(source.getEstimatedTime());
        target.setEffectiveTime(source.getEffectiveTime());
        target.setProgress(source.getProgress());
        target.setObservations(source.getObservations());
    }

    @FXML
    private void cancelEdit(ActionEvent event) {
        WindowCloser.closeWindowFromEvent(event);
    }

}