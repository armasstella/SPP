package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;


public class ActivityEditController {

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
    private static final int MAX_PROGRESS = 100;

    public void setActivity(ActivityDTO activity) {
        this.activity = activity;
        txtTitle.setText(activity.getTitle());
        taDescription.setText(activity.getDescription());
        dpStartDate.setValue(activity.getStartDate());
        dpEndDate.setValue(activity.getEndDate());
        txtEstimatedTime.setText(String.valueOf(activity.getEstimatedTime()));
        txtEffectiveTime.setText(String.valueOf(activity.getEffectiveTime()));
        txtProgress.setText(String.valueOf(activity.getProgress()));
        taObservations.setText(activity.getObservations());

    }

    public boolean isUpdated() {
        return updated;

    }

    @FXML
    private void saveChanges(ActionEvent event) {
        if (validateInputs()) {
            return;
        }

        ActivityDTO editedActivity = readForm();
        ActivityDAO activityDAO = new ActivityDAO();
        try {
            if (activityDAO.updateActivity(editedActivity)) {
                copyInto(activity, editedActivity);
                updated = true;
                closeWindow(event);
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al actualizar la actividad");
        }

    }

    private boolean validateInputs() {
        if (txtTitle.getText().trim().isEmpty()
                || taDescription.getText().trim().isEmpty()
                || taObservations.getText().trim().isEmpty()
                || dpStartDate.getValue() == null
                || dpEndDate.getValue() == null
                || txtEstimatedTime.getText().trim().isEmpty()
                || txtEffectiveTime.getText().trim().isEmpty()
                || txtProgress.getText().trim().isEmpty()) {
            StatusLabel.showError(lblStatus, "Complete todos los campos.");
            return true;
        }

        if (dpEndDate.getValue().isBefore(dpStartDate.getValue())) {
            StatusLabel.showError(lblStatus, "La fecha fin no puede ser anterior a la fecha inicio.");
            return true;
        }

        Integer estimatedTime = parseNonNegativeInt(txtEstimatedTime.getText());
        Integer effectiveTime = parseNonNegativeInt(txtEffectiveTime.getText());
        Integer progress = parseNonNegativeInt(txtProgress.getText());
        if (estimatedTime == null || effectiveTime == null || progress == null) {
            StatusLabel.showError(lblStatus, "Tiempo estimado, tiempo efectivo y avance deben ser números válidos.");
            return true;
        }

        if (progress > MAX_PROGRESS) {
            StatusLabel.showError(lblStatus, "El avance debe estar entre 0 y 100.");
            return true;
        }

        return false;

    }

    private Integer parseNonNegativeInt(String text) {
        try {
            int value = Integer.parseInt(text.trim());
            return (value >= 0) ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }

    }

    private ActivityDTO readForm() {
        ActivityDTO editedActivity = new ActivityDTO();
        editedActivity.setId(activity.getId());
        editedActivity.setTitle(txtTitle.getText().trim());
        editedActivity.setDescription(taDescription.getText().trim());
        editedActivity.setStartDate(dpStartDate.getValue());
        editedActivity.setEndDate(dpEndDate.getValue());
        editedActivity.setEstimatedTime(Integer.parseInt(txtEstimatedTime.getText().trim()));
        editedActivity.setEffectiveTime(Integer.parseInt(txtEffectiveTime.getText().trim()));
        editedActivity.setProgress(Integer.parseInt(txtProgress.getText().trim()));
        editedActivity.setObservations(taObservations.getText().trim());
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
        closeWindow(event);

    }

    private void closeWindow(ActionEvent event) {
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close();

    }

}
