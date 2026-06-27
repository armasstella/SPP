package spp.presentation.controller.coordinator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dao.InstructorDAO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.AlertHelper;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class GroupAssignationToInstructorController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtCourseCode;
    @FXML private TextField txtTerm;
    @FXML private TextField txtSchoolBlock;
    @FXML private TextField txtSection;
    @FXML private ComboBox<InstructorDTO> cmbInstructor;
    private CourseDTO courseInEdition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadActiveInstructorsInComboBox();
    }

    public void setCourseInEdition(CourseDTO courseInEdition) {
        this.courseInEdition = courseInEdition;
        showDataInFields();
    }

    private void showDataInFields() {
        if (this.courseInEdition != null) {
            txtCourseCode.setText(String.valueOf(courseInEdition.getCourseCode()));
            txtSchoolBlock.setText(String.valueOf(courseInEdition.getSchoolBlock()));
            txtSection.setText(String.valueOf(courseInEdition.getSection()));
        }
    }

    @FXML
    private void assignInstructor(ActionEvent event) {
        InstructorDTO selectedInstructor = cmbInstructor.getValue();

        if (selectedInstructor == null) {
            StatusLabel.showError(lblStatus, "Debe seleccionar un profesor");
        } else {
            this.courseInEdition.setInstructorDTO(selectedInstructor);

            try {
                CourseDAO courseDAO = new CourseDAO();

                if (AlertHelper.showConfirmation("Confirmar operación",
                        "¿Seguro que desea asignar el profesor?")) {
                    if (courseDAO.assignInstructorToCourse(this.courseInEdition)) {
                        ViewNavigator.loadView(
                                "/spp/presentation/view/coordinator/CourseInformationView.fxml",
                                "Información de Cursos", event);
                    }
                }

            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "Error al asignar profesor a curso");
            }
        }
    }

    @FXML
    private void cancel (ActionEvent event) {
        ViewNavigator.loadView(
                "/spp/presentation/view/coordinator/CourseInformationView.fxml",
                "Información de Cursos", event);
    }

    private void loadActiveInstructorsInComboBox() {
        try {
            InstructorDAO instructorDAO = new InstructorDAO();
            List<InstructorDTO> activeInstructors = instructorDAO.getActiveInstructorsIdentifiers();
            ObservableList<InstructorDTO> instructorObservableList =
                    FXCollections.observableArrayList(activeInstructors);
            cmbInstructor.setItems(instructorObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al cargar lista de profesores");
        }

    }


}
