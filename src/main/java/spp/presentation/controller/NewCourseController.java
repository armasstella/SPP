package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;


public class NewCourseController {

    @FXML TextField txtCourseCode;
    @FXML ComboBox<String> cmbTerm;
    @FXML ComboBox<String> cmbSchoolBlock;
    @FXML ComboBox<String> cmbSection;
    @FXML TextField txtSection;
    @FXML ComboBox<InstructorDTO> cmbInstructor;
    @FXML Label lblStatus;
    private final CourseDAO courseDAO = new CourseDAO();

    private CourseDTO buildCourseDTO() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCourseCode(Integer.parseInt(txtCourseCode.getText().trim()));
        courseDTO.setTerm(String.valueOf(cmbTerm.getSelectionModel().getSelectedItem()));
        courseDTO.setSchoolBlock(Integer.parseInt(cmbSchoolBlock.getSelectionModel().getSelectedItem()));
        courseDTO.setSection(Integer.parseInt(cmbSection.getSelectionModel().getSelectedItem()));
        courseDTO.setInstructor(cmbInstructor.getSelectionModel().getSelectedItem());

        return courseDTO;

    }

    @FXML
    private void saveCourse(ActionEvent event) {
        if (validateEmptyFields()) {
            return;
        }

    /*    try {
            if (courseDAO.addCourse(buildCourseDTO())) {
                showSuccess("Curso registrado correctamente.");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    */
    }

    private void clearInputFields() {
        txtCourseCode.clear();
        cmbTerm.getItems().clear();
        cmbSection.getItems().clear();
        cmbSchoolBlock.getItems().clear();
        cmbInstructor.getItems().clear();

    }

    private boolean validateEmptyFields() {
        boolean thereAreEmptyFields = false;

        if (txtCourseCode.getText().isBlank() ||
                cmbTerm.getItems().isEmpty() ||
                cmbSchoolBlock.getItems().isEmpty() ||
                txtSection.getText().isBlank() ||
                cmbInstructor.getItems().isEmpty()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            thereAreEmptyFields = true;
        }

        return thereAreEmptyFields;

    }

    @FXML
    private void goBackToCourseInformationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CourseInformationView.fxml",
                "Cursos", event);

    }

}
