package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;


public class ActivityRegistrationController {

    @FXML private Label lblStatus;
    @FXML private TextField txtTitle;
    @FXML private TextArea taDescription;
    @FXML private DatePicker dpStartDate;
    @FXML private DatePicker dpEndDate;
    @FXML private TextField txtEstimatedTime;
    @FXML private TextField txtEffectiveTime;
    @FXML private TextField txtProgress;
    @FXML private TextArea taObservations;
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final InternDAO internDAO = new InternDAO();
    private static final int MAX_PROGRESS = 100;

    @FXML
    private void saveActivity(ActionEvent event) {
        if (validateInputs()) {
            return;
        }

        try {
            String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
            if (activityDAO.saveActivityForIntern(studentNumber, buildActivityDTO())) {
                StatusLabel.showSuccess(lblStatus, "Actividad registrada correctamente.");
                clearFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al registrar la actividad");
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
            StatusLabel.showError(lblStatus, "Llene todos los campos.");
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