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
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectManagerDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class NewProjectManagerController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtResponsability;
    @FXML private TextField txtRole;
    @FXML private TextField txtPhoneNumber;
    @FXML private ComboBox<LinkedOrganizationDTO> cmbLinkedOrganizations;
    private final ProjectManagerDAO projectManagerDAO  = new ProjectManagerDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLinkedOrganizationsInComboBox();
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFilter(txtFirstName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtSecondName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtFirstLastName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtSecondLastName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtResponsability, InputFilter.ALPHANUMERIC_PATTERN, 100);
        InputFilter.applyFilter(txtRole, InputFilter.ALPHANUMERIC_PATTERN, 100);
        InputFilter.applyFilter(txtPhoneNumber, InputFilter.NUMERIC_PATTERN, 10);

    }

    private void loadLinkedOrganizationsInComboBox() {
        try {
            LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();
            List<LinkedOrganizationDTO> linkedOrganizationsList = linkedOrganizationDAO.findActiveLinkedOrganizationsIdentifiers();
            ObservableList<LinkedOrganizationDTO> linkedOrganizationsObservableList =
                    FXCollections.observableArrayList(linkedOrganizationsList);
            cmbLinkedOrganizations.setItems(linkedOrganizationsObservableList);

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al cargar organizaciones vinculadas");
        }

    }

    @FXML
    private ProjectManagerDTO buildProjectManagerDTO() {
        ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
        projectManagerDTO.setFirstName(txtFirstName.getText().trim());
        projectManagerDTO.setSecondName(txtSecondName.getText().trim());
        projectManagerDTO.setFirstLastName(txtFirstLastName.getText().trim());
        projectManagerDTO.setSecondLastName(txtSecondLastName.getText().trim());
        projectManagerDTO.setRole(txtRole.getText().trim());
        projectManagerDTO.setResponsibility(txtResponsability.getText().trim());
        projectManagerDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        return projectManagerDTO;

    }

    @FXML
    private void saveProjectManager(ActionEvent event) {
        if (validateRegistrationInputs()) {
            return;
        }

        try {
            int linkedOrganizationId = cmbLinkedOrganizations.getValue().getId();
            if (projectManagerDAO.registerProjectManager(buildProjectManagerDTO(),linkedOrganizationId)) {
                StatusLabel.showSuccess(lblStatus, "Encargado de proyecto registrado correctamente.");
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
                "Menú Coordinador", event);

    }

    private boolean validateRegistrationInputs() {
        boolean validFields = false;

        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtResponsability.getText().isBlank() ||
                txtRole.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank()){
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            validFields = true;
        }

        return validFields;

    }

    private void clearInputFields() {
        txtFirstName.clear();
        txtSecondName.clear();
        txtFirstLastName.clear();
        txtSecondLastName.clear();
        txtResponsability.clear();
        txtRole.clear();
        txtPhoneNumber.clear();

    }

}
