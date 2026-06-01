package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import java.util.ResourceBundle;


public class NewProjectController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtName;
    @FXML private TextField txtDescription;
    @FXML private TextField txtPlacesAvailable;
    @FXML private TextField txtProjectManager;
    @FXML private TextField txtLinkedOrganization;

    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
    }

    private void setUpFields() {
        final String TEXT_PATTERN = "[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]*";

        InputFilter.applyFilter(txtName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtDescription, InputFilter.ALPHANUMERIC_PATTERN, 40);
        InputFilter.applyFilter(txtPlacesAvailable, InputFilter.NUMERIC_PATTERN, 2);
        InputFilter.applyFilter(txtProjectManager, InputFilter.NUMERIC_PATTERN, 2);
        InputFilter.applyFilter(txtLinkedOrganization, InputFilter.NUMERIC_PATTERN, 2);

    }

    private ProjectDTO buildProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO();
        ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        projectDTO.setName(txtName.getText().trim());
        projectDTO.setDescription(txtDescription.getText().trim());
        projectDTO.setPlacesAvailable(Integer.parseInt(txtPlacesAvailable.getText().trim()));
        projectManagerDTO.setId(Integer.parseInt(txtProjectManager.getText().trim()));
        projectDTO.setProjectManagerDTO(projectManagerDTO);
        linkedOrganizationDTO.setId(Integer.parseInt(txtLinkedOrganization.getText().trim()));
        projectDTO.setLinkedOrganizationDTO(linkedOrganizationDTO);

        return projectDTO;

    }

    @FXML
    private void saveProject(ActionEvent event) {
        if (validateRegistrationInputs()) {
            return;
        }

        try {
            if (projectDAO.addProject(buildProjectDTO())) {
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
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml", "Cancelar", event);

    }

    private boolean validateRegistrationInputs() {
        boolean emptyFields = false;
        if (txtName.getText().isBlank() ||
                txtDescription.getText().isBlank() ||
                txtPlacesAvailable.getText().isBlank() ||
                txtProjectManager.getText().isBlank() ||
                txtLinkedOrganization.getText().isBlank()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            emptyFields = true;
        }

        return emptyFields;

    }

    private void clearInputFields() {
        txtName.clear();
        txtDescription.clear();
        txtPlacesAvailable.clear();
        txtProjectManager.clear();
        txtLinkedOrganization.clear();

    }

}