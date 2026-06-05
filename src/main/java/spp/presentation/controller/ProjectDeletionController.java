package spp.presentation.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ProjectDeletionController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<ProjectDTO> tblProjects;
    @FXML private TableColumn<ProjectDTO, String> colName;
    @FXML private TableColumn<ProjectDTO, String> colDescription;
    @FXML private TableColumn<ProjectDTO, String> colAvailability;
    @FXML private TableColumn<ProjectDTO, String> colPlacesAvailable;
    @FXML private TableColumn<ProjectDTO, String> colLinkedOrganization;
    @FXML private TableColumn<ProjectDTO, String> colProjectManager;
    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainProjects();
    }

    @FXML
    public void deleteProject(ActionEvent event) {
        ProjectDTO projectSelected = tblProjects.getSelectionModel().getSelectedItem();

        if(projectSelected == null) {
            StatusLabel.showError(lblStatus, "Seleccione el proyecto a eliminar.");
            return;
        }

        if (AlertHelper.showConfirmation("Confirmar acción",
                "¿Seguro que desea eliminar el proyecto: \"" + projectSelected.getName() + "\"?")) {
            try {
                if (projectDAO.deleteProject(projectSelected)) {
                    obtainProjects();
                    StatusLabel.showSuccess(lblStatus, "Proyecto eliminado exitosamente.");
                }
            } catch (DAOException e) {
                AppLogger.logError(e);
                StatusLabel.showError(lblStatus, "Error al eliminar proyecto.");
            }
        }

    }

    private void obtainProjects() {
        try {
            List<ProjectDTO> projectsList = projectDAO.obtainAllProjects();
            ObservableList<ProjectDTO> projectsObservableList = FXCollections.observableArrayList(projectsList);
            tblProjects.setItems(projectsObservableList);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al obtener lista de proyectos.");
        }

    }

    private void setUpColumns() {
        colName.setCellValueFactory(
                new GenericNestedSelector<>("name", "Sin nombre"));
        colDescription.setCellValueFactory(
                new GenericNestedSelector<>("description", "Sin descripción"));
        colPlacesAvailable.setCellValueFactory(
                new GenericNestedSelector<>("placesAvailable", "0"));
        colAvailability.setCellValueFactory(
                new GenericNestedSelector<>("availability", "No definido"));
        colLinkedOrganization.setCellValueFactory(
                new GenericNestedSelector<>("linkedOrganizationDTO.name", "Sin organización vinculada"));
        colProjectManager.setCellValueFactory(
                new GenericNestedSelector<>("projectManagerDTO.firstName", "Sin encargado"));
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Cancelar", event);

    }

}
