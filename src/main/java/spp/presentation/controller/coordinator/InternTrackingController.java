package spp.presentation.controller.coordinator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dto.InternTrackingCourseEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.ViewConstant;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InternTrackingController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<InternTrackingCourseEnrollmentDTO> tblInterns;
    @FXML private TableColumn<InternTrackingCourseEnrollmentDTO, String> colStudentNumber;
    @FXML private TableColumn<InternTrackingCourseEnrollmentDTO, String> colFullName;
    @FXML private TableColumn<InternTrackingCourseEnrollmentDTO, String> colEmail;
    @FXML private TableColumn<InternTrackingCourseEnrollmentDTO, String> colAssignedProject;
    @FXML private TableColumn<InternTrackingCourseEnrollmentDTO, String> colProjectAddress;
    @FXML private TableColumn<InternTrackingCourseEnrollmentDTO, String> colCompletedHours;
    @FXML private TableColumn<InternTrackingCourseEnrollmentDTO, String> colPhaseStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
    }

    private void setUpColumns() {
        colStudentNumber.setCellValueFactory(
                new GenericNestedSelector<>("studentNumber", "Sin matrícula"));
        colFullName.setCellValueFactory(
                new GenericNestedSelector<>("fullName", "Sin nombre"));
        colEmail.setCellValueFactory(
                new GenericNestedSelector<>("email", "Sin correo"));
        colAssignedProject.setCellValueFactory(
                new GenericNestedSelector<>("nameProjectAssigned", "Sin proyecto"));
        colProjectAddress.setCellValueFactory(
                new GenericNestedSelector<>("addressProject", "Sin dirección"));
        colCompletedHours.setCellValueFactory(
                new GenericNestedSelector<>("completedHours", "0"));
        colPhaseStatus.setCellValueFactory(
                new GenericNestedSelector<>("enrollmentPhase", "Desconocido"));
    }

    public void setCourseData(int courseId) {
        if (courseId > ViewConstant.ID_ZERO_INVALID) {
            this.obtainInterns(courseId);
        } else {
            StatusLabel.showError(lblStatus, "El identificador del curso es inválido");
        }
    }

    private void obtainInterns(int courseId) {
        CourseDAO courseDAO = new CourseDAO();
        try {
            List<InternTrackingCourseEnrollmentDTO> internDTOList = courseDAO.getTrackingByCourseId(courseId);
            ObservableList<InternTrackingCourseEnrollmentDTO> internsObservableList = FXCollections.observableArrayList(internDTOList);
            tblInterns.setItems(internsObservableList);
        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, exception.getMessage());
        }
    }

    @FXML
    private void goBackToMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CourseInformationView.fxml",
                "Información de Cursos", event);
    }
}