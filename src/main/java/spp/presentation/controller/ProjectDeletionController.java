package spp.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectDeletionController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<ProjectDTO> tblProjects;
    @FXML private TableColumn<ProjectDTO, String> clmnName;
    @FXML private TableColumn<ProjectDTO, String> clmnDescription;
    @FXML private TableColumn<ProjectDTO, String> clmnAvailibility;
    @FXML private TableColumn<ProjectDTO, String> clmnOrganization;
    @FXML private TableColumn<ProjectDTO, String> clmnManager;


    private final ProjectDAO projectDAO = new ProjectDAO();
    private ObservableList<ProjectDTO> projectsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainProjects();
    }

    @FXML
    private void deleteProject(ActionEvent event) {
        ProjectDTO selectedProject = tblProjects.getSelectionModel().getSelectedItem();
        if (selectedProject == null) {
            showError("Seleccione el proyecto a eliminar");
            return;
        }

        if (AlertHelper.showConfirmation("Confirmar acción, " +
                "¿Seguro que desea eliminar el proyecto " + selectedProject.getId() + "?")) {
            try {
                if (projectDAO.deleteProject(selectedProject)) {
                    obtainProjects();
                    showSuccess("Proyecto eliminado exitosamente.");
                }
            } catch (DAOException e) {
                AppLogger.logError(e);
                showError("Error al eliminar el proyecto selecccionado.");
            }
        }
    }

    private void setUpColumns() {
        clmnName.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        clmnDescription.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        clmnAvailibility.setCellValueFactory(
                new PropertyValueFactory<>("availability"));
        clmnOrganization.setCellValueFactory(
                new PropertyValueFactory<>("organization"));
        clmnManager.setCellValueFactory(
                new PropertyValueFactory<>("manager"));
    }

    @FXML
    private void obtainProjects() {
        try {
            List<ProjectDTO> projectsList = projectDAO.obtainAllProjects();
            projectsObservableList = FXCollections.observableList(projectsList);
            tblProjects.setItems(projectsObservableList);
        } catch (DAOException e) {
            showError("Error al obtener la lista de proyectos.");
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml", "Cancelar", event);
    }

    private void showSuccess(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("success");
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("error");
    }

    private void clearStatus() {
        if (lblStatus != null) {
            lblStatus.setText("");
            lblStatus.getStyleClass().removeAll("error", "success");
        }
    }
}