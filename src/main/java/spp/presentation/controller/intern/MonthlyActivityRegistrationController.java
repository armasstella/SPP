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
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;
import spp.utils.view.datepicker.DatePickerConfigurator;
import spp.utils.view.datepicker.DateValidationMode;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;


public class MonthlyActivityRegistrationController implements Initializable {

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
    }

    @FXML
    private void saveActivity(ActionEvent event) {
        if (!validateInputs()) {
            ActivityDAO activityDAO = new ActivityDAO();
            InternDAO internDAO = new InternDAO();

            try {
                String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
                if (activityDAO.saveActivityForIntern(studentNumber, buildActivityDTO(), ActivityType.MONTHLY)) {
                    StatusLabel.showSuccess(lblStatus, "Actividad registrada correctamente.");
                    clearFields();
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "Error al registrar la actividad");
            }
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
            return true;
        }

        LocalDate selectedDate = null;
        selectedDate = dpStartDate.getValue();
        if (!DateValidator.isDateValid(selectedDate, DateValidationMode.ANY_DATE)) {
            StatusLabel.showError(lblStatus, "La fecha ingresada es errónea");
            return true;
        }

        selectedDate = dpEndDate.getValue();
        if (!DateValidator.isDateValid(selectedDate, DateValidationMode.ANY_DATE)) {
            StatusLabel.showError(lblStatus, "La fecha ingresada es errónea");
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

        if (progress > ViewConstant.MAX_PROGRESS) {
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
                "Reporte Mensual", event);

    }


}
