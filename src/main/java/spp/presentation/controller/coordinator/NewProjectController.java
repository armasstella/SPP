package spp.presentation.controller.coordinator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.LinkedOrganizationDAO;
import spp.businesslogic.dao.ProjectManagerDAO;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class NewProjectController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private TextField txtPlacesAvailable;
    @FXML private ComboBox<ProjectManagerDTO> cmbProjectManager;
    @FXML private ComboBox<LinkedOrganizationDTO> cmbLinkedOrganization;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
        loadLinkedOrganizationInComboBox();
        loadProjectManagersInComboBox();

    }

    private void loadProjectManagersInComboBox() {
        try {
            ProjectManagerDAO projectManagerDAO = new ProjectManagerDAO();
            List<ProjectManagerDTO> projectManagerList =
                    projectManagerDAO.getActiveProjectManagers();
            ObservableList<ProjectManagerDTO> projectManagerObservableList =
                    FXCollections.observableArrayList(projectManagerList);
            cmbProjectManager.setItems(projectManagerObservableList);
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al cargar encargados de proyecto");
        }

    }

    private void loadLinkedOrganizationInComboBox() {
        try {
            LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();
            List<LinkedOrganizationDTO> linkedOrganizationList =
                    linkedOrganizationDAO.findActiveLinkedOrganizationsIdentifiers();
            ObservableList<LinkedOrganizationDTO> linkedOrganizationObservableList =
                    FXCollections.observableArrayList(linkedOrganizationList);
            cmbLinkedOrganization.setItems(linkedOrganizationObservableList);
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al cargar organizaciones vinculadas");
        }

    }

    private void setUpFields() {
        InputFilter.applyFilter(txtName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtDescription, InputFilter.ALPHANUMERIC_PATTERN, 40);
        InputFilter.applyFilter(txtPlacesAvailable, InputFilter.NUMERIC_PATTERN, 2);

    }

    private void setAllProject(ProjectDTO projectDTO) {
        ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        projectDTO.setName(txtName.getText().trim());
        projectDTO.setDescription(txtDescription.getText().trim());
        projectDTO.setPlacesAvailable(Integer.parseInt(txtPlacesAvailable.getText().trim()));
        projectManagerDTO.setId(cmbProjectManager.getValue().getId());
        projectDTO.setProjectManagerDTO(projectManagerDTO);
        linkedOrganizationDTO.setId(cmbLinkedOrganization.getValue().getId());
        projectDTO.setLinkedOrganizationDTO(linkedOrganizationDTO);

    }

    @FXML
    private void saveProject(ActionEvent event) {
        if (validateRegistrationInputs()) {
            return;
        }
        ProjectDAO projectDAO = new ProjectDAO();
        ProjectDTO projectDTO = new ProjectDTO();
        setAllProject(projectDTO);

        try {
            if (projectDAO.registerProject(projectDTO)) {
                StatusLabel.showSuccess(lblStatus, "Proyecto registrado correctamente.");
                clearInputFields();
            }

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Cancelar", event);

    }

    private boolean validateRegistrationInputs() {
        boolean emptyFields = false;
        if (txtName.getText().isBlank() ||
                txtDescription.getText().isBlank() ||
                txtPlacesAvailable.getText().isBlank() ||
                cmbProjectManager.getValue() == null ||
                cmbLinkedOrganization.getValue() == null) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            emptyFields = true;
        }

        return emptyFields;

    }

    private void clearInputFields() {
        txtName.clear();
        txtDescription.clear();
        txtPlacesAvailable.clear();
        cmbProjectManager.setValue(null);
        cmbLinkedOrganization.setValue(null);

    }

}
