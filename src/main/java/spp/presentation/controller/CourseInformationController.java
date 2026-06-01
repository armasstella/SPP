package spp.presentation.controller;


import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class CourseInformationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<CourseDTO> tblCourses;
    @FXML private TableColumn<CourseDTO, Integer> colCourseCode;
    @FXML private TableColumn<CourseDTO, String> colTerm;
    @FXML private TableColumn<CourseDTO, Integer> colSchoolBlock;
    @FXML private TableColumn<CourseDTO, Integer> colSection;
    @FXML private TableColumn<CourseDTO, String> colInstructor;
    @FXML private TableColumn<CourseDTO, Integer> colNumberOfInterns;
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainCourses();

    }

    private void setUpColumns() {
        colCourseCode.setCellValueFactory(
                new PropertyValueFactory<>("courseCode"));
        colTerm.setCellValueFactory(
                new PropertyValueFactory<>("term"));
        colSchoolBlock.setCellValueFactory(
                new PropertyValueFactory<>("schoolBlock"));
        colSection.setCellValueFactory(
                new PropertyValueFactory<>("section"));
        colInstructor.setCellValueFactory(
                cellData -> {
                    InstructorDTO instructorDTO = cellData.getValue().getInstructorDTO();
                    String name = (instructorDTO != null) ? instructorDTO.getFirstName() :
                            "Sin profesor";
                    return new SimpleStringProperty(name);
                });
        colNumberOfInterns.setCellValueFactory(
                new PropertyValueFactory<>("numberOfInterns"));

    }

    @FXML
    private void obtainCourses() {
        try {
            List<CourseDTO> courseDTOList = courseDAO.obtainAllActiveCourses();
            ObservableList<CourseDTO> coursesObservableList = FXCollections.observableArrayList(courseDTOList);
            tblCourses.setItems(coursesObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al obtener lista de cursos");
        }

    }

    @FXML
    private void goToNewCourseView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewCourseView.fxml",
                "Registrar Curso", event);

    }

    @FXML
    private void goToAssignInstructor(ActionEvent event) {
        CourseDTO courseSelected = tblCourses.getSelectionModel().getSelectedItem();

        if (courseSelected == null) {
            StatusLabel.showError(lblStatus, "Debe seleccionar un curso primero");
            return;
        }

        GroupAssignationToInstructorController groupAssignationToInstructorController = ViewNavigator.loadView(
                "/spp/presentation/view/GroupAssignationToInstructorView.fxml",
                "Asignar Profesor", event);

        if (groupAssignationToInstructorController != null) {
            groupAssignationToInstructorController.setCourseInEdition(courseSelected);
        }

    }

    @FXML
    private void goBackToMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Menú Principal", event);

    }

}