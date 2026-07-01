package spp.presentation.controller.coordinator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.table.DoubleClickListener;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.table.TableViewConfigurator;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class CourseInformationController implements Initializable, DoubleClickListener<CourseDTO> {

    @FXML private BorderPane rootMenuPane;
    @FXML private Label lblStatus;
    @FXML private TableView<CourseDTO> tblCourses;
    @FXML private TableColumn<CourseDTO, String> colCourseCode;
    @FXML private TableColumn<CourseDTO, String> colTerm;
    @FXML private TableColumn<CourseDTO, String> colSchoolBlock;
    @FXML private TableColumn<CourseDTO, String> colSection;
    @FXML private TableColumn<CourseDTO, String> colInstructor;
    @FXML private TableColumn<CourseDTO, String> colNumberOfInterns;

    private CourseDTO selectedCourse;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainCourses();
        TableViewConfigurator.enableDoubleClickSelection(tblCourses, this);
    }

    @Override
    public void onItemSelected(CourseDTO clickedCourse) {
        this.selectedCourse = clickedCourse;
        Stage currentStage = (Stage) rootMenuPane.getScene().getWindow();
        InternTrackingController internTrackingController = ViewNavigator.loadView("/spp/presentation/view/coordinator/InternsTrackingView.fxml",
                "Asignar Profesor", currentStage);
        internTrackingController.setCourseData(clickedCourse.getIdCourse());
    }

    private void setUpColumns() {
        colCourseCode.setCellValueFactory(
                new GenericNestedSelector<>("courseCode", "Sin NRC"));
        colTerm.setCellValueFactory(
                new GenericNestedSelector<>("termDTO.name", "Sin periodo"));
        colSchoolBlock.setCellValueFactory(
                new GenericNestedSelector<>("schoolBlock", "Sin bloque"));
        colSection.setCellValueFactory(
                new GenericNestedSelector<>("section", "Sin sección"));
        colInstructor.setCellValueFactory(
                new GenericNestedSelector<>("instructorDTO.firstName", "Sin profesor"));
        colNumberOfInterns.setCellValueFactory(
                new GenericNestedSelector<>("numberOfInterns", "0"));

    }

    @FXML
    private void obtainCourses() {
        CourseDAO courseDAO = new CourseDAO();
        try {
            List<CourseDTO> courseDTOList = courseDAO.getActiveCoursesStatistics();
            ObservableList<CourseDTO> coursesObservableList = FXCollections.observableArrayList(courseDTOList);
            tblCourses.setItems(coursesObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    @FXML
    private void goToNewCourseView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/NewCourseView.fxml",
                "Registrar Curso", event);
    }

    @FXML
    private void goToAssignInstructor(ActionEvent event) {
        CourseDTO courseSelected = tblCourses.getSelectionModel().getSelectedItem();

        if (courseSelected == null) {
            StatusLabel.showError(lblStatus, "Debe seleccionar un curso primero");
        } else {
            GroupAssignationToInstructorController groupAssignationToInstructorController = ViewNavigator.loadView(
                    "/spp/presentation/view/coordinator/GroupAssignationToInstructorView.fxml",
                    "Asignar Profesor", event);

            if (groupAssignationToInstructorController != null) {
                groupAssignationToInstructorController.setCourseInEdition(courseSelected);
            }   
        }

    }

    @FXML
    private void goBackToMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Principal", event);

    }

}
