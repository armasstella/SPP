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
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.table.DoubleClickListener;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.table.TableViewConfigurator;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AvailableProjectsController implements Initializable, DoubleClickListener<ProjectDTO> {

    @FXML private Label lblCounter;
    @FXML private Label lblStatus;
    @FXML private TableView<ProjectDTO> tblChosenProjects;
    @FXML private TableColumn<ProjectDTO, String> colChosenName;
    @FXML private TableColumn<ProjectDTO, String> colChosenAvailability;
    @FXML private TableView<ProjectDTO> tblProjects;
    @FXML private TableColumn<ProjectDTO, String> colName;
    @FXML private TableColumn<ProjectDTO, String> colAvailability;
    private ObservableList<ProjectDTO> availableProjectsObservableList;
    private ObservableList<ProjectDTO> chosenProjectsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        setUpChosenProjects();
        obtainProjects();
        TableViewConfigurator.enableDoubleClickSelection(tblProjects, this);
        updateCounter();
    }

    @Override
    public void onItemSelected(ProjectDTO selectedItem) {
        if (selectedItem != null) {
            openProjectDetail(selectedItem);
        }
    }

    private void setUpColumns() {
        GenericNestedSelector<ProjectDTO> nameSelector =
                new GenericNestedSelector<>("name", "Sin nombre");
        GenericNestedSelector<ProjectDTO> availabilitySelector =
                new GenericNestedSelector<>("availability", "Sin disponibilidad");

        colName.setCellValueFactory(nameSelector);
        colAvailability.setCellValueFactory(availabilitySelector);
        colChosenName.setCellValueFactory(nameSelector);
        colChosenAvailability.setCellValueFactory(availabilitySelector);
    }

    private void setUpChosenProjects() {
        chosenProjectsObservableList = FXCollections.observableArrayList();
        tblChosenProjects.setItems(chosenProjectsObservableList);
    }

    @FXML
    private void obtainProjects() {
        ProjectDAO projectDAO = new ProjectDAO();
        try {
            List<ProjectDTO> projectList = projectDAO.findProjectsDetailsForActiveTerm();
            availableProjectsObservableList = FXCollections.observableArrayList(projectList);
            tblProjects.setItems(availableProjectsObservableList);
        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, "Error al obtener proyectos");
        }
    }

    private void openProjectDetail(ProjectDTO project) {
        int chosenSize = chosenProjectsObservableList.size();
        boolean selectionAllowed = chosenSize < ViewConstant.MAX_CHOSEN_PROJECTS;

        try {
            URL detailViewUrl = getClass().getResource("/spp/presentation/view/coordinator/ProjectDetailView.fxml");
            FXMLLoader loader = new FXMLLoader(detailViewUrl);
            Parent detailRoot = loader.load();

            ProjectDetailController detailController = loader.getController();
            detailController.setProject(project);
            detailController.setSelectionAllowed(selectionAllowed);

            Stage detailStage = new Stage();
            detailStage.setTitle("Detalle del Proyecto");
            detailStage.initModality(Modality.APPLICATION_MODAL);

            Scene detailScene = new Scene(detailRoot);
            detailStage.setScene(detailScene);
            detailStage.showAndWait();

            boolean isProjectSelected = detailController.isProjectSelected();

            if (isProjectSelected) {
                moveProjectToChosen(project);
            }
        } catch (IOException exception) {
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
        int chosenSize = chosenProjectsObservableList.size();
        String counterText = chosenSize + " de " + ViewConstant.MAX_CHOSEN_PROJECTS + " proyectos elegidos.";
        lblCounter.setText(counterText);
    }

    @FXML
    private void finishSelection(ActionEvent event) {
        boolean isListEmpty = chosenProjectsObservableList.isEmpty();

        if (isListEmpty) {
            StatusLabel.showError(lblStatus, "Debes elegir al menos un proyecto.");
        } else {
            executeProjectSelection(event);
        }
    }

    private void executeProjectSelection(ActionEvent event) {
        PrioritizedProjectDAO prioritizedProjectDAO = new PrioritizedProjectDAO();

        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            List<ProjectDTO> chosenProjectsList = new ArrayList<>(chosenProjectsObservableList);

            boolean isSaved = prioritizedProjectDAO.savePrioritizedProjects(userEmail, chosenProjectsList);

            if (isSaved) {
                AlertHelper.showMessage("Elección finalizada", "Tu elección de proyectos se guardó correctamente.");
                ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml", "Menú Practicante", event);
            }
        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, "Error al guardar la elección");
        }
    }

    @FXML
    private void goToInternMenuView(ActionEvent event) {
        boolean shouldNavigate = true;
        boolean hasChosenProjects = !chosenProjectsObservableList.isEmpty();

        if (hasChosenProjects) {
            String alertTitle = "Regresar";
            String alertMessage = "Los proyectos elegidos se perderán. ¿Deseas continuar?";
            boolean isConfirmed = AlertHelper.showConfirmation(alertTitle, alertMessage);

            shouldNavigate = isConfirmed;
        }

        if (shouldNavigate) {
            ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml", "Menú Practicante", event);
        }
    }
}