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
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewConstant;
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

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtName,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_TITLE);
        InputFilter.applyFormatFilter(txtDescription,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
        InputFilter.applyFormatFilter(txtPlacesAvailable,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY);
    }

    private void setAllProject(ProjectDTO projectDTO) {
        projectDTO.setName(txtName.getText().trim());
        projectDTO.setDescription(txtDescription.getText().trim());
        projectDTO.setPlacesAvailable(Integer.parseInt(txtPlacesAvailable.getText().trim()));
        projectDTO.setProjectManagerDTO(cmbProjectManager.getValue());
        projectDTO.setLinkedOrganizationDTO(cmbLinkedOrganization.getValue());
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtName.getText().isBlank() ||
                txtDescription.getText().isBlank() ||
                txtPlacesAvailable.getText().isBlank() ||
                cmbProjectManager.getValue() == null ||
                cmbLinkedOrganization.getValue() == null) {

            emptyFields = true;
        }

        return emptyFields;
    }

    private boolean hasValidMinimumLengths() {
        boolean validLengths = false;

        boolean validName = InputFilter.hasMinimumLength(txtName, ViewConstant.
                MIN_LENGTH_NAME);

        if (validName) {
            validLengths = true;
        }

        return validLengths;
    }

    private boolean areValidFields() {
        boolean validFields = false;

        if (hasEmptyFields()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
        } else {
            if (hasValidMinimumLengths()) {
                validFields = true;
            } else {
                StatusLabel.showError(lblStatus, "La longitud de los campos debe cumplir con el mínimo de caracteres.");
            }
        }

        return validFields;
    }

    @FXML
    private void saveProject(ActionEvent event) {
        if (areValidFields()) {
            ProjectDTO projectDTO = new ProjectDTO();
            setAllProject(projectDTO);

            ProjectDAO projectDAO = new ProjectDAO();
            try {
                if (projectDAO.registerProject(projectDTO)) {
                    StatusLabel.showSuccess(lblStatus, "Proyecto registrado correctamente.");
                    clearInputFields();
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "No se pudo registrar el proyecto");
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
    }

    private void loadProjectManagersInComboBox() {
        try {
            ProjectManagerDAO projectManagerDAO = new ProjectManagerDAO();
            List<ProjectManagerDTO> projectManagerList = projectManagerDAO.getActiveProjectManagers();
            ObservableList<ProjectManagerDTO> projectManagerObservableList = FXCollections.observableArrayList(projectManagerList);

            cmbProjectManager.setItems(projectManagerObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al cargar la lista de encargados de proyecto.");
        }
    }

    private void loadLinkedOrganizationInComboBox() {
        try {
            LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();
            List<LinkedOrganizationDTO> linkedOrganizationList = linkedOrganizationDAO.findActiveLinkedOrganizationsIdentifiers();
            ObservableList<LinkedOrganizationDTO> linkedOrganizationObservableList = FXCollections.observableArrayList(linkedOrganizationList);

            cmbLinkedOrganization.setItems(linkedOrganizationObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al cargar la lista de organizaciones vinculadas.");
        }
    }

    private void clearInputFields() {
        txtName.clear();
        txtDescription.clear();
        txtPlacesAvailable.clear();
        cmbProjectManager.setValue(null);
        cmbLinkedOrganization.setValue(null);
    }
}
