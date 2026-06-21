package spp.presentation.controller.coordinator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dao.InstructorDAO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.logger.AppLogger;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class NewCourseController implements Initializable {

    @FXML Label lblStatus;
    @FXML TextField txtCourseCode;
    @FXML TextField txtTerm;
    @FXML ComboBox<String> cmbSchoolBlock;
    @FXML ComboBox<String> cmbSection;
    @FXML ComboBox<InstructorDTO> cmbInstructor;
    @FXML TextField txtCapacity;
    @FXML TextArea taCourseDetails;
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadActiveInstructors();
        setUpFields();

    }

    private void setUpFields() {
        InputFilter.applyFilter(txtCourseCode, InputFilter.NUMERIC_PATTERN, 6);
        InputFilter.applyFilter(txtTerm, InputFilter.ALPHANUMERIC_PATTERN, 10);
        InputFilter.applyFilter(txtCapacity, InputFilter.NUMERIC_PATTERN, 2);
        InputFilter.applyFilter(taCourseDetails, InputFilter.ALPHANUMERIC_PATTERN, 40);

    }

    private CourseDTO buildCourseDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseCode(Integer.parseInt(txtCourseCode.getText().trim()));
        courseDTO.setTerm(txtTerm.getText().trim());
        courseDTO.setSchoolBlock(Integer.parseInt(cmbSchoolBlock.getSelectionModel().getSelectedItem()));
        courseDTO.setSection(Integer.parseInt(cmbSection.getSelectionModel().getSelectedItem()));
        courseDTO.setCapacity(Integer.parseInt(txtCapacity.getText().trim()));
        courseDTO.setCourseDetails(taCourseDetails.getText().trim());
        courseDTO.setInstructorDTO(cmbInstructor.getSelectionModel().getSelectedItem());

        return courseDTO;

    }

    @FXML
    private void saveCourse(ActionEvent event) {
        if (validateEmptyFields()) {
            return;
        }

        boolean savedWithoutInstructor = (cmbInstructor.getValue() == null);

        try {
            if (courseDAO.registerCourse(buildCourseDTO())) {
                String successMessage = savedWithoutInstructor ?
                        "Curso registrado\nRecuerde asignar un profesor posteriormente" :
                        "Curso registrado correctamente";

                StatusLabel.showSuccess(lblStatus, successMessage);
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, String.valueOf(e.getMessage()));
        }

    }

    private void clearInputFields() {
        txtCourseCode.clear();
        txtTerm.clear();
        cmbSection.setValue(null);
        cmbSchoolBlock.setValue(null);
        txtCapacity.clear();
        taCourseDetails.clear();
        cmbInstructor.setValue(null);

    }

    private boolean validateEmptyFields() {
        boolean thereAreEmptyFields = false;

        if (txtCourseCode.getText().isBlank() ||
                txtTerm.getText().isBlank() ||
                cmbSchoolBlock.getValue() == null ||
                cmbSection.getValue() == null ||
                txtCapacity.getText().isBlank() ||
                txtCourseCode.getText().isBlank()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            thereAreEmptyFields = true;
        }

        return thereAreEmptyFields;

    }

    @FXML
    private void goBackToCourseInformationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CourseInformationView.fxml",
                "Cursos", event);

    }

    private void loadActiveInstructors() {
        try {
            InstructorDAO instructorDAO = new InstructorDAO();
            List<InstructorDTO> activeInstructors = instructorDAO.getActiveInstructorsIdentifiers();
            ObservableList<InstructorDTO> instructorObservableList =
                    FXCollections.observableArrayList(activeInstructors);
            cmbInstructor.setItems(instructorObservableList);

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al cargar lista de profesores");
        }

    }

}
