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
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
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
                new PropertyValueFactory<>("name"));
        colDescription.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        colPlacesAvailable.setCellValueFactory(
                new PropertyValueFactory<>("placesAvailable"));
        colAvailability.setCellValueFactory(
                new PropertyValueFactory<>("availability"));
        colLinkedOrganization.setCellValueFactory(
                cellData -> {
                    LinkedOrganizationDTO linkedOrganizationDTO = cellData.getValue().getLinkedOrganizationDTO();;
                    String name = (linkedOrganizationDTO != null) ? linkedOrganizationDTO.getName() :
                            "Sin organización vinculada";
                    return new SimpleStringProperty(name);
                });
        colProjectManager.setCellValueFactory(
                cellData -> {
                    ProjectManagerDTO projectManagerDTO = cellData.getValue().getProjectManagerDTO();
                    String name = (projectManagerDTO != null) ? projectManagerDTO.getFirstName() :
                            "Sin encargado";
                    return new SimpleStringProperty(name);
                }
        );

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Cancelar", event);

    }

}
