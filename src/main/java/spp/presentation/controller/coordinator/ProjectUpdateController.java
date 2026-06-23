package spp.presentation.controller.coordinator;


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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class ProjectUpdateController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private Label lblMessageBeforeEdition;
    @FXML private Label lblMessageInEdition;
    @FXML private VBox vbShowAllProjects;
    @FXML private VBox vbEditProject;
    @FXML private HBox hbContinueButtons;
    @FXML private HBox hbEditionButtons;
    @FXML private TextField txtName;
    @FXML private TextArea txtDescription;
    @FXML private TextField txtPlacesAvailable;
    @FXML private TableView<ProjectDTO> tblProjects;
    @FXML private TableColumn<ProjectDTO, String> colName;
    @FXML private TableColumn<ProjectDTO, String> colDescription;
    @FXML private TableColumn<ProjectDTO, String> colAvailability;
    @FXML private TableColumn<ProjectDTO, String> colPlacesAvailable;
    @FXML private TableColumn<ProjectDTO, String> colLinkedOrganization;
    @FXML private TableColumn<ProjectDTO, String> colProjectManager;
    private final ProjectDAO projectDAO = new ProjectDAO();
    private ProjectDTO projectInEdition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainProjects();
        setUpFields();

    }

    private void setUpFields() {
        InputFilter.applyFilter(txtName, InputFilter.NAME_PATTERN, 100);
        InputFilter.applyFilter(txtDescription, InputFilter.ALPHANUMERIC_PATTERN, 500);
        InputFilter.applyFilter(txtPlacesAvailable, InputFilter.NUMERIC_PATTERN, 2);

    }

    private void obtainProjects() {
        try {
            List<ProjectDTO> projectsList = projectDAO.findProjectsDetailsForActiveTerm();
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
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml", "Cancelar", event);

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
