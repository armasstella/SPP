package spp.presentation.controller.coordinator;

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
import spp.businesslogic.dao.PrioritizedProjectDAO;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InternDAO;
import spp.utils.view.table.DoubleClickListener;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.table.TableViewConfigurator;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectAssignmentController implements Initializable, DoubleClickListener<InternDTO> {

    @FXML private Label lblStatus;
    @FXML private TableView<InternDTO> tblInterns;
    @FXML private TableColumn<InternDTO, String> colStudentNumber;
    @FXML private TableColumn<InternDTO, String> colName;
    @FXML private Label lblNoSelection;
    @FXML private ToggleGroup projectToggleGroup;
    @FXML private ToggleButton tglFirstSelectedProject;
    @FXML private ToggleButton tglSecondSelectedProject;
    @FXML private ToggleButton tglThirdSelectedProject;

    private List<ToggleButton> projectToggleButtons;
    private InternDTO selectedIntern;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        setUpProjectToggleButtons();
        obtainInterns();
        TableViewConfigurator.enableDoubleClickSelection(tblInterns, this);
    }

    @Override
    public void onItemSelected(InternDTO clickedIntern) {
        this.selectedIntern = clickedIntern;
        displaySelectedProjects(clickedIntern);
    }

    private void setUpColumns() {
        GenericNestedSelector<InternDTO> studentNumberSelector =
                new GenericNestedSelector<>("studentNumber", "Sin matrícula");
        GenericNestedSelector<InternDTO> fullNameSelector =
                new GenericNestedSelector<>("fullName", "Sin nombre");

        colStudentNumber.setCellValueFactory(studentNumberSelector);
        colName.setCellValueFactory(fullNameSelector);
    }

    private void setUpProjectToggleButtons() {
        projectToggleButtons = List.of(
                tglFirstSelectedProject,
                tglSecondSelectedProject,
                tglThirdSelectedProject
        );
    }

    @FXML
    private void obtainInterns() {
        InternDAO internDAO = new InternDAO();
        try {
            List<InternDTO> internList = internDAO.findUnassignedInternsIdentifiers();
            ObservableList<InternDTO> internsObservableList = FXCollections.observableArrayList(internList);
            tblInterns.setItems(internsObservableList);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void displaySelectedProjects(InternDTO intern) {
        clearProjectSelection();
        lblStatus.setText("");

        PrioritizedProjectDAO prioritizedProjectDAO = new PrioritizedProjectDAO();
        try {
            String studentNumber = intern.getStudentNumber();
            List<ProjectDTO> selectedProjectList =
                    prioritizedProjectDAO.findPrioritizedProjectsIdentifiersByStudentNumber(studentNumber);

            if (selectedProjectList.isEmpty()) {
                hideAllProjectCards();
                lblNoSelection.setText("El practicante no ha seleccionado proyectos.");
                lblNoSelection.setVisible(true);
                lblNoSelection.setManaged(true);
            } else {
                lblNoSelection.setVisible(false);
                lblNoSelection.setManaged(false);
                fillProjectCards(selectedProjectList);
            }
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void fillProjectCards(List<ProjectDTO> selectedProjectList) {
        int totalProjects = selectedProjectList.size();

        for (int index = 0; index < projectToggleButtons.size(); index++) {
            ToggleButton projectToggleButton = projectToggleButtons.get(index);

            if (index < totalProjects) {
                ProjectDTO currentProject = selectedProjectList.get(index);
                String displayTitle = (index + 1) + ". " + currentProject.getName();

                projectToggleButton.setText(displayTitle);
                projectToggleButton.setUserData(currentProject);
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
        } else {
            Toggle selectedToggle = projectToggleGroup.getSelectedToggle();

            if (selectedToggle == null) {
                StatusLabel.showError(lblStatus, "Selecciona un proyecto para asignar.");
            } else {
                ProjectDTO selectedProject = (ProjectDTO) selectedToggle.getUserData();
                executeProjectAssignment(selectedProject);
            }
        }
    }

    private void executeProjectAssignment(ProjectDTO selectedProject) {
        ProfessionalPracticeEnrollmentDAO enrollmentDAO = new ProfessionalPracticeEnrollmentDAO();
        String studentNumber = selectedIntern.getStudentNumber();
        int projectId = selectedProject.getId();

        try {
            boolean isAssigned = enrollmentDAO.assignProjectByStudentNumber(studentNumber, projectId);

            if (isAssigned) {
                StatusLabel.showSuccess(lblStatus, "Proyecto asignado correctamente.");
                obtainInterns();
                hideAllProjectCards();
            }
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    @FXML
    private void goToCoordinatorMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
    }
}