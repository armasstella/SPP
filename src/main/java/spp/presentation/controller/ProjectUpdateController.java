package spp.presentation.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProjectUpdateController implements Initializable {

    @FXML private Label lblMessageBeforeEdition;
    @FXML private Label lblMessageInEdition;
    @FXML private VBox vbShowAllProjects;
    @FXML private VBox vbEditProject;
    @FXML private HBox hbContinueButtons;
    @FXML private HBox hbEditionButtons;
    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtPlacesAvailable;
    @FXML private TextField txtLinkedOrganizationId;
    @FXML private TextField txtProjectManagerId;
    @FXML private TableView<ProjectDTO> tblProjects;
    @FXML private TableColumn<ProjectDTO, String> colName;
    @FXML private TableColumn<ProjectDTO, String> colDescription;
    @FXML private TableColumn<ProjectDTO, String> colAvailability;
    @FXML private TableColumn<ProjectDTO, String> colPlacesAvailable;
    @FXML private TableColumn<ProjectDTO, String> colLinkedOrganization;
    @FXML private TableColumn<ProjectDTO, String> colProjectManager;
    @FXML private Label lblStatus;

    private final ProjectDAO projectDAO = new ProjectDAO();
    private ObservableList<ProjectDTO> projectsObservableList;
    private ProjectDTO projectInEdition;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainProjects();
        setUpFields();
    }

    private void setUpFields() {
        String textPattern = "[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ¿?¡!'\"()/$#=%+\\-\\[\\]{}.,_ ]*";

        InputFilter.applyFilter(txtName, textPattern, 100);
        InputFilter.applyFilter(txtDescription, textPattern, 500);
        InputFilter.applyFilter(txtPlacesAvailable, "\\d*", 2);
    }

    private void obtainProjects() {
        try {
            List<ProjectDTO> projectsList = projectDAO.obtainAllProjects();
            projectsObservableList = FXCollections.observableArrayList(projectsList);
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
                    LinkedOrganizationDTO linkedOrganizationDTO = cellData.getValue().getLinkedOrganizationDTO();
                    String name = (linkedOrganizationDTO != null) ? linkedOrganizationDTO.getName() : "Sin organización vinculada";
                    return new SimpleStringProperty(name);
                });
        colProjectManager.setCellValueFactory(
                cellData -> {
                    ProjectManagerDTO projectManagerDTO = cellData.getValue().getProjectManagerDTO();
                    String name = (projectManagerDTO != null) ? projectManagerDTO.getFirstName() : "Sin encargado";
                    return new SimpleStringProperty(name);
                }
        );
    }

    @FXML
    private void continueToEdit(ActionEvent event) {
        projectInEdition = tblProjects.getSelectionModel().getSelectedItem();
        if (projectInEdition == null) {
            StatusLabel.showError(lblStatus, "Debe seleccionar un proyecto");
            return;
        }
        lblStatus.setText("");
        lblMessageBeforeEdition.setVisible(false);
        vbShowAllProjects.setVisible(false);
        hbContinueButtons.setVisible(false);
        lblMessageInEdition.setVisible(true);
        vbEditProject.setVisible(true);
        hbEditionButtons.setVisible(true);
        txtName.setText(projectInEdition.getName());
        txtDescription.setText(projectInEdition.getDescription());
        txtPlacesAvailable.setText(String.valueOf(projectInEdition.getPlacesAvailable()));
    }

    @FXML
    private void cancelEdition(ActionEvent event) {
        if (!txtName.getText().isBlank() ||
                !txtDescription.getText().isBlank() ||
                !txtPlacesAvailable.getText().isBlank()) {
            if (AlertHelper.showConfirmation("¿Seguro que desea cancelar?",
                    "La información registrada se perderá")) {

                clearInputFields();
                lblStatus.setText("");
                lblMessageInEdition.setVisible(false);
                vbEditProject.setVisible(false);
                hbEditionButtons.setVisible(false);
                lblMessageBeforeEdition.setVisible(true);
                vbShowAllProjects.setVisible(true);
                hbContinueButtons.setVisible(true);
            }
        } else {
            clearInputFields();
            lblStatus.setText("");
            lblMessageInEdition.setVisible(false);
            vbEditProject.setVisible(false);
            hbEditionButtons.setVisible(false);
            lblMessageBeforeEdition.setVisible(true);
            vbShowAllProjects.setVisible(true);
            hbContinueButtons.setVisible(true);
        }
    }

    private boolean areRequiredInputFieldsEmpty() {
        return txtName.getText().isBlank() ||
                txtDescription.getText().isBlank() ||
                txtPlacesAvailable.getText().isBlank();

    }

    @FXML
    private void updateProject(ActionEvent event) {

        if (!validateUpdateInputs()) {
            return;
        }

        try {
            String newName = txtName.getText().trim();
            String newDescription = txtDescription.getText().trim();
            int newPlacesAvailable = Integer.parseInt(txtPlacesAvailable.getText().trim());

            ProjectDTO projectDTO = new ProjectDTO();
            projectDTO.setName(newName);
            projectDTO.setDescription(newDescription);
            projectDTO.setPlacesAvailable(newPlacesAvailable);
            projectDTO.setId(projectInEdition.getId());

            if (projectDAO.updateProject(projectDTO)) {
                showAllProjects();
                StatusLabel.showSuccess(lblStatus, "Proyecto actualizado correctamente.");
                clearInputFields();
            }
        } catch (NumberFormatException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "El ID debe ser un número válido.");
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void showAllProjects() {
        StatusLabel.showSuccess(lblStatus, "Proyecto actualizado correctamente.");
        lblMessageInEdition.setVisible(false);
        vbEditProject.setVisible(false);
        hbEditionButtons.setVisible(false);
        lblMessageBeforeEdition.setVisible(true);
        vbShowAllProjects.setVisible(true);
        hbContinueButtons.setVisible(true);
        obtainProjects();
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml", "Cancelar", event);
    }

    private boolean validateUpdateInputs() {
        boolean areInputsValid = true;
        if (areRequiredInputFieldsEmpty()) {
            StatusLabel.showError(lblStatus, "Los campos no deben estar vacíos.");
            areInputsValid = false;
        }
        return areInputsValid;
    }

    private void clearInputFields() {
        txtName.setText("");
        txtDescription.setText("");
        txtPlacesAvailable.setText("");
    }


}
