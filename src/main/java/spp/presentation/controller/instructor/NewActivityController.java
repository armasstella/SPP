package spp.presentation.controller.instructor;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InstructorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;


public class NewActivityController implements Initializable {

    @FXML TextField txtTitle;
    @FXML TextArea taDescription;
    @FXML DatePicker dpSubmissionDate;
    @FXML Label lblStatus;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final ActivityDAO activityDAO = new ActivityDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configureDatePicker();
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFilter(txtTitle, InputFilter.NAME_PATTERN, 20);
        InputFilter.applyFilter(taDescription, InputFilter.ALPHANUMERIC_PATTERN, 250);

    }

    @FXML
    private void saveActivity() {
        if (validateEmptyFields()) {
            return;
        }

        try {
            if (activityDAO.addActivity(buildActivityDTO())) {
                StatusLabel.showSuccess(lblStatus, "Actividad añadida correctamente");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al añadir actividad");
        }

    }

    private void clearInputFields() {
        txtTitle.clear();
        taDescription.clear();
        dpSubmissionDate.setValue(null);

    }

    private ActivityDTO buildActivityDTO() {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setTitle(txtTitle.getText().trim());
        activityDTO.setDescription(taDescription.getText().trim());
        activityDTO.setSubmissionDate(dpSubmissionDate.getValue().atStartOfDay());
        activityDTO.setInstructorDTO(buildInstructorDTO());

        return activityDTO;

    }

    private InstructorDTO buildInstructorDTO() {
        InstructorDTO instructorDTO = new InstructorDTO();
        InstructorDAO instructorDAO = new InstructorDAO();

        try {
            instructorDTO.setId(instructorDAO.obtainId(""));
            instructorDTO.setPersonalNumber("");
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }

        return instructorDTO;

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                "Menú principal", event);

    }

    private boolean validateEmptyFields() {
        boolean thereAreEmptyFields = false;

        if (txtTitle.getText().isBlank() ||
                taDescription.getText().isBlank() ||
                dpSubmissionDate.getValue() == null) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            thereAreEmptyFields = true;
        }

        return thereAreEmptyFields;

    }

    private void configureDatePicker() {
        dpSubmissionDate.setConverter(new StringConverter<LocalDate>() {
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
