package spp.presentation.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ProjectAssignmentController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<InternDTO> tblInterns;
    @FXML private TableColumn<InternDTO, String> colStudentNumber;
    @FXML private TableColumn<InternDTO, String> colName;
    @FXML private Label lblNoSelection;
    @FXML private ToggleGroup projectToggleGroup;
    @FXML private ToggleButton tglFirstSelectedProject;
    @FXML private ToggleButton tglSecondSelectedProject;
    @FXML private ToggleButton tglThirdSelectedProject;
    private final InternDAO internDAO = new InternDAO();
    private final ProjectDAO projectDAO = new ProjectDAO();
    private ObservableList<InternDTO> internsObservableList;
    private List<ToggleButton> projectToggleButtons;
    private InternDTO selectedIntern;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        setUpProjectToggleButtons();
        obtainInterns();
        setUpDoubleClickOnIntern();

    }

    private void setUpColumns() {
        colStudentNumber.setCellValueFactory(
                new GenericNestedSelector<>("studentNumber", "Sin matrícula"));
        colName.setCellValueFactory(
                new GenericNestedSelector<>("fullName", "Sin nombre"));

    }

    private void setUpProjectToggleButtons() {
        projectToggleButtons = List.of(tglFirstSelectedProject, tglSecondSelectedProject, tglThirdSelectedProject);

    }

    private void setUpDoubleClickOnIntern() {
        tblInterns.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tblInterns.getSelectionModel().getSelectedItem() != null) {
                selectedIntern = tblInterns.getSelectionModel().getSelectedItem();
                displaySelectedProjects(selectedIntern);
            }
        });

    }

    @FXML
    private void obtainInterns() {
        try {
            List<InternDTO> internList = internDAO.obtainInternsWithoutAssignedProject();
            internsObservableList = FXCollections.observableArrayList(internList);
            tblInterns.setItems(internsObservableList);
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al obtener practicantes");
        }

    }

    private void displaySelectedProjects(InternDTO intern) {
        clearProjectSelection();
        lblStatus.setText("");
        try {
            List<ProjectDTO> selectedProjectList =
                    projectDAO.obtainSelectedProjectsByIntern(intern.getStudentNumber());

            if (selectedProjectList.isEmpty()) {
                hideAllProjectCards();
                lblNoSelection.setText("El practicante no ha seleccionado proyectos.");
                lblNoSelection.setVisible(true);
                lblNoSelection.setManaged(true);
                return;
            }

            lblNoSelection.setVisible(false);
            lblNoSelection.setManaged(false);
            fillProjectCards(selectedProjectList);
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al obtener proyectos del practicante");
        }

    }

    private void fillProjectCards(List<ProjectDTO> selectedProjectList) {
        for (int index = 0; index < projectToggleButtons.size(); index++) {
            ToggleButton projectToggleButton = projectToggleButtons.get(index);
            if (index < selectedProjectList.size()) {
                ProjectDTO project = selectedProjectList.get(index);
                projectToggleButton.setText((index + 1) + ". " + project.getName());
                projectToggleButton.setUserData(project);
                projectToggleButton.setVisible(true);
                projectToggleButton.setManaged(true);
            } else {
                projectToggleButton.setUserData(null);
                projectToggleButton.setVisible(false);
                projectToggleButton.setManaged(false);
            }
        }

    }

    private void hideAllProjectCards() {
        for (ToggleButton projectToggleButton : projectToggleButtons) {
            projectToggleButton.setUserData(null);
            projectToggleButton.setVisible(false);
            projectToggleButton.setManaged(false);
        }

    }

    private void clearProjectSelection() {
        Toggle currentSelection = projectToggleGroup.getSelectedToggle();
        if (currentSelection != null) {
            currentSelection.setSelected(false);
        }

    }

    @FXML
    private void assignProject(ActionEvent event) {
        if (selectedIntern == null) {
            StatusLabel.showError(lblStatus, "Selecciona un practicante.");
            return;
        }

        Toggle selectedToggle = projectToggleGroup.getSelectedToggle();
        if (selectedToggle == null) {
            StatusLabel.showError(lblStatus, "Selecciona un proyecto para asignar.");
            return;
        }

        ProjectDTO selectedProject = (ProjectDTO) selectedToggle.getUserData();
        try {
            ProfessionalPracticeEnrollmentDAO professionalPracticeEnrollmentDAO =
                    new ProfessionalPracticeEnrollmentDAO();
            if (professionalPracticeEnrollmentDAO.assignProjectToInscription(selectedIntern.getStudentNumber(),
                    selectedProject.getId())) {
                StatusLabel.showSuccess(lblStatus, "Proyecto asignado correctamente.");
                obtainInterns();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al asignar el proyecto");
        }

    }

    @FXML
    private void goToCoordinatorMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);

    }

}