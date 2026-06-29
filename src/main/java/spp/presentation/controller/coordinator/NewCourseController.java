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
import spp.businesslogic.dao.TermDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class NewCourseController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtCourseCode;
    @FXML private TextField txtTerm;
    @FXML private ComboBox<String> cmbSchoolBlock;
    @FXML private ComboBox<String> cmbSection;
    @FXML private ComboBox<InstructorDTO> cmbInstructor;
    @FXML private TextField txtCapacity;
    @FXML private TextArea taCourseDetails;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadActiveInstructors();
        loadActiveTerm();
        setUpFields();
    }

    private void loadActiveTerm() {
        txtTerm.setText(ActiveSessionDTO.get().getActiveTerm());
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtCourseCode,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_NRC);
        InputFilter.applyFormatFilter(txtTerm,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_TERM);
        InputFilter.applyFormatFilter(txtCapacity,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY);
        InputFilter.applyFormatFilter(taCourseDetails,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
    }

    private void setAllCourse(CourseDTO courseDTO) {
        courseDTO.setCourseCode(Integer.parseInt(txtCourseCode.getText().trim()));
        courseDTO.setSchoolBlock(Integer.parseInt(cmbSchoolBlock.getValue()));
        courseDTO.setSection(Integer.parseInt(cmbSection.getValue()));
        courseDTO.setCapacity(Integer.parseInt(txtCapacity.getText().trim()));
        courseDTO.setCourseDetails(taCourseDetails.getText().trim());
        courseDTO.setInstructorDTO(cmbInstructor.getValue());
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtCourseCode.getText().isBlank() ||
                txtTerm.getText().isBlank() ||
                cmbSchoolBlock.getValue() == null ||
                cmbSection.getValue() == null ||
                txtCapacity.getText().isBlank()) {

            emptyFields = true;
        }

        return emptyFields;
    }

    private boolean hasValidMinimumLengths() {
        boolean validLengths = false;

        boolean validCourseCode = InputFilter.hasMinimumLength(txtCourseCode,
                ViewConstant.MIN_LENGTH_NRC);

        boolean validTerm = InputFilter.hasMinimumLength(txtTerm,
                ViewConstant.MIN_LENGTH_TERM);

        if (validCourseCode && validTerm) {
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
    private void saveCourse(ActionEvent event) {
        if (areValidFields()) {
            CourseDTO courseDTO = new CourseDTO();
            setAllCourse(courseDTO);

            if (courseDTO.isValid()) {
                CourseDAO courseDAO = new CourseDAO();
                setAllCourse(courseDTO);
                TermDAO termDAO = new TermDAO();
                try {
                    int activeTermId = termDAO.findActiveTermId();
                    if (courseDAO.registerCourse(courseDTO, activeTermId)) {
                        boolean savedWithoutInstructor = (cmbInstructor.getValue() == null);
                        String successMessage;

                        if (savedWithoutInstructor) {
                            successMessage = "Curso registrado.\nRecuerde asignar un profesor posteriormente.";
                        } else {
                            successMessage = "Curso registrado correctamente.";
                        }

                        StatusLabel.showSuccess(lblStatus, successMessage);
                        clearInputFields();
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, e.getMessage());
                }
            } else {
                String errorMessages = String.join("\n• ", courseDTO.getErrors());
                StatusLabel.showError(lblStatus, "Corrige los siguientes formatos:\n• " + errorMessages);
            }
        }
    }

    private void loadActiveInstructors() {
        try {
            InstructorDAO instructorDAO = new InstructorDAO();
            List<InstructorDTO> activeInstructors = instructorDAO.getActiveInstructorsIdentifiers();
            ObservableList<InstructorDTO> instructorObservableList = FXCollections.observableArrayList(activeInstructors);

            cmbInstructor.setItems(instructorObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    @FXML
    private void goBackToCourseInformationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CourseInformationView.fxml",
                "Cursos", event);
    }

    private void clearInputFields() {
        txtCourseCode.clear();
        cmbSection.setValue(null);
        cmbSchoolBlock.setValue(null);
        txtCapacity.clear();
        taCourseDetails.clear();
        cmbInstructor.setValue(null);
    }
}
