package spp.presentation.controller.intern;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectDAO;
import spp.businesslogic.dao.PrioritizedProjectDAO;
import spp.presentation.controller.coordinator.ProjectDetailController;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class AvailableProjectsController implements Initializable {

    @FXML private Label lblCounter;
    @FXML private Label lblStatus;
    @FXML private TableView<ProjectDTO> tblChosenProjects;
    @FXML private TableColumn<ProjectDTO, String> colChosenName;
    @FXML private TableColumn<ProjectDTO, String> colChosenAvailability;
    @FXML private TableView<ProjectDTO> tblProjects;
    @FXML private TableColumn<ProjectDTO, String> colName;
    @FXML private TableColumn<ProjectDTO, String> colAvailability;
    private static final int MAX_CHOSEN_PROJECTS = 3;
    private final ProjectDAO projectDAO = new ProjectDAO();
    private final PrioritizedProjectDAO prioritizedProjectDAO = new PrioritizedProjectDAO();
    private ObservableList<ProjectDTO> availableProjectsObservableList;
    private ObservableList<ProjectDTO> chosenProjectsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        setUpChosenProjects();
        obtainProjects();
        setUpClickOnProject();
        updateCounter();

    }

    private void setUpColumns() {
        colName.setCellValueFactory(
                new GenericNestedSelector<>("name", "Sin nombre"));
        colAvailability.setCellValueFactory(
                new GenericNestedSelector<>("availability", "Sin disponibilidad"));
        colChosenName.setCellValueFactory(
                new GenericNestedSelector<>("name", "Sin nombre"));
        colChosenAvailability.setCellValueFactory(
                new GenericNestedSelector<>("availability", "Sin disponibilidad"));

    }

    private void setUpChosenProjects() {
        chosenProjectsObservableList = FXCollections.observableArrayList();
        tblChosenProjects.setItems(chosenProjectsObservableList);

    }

    private void setUpClickOnProject() {
        tblProjects.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && tblProjects.getSelectionModel().getSelectedItem() != null) {
                ProjectDTO selectedProject = tblProjects.getSelectionModel().getSelectedItem();
                openProjectDetail(selectedProject);
            }
        });

    }

    @FXML
    private void obtainProjects() {
        try {
            List<ProjectDTO> projectList = projectDAO.findProjectsDetailsForActiveTerm();
            availableProjectsObservableList = FXCollections.observableArrayList(projectList);
            tblProjects.setItems(availableProjectsObservableList);
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al obtener proyectos");
        }

    }

    private void openProjectDetail(ProjectDTO project) {
        boolean selectionAllowed = chosenProjectsObservableList.size() < MAX_CHOSEN_PROJECTS;
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/spp/presentation/view/coordinator/ProjectDetailView.fxml"));
            Parent detailRoot = loader.load();
            ProjectDetailController detailController = loader.getController();
            detailController.setProject(project);
            detailController.setSelectionAllowed(selectionAllowed);

            Stage detailStage = new Stage();
            detailStage.setTitle("Detalle del Proyecto");
            detailStage.initModality(Modality.APPLICATION_MODAL);
            detailStage.setScene(new Scene(detailRoot));
            detailStage.showAndWait();

            if (detailController.isProjectSelected()) {
                moveProjectToChosen(project);
            }
        } catch (IOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al abrir el detalle del proyecto");
        }

    }

    private void moveProjectToChosen(ProjectDTO project) {
        availableProjectsObservableList.remove(project);
        chosenProjectsObservableList.add(project);
        updateCounter();
        StatusLabel.clear(lblStatus);

    }

    private void updateCounter() {
        lblCounter.setText(chosenProjectsObservableList.size()
                + " de " + MAX_CHOSEN_PROJECTS + " proyectos elegidos.");

    }

    @FXML
    private void finishSelection(ActionEvent event) {
        if (chosenProjectsObservableList.isEmpty()) {
            StatusLabel.showError(lblStatus, "Debes elegir al menos un proyecto.");
            return;
        }

        try {
            if (prioritizedProjectDAO.savePrioritizedProjects(
                    ActiveSessionDTO.get().getEmail(), new ArrayList<>(chosenProjectsObservableList))) {
                AlertHelper.showMessage("Elección finalizada",
                        "Tu elección de proyectos se guardó correctamente.");
                ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                        "Menú Practicante", event);
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al guardar la elección");
        }

    }

    @FXML
    private void goToInternMenuView(ActionEvent event) {
        if (!chosenProjectsObservableList.isEmpty()
                && !AlertHelper.showConfirmation("Regresar",
                "Los proyectos elegidos se perderán. ¿Deseas continuar?")) {
            return;
        }

        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);

    }

}