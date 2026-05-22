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
import spp.dataaccess.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectDeletionController implements Initializable {

    @FXML private TableView<ProjectDTO> tblProjects;
    @FXML private TableColumn<ProjectDTO, String> clmnName;
    @FXML private TableColumn<ProjectDTO, String> clmnDescription;
    @FXML private TableColumn<ProjectDTO, String> clmnAvailability;
    @FXML private TableColumn<ProjectDTO, String> clmnPlacesAvailable;
    @FXML private TableColumn<ProjectDTO, String> clmnLinkedOrganization;
    @FXML private TableColumn<ProjectDTO, String> clmnProjectManager;
    @FXML private Label lblStatus;

    private final ProjectDAO projectDAO = new ProjectDAO();
    private ObservableList<ProjectDTO> projectsObservableList;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainProjects();
    }

    @FXML
    public void deleteProject(ActionEvent event) {
        ProjectDTO projectSelected = tblProjects.getSelectionModel().getSelectedItem();
        if(projectSelected == null) {
            showError("Seleccione el proyecto a eliminar.");
            return;
        }

        if (AlertHelper.showConfirmation("Confirmar acción",
                "¿Seguro que desea eliminar el proyecto: \"" + projectSelected.getName() + "\"?")) {
            try {
                if (projectDAO.deleteProject(projectSelected)) {
                    obtainProjects();
                    showSuccess("Proyecto eliminado exitosamente.");
                }
            } catch (DAOException e) {
                AppLogger.logError(e);
                showError("Error al eliminar proyecto.");
            }
        }
    }

    private void obtainProjects() {
        try {
            List<ProjectDTO> projectsList = projectDAO.obtainAllProjects();
            projectsObservableList = FXCollections.observableArrayList(projectsList);
            tblProjects.setItems(projectsObservableList);
        } catch (DAOException e) {
            showError("Error al obtener lista de proyectos.");
        }
    }

    private void setUpColumns() {
        clmnName.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        clmnDescription.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        clmnPlacesAvailable.setCellValueFactory(
                new PropertyValueFactory<>("placesAvailable"));
        clmnAvailability.setCellValueFactory(
                new PropertyValueFactory<>("availability"));
        clmnLinkedOrganization.setCellValueFactory(
                cellData -> {
                    LinkedOrganizationDTO linkedOrganizationDTO = cellData.getValue().getLinkedOrganizationDTO();;
                    String name = (linkedOrganizationDTO != null) ? linkedOrganizationDTO.getName() : "Sin organización vinculada";
                    return new SimpleStringProperty(name);
                });
        clmnProjectManager.setCellValueFactory(
                cellData -> {
                    ProjectManagerDTO projectManagerDTO = cellData.getValue().getProjectManagerDTO();
                    String name = (projectManagerDTO != null) ? projectManagerDTO.getFirstName() : "Sin encargado";
                    return new SimpleStringProperty(name);
                }
        );
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Cancelar", event);
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
