package spp.presentation.controller.coordinator;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import spp.businesslogic.dao.ActivityScheduleDAO;
import spp.businesslogic.dao.LinkedOrganizationDAO;
import spp.businesslogic.dao.ProjectManagerDAO;
import spp.businesslogic.dto.ActivityScheduleDTO;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectDAO;
import spp.utils.file.FileUtils;
import spp.utils.logger.AppLogger;
import spp.utils.view.FileChooserUtil;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;


public class NewProjectController implements Initializable, ChangeListener<LinkedOrganizationDTO> {

    @FXML private Label lblStatus;
    @FXML private TextField txtName;
    @FXML private TextArea taDescription;
    @FXML private TextField txtPlacesAvailable;
    @FXML private TextField txtActivitiesScheduleFile;
    @FXML private ComboBox<ProjectManagerDTO> cmbProjectManager;
    @FXML private ComboBox<LinkedOrganizationDTO> cmbLinkedOrganization;
    private File selectedDocument;
    private boolean isActivitiesScheduleFileSelected = false;
    String currentFolder;
    String currentPrefix;

    private final ProjectDAO projectDAO = new ProjectDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
        loadLinkedOrganizationInComboBox();
        cmbLinkedOrganization.getSelectionModel().selectedItemProperty().addListener(this);

    }

    @Override
    public void changed(ObservableValue<? extends LinkedOrganizationDTO> observable,
                        LinkedOrganizationDTO oldValue,
                        LinkedOrganizationDTO newValue) {
        if (newValue != null) {
            updateProjectManagers(newValue.getId());
        }
    }

    @FXML
    private void chooseActivitiesScheduleFile(ActionEvent event) {
        if (selectFile(event, "Seleccionar horario")) {
            currentFolder = "./documents/projects/activitiesSchedule/";
            currentPrefix = "activitiesSchedule";
        }
    }

    private boolean selectFile(ActionEvent event, String dialogTitle) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        File file = FileChooserUtil.selectSingleFile(window, dialogTitle);

        if (file != null) {
            selectedDocument = file;
            txtActivitiesScheduleFile.setText(selectedDocument.getName());
            StatusLabel.showSuccess(lblStatus, "Archivo listo para subir.");
            isActivitiesScheduleFileSelected = true;
        }
        return isActivitiesScheduleFileSelected;
    }


    private void updateProjectManagers(int organizationId) {
        try {
            ProjectManagerDAO projectManagerDAO = new ProjectManagerDAO();
            List<ProjectManagerDTO> managers = projectManagerDAO.getProjectManagersByOrganization(organizationId);

            ObservableList<ProjectManagerDTO> observableList = FXCollections.observableArrayList(managers);
            cmbProjectManager.setItems(observableList);
            cmbProjectManager.setDisable(managers.isEmpty());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al cargar encargados.");
        }
    }

    private void loadLinkedOrganizationInComboBox() {
        try {
            LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();
            List<LinkedOrganizationDTO> linkedOrganizationList = linkedOrganizationDAO.findActiveLinkedOrganizationsIdentifiers();
            ObservableList<LinkedOrganizationDTO> linkedOrganizationObservableList =
                    FXCollections.observableArrayList(linkedOrganizationList);
            cmbLinkedOrganization.setItems(linkedOrganizationObservableList);
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al cargar organizaciones vinculadas");
        }

    }

    private void setUpFields() {
        final String TEXT_PATTERN = "[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ ]*";

        InputFilter.applyFilter(txtName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(taDescription, InputFilter.ALPHANUMERIC_PATTERN, 40);
        InputFilter.applyFilter(txtPlacesAvailable, InputFilter.NUMERIC_PATTERN, 2);

    }

    private ProjectDTO buildProjectDTO() {
        ProjectDTO projectDTO = new ProjectDTO();
        ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        projectDTO.setName(txtName.getText().trim());
        projectDTO.setDescription(taDescription.getText().trim());
        projectDTO.setPlacesAvailable(Integer.parseInt(txtPlacesAvailable.getText().trim()));
        projectManagerDTO.setId(cmbProjectManager.getValue().getId());
        projectDTO.setProjectManagerDTO(projectManagerDTO);
        linkedOrganizationDTO.setId(cmbLinkedOrganization.getValue().getId());
        projectDTO.setLinkedOrganizationDTO(linkedOrganizationDTO);

        return projectDTO;

    }

    @FXML
    private void saveProject(ActionEvent event) {
        if (validateRegistrationInputs()) {
            return;
        }

        try {
            int projectIdInserted = projectDAO.registerProject(buildProjectDTO());
            if (projectIdInserted > 0) {
                ActivityScheduleDAO  activityScheduleDAO = new ActivityScheduleDAO();
                ActivityScheduleDTO activityScheduleDTO = new ActivityScheduleDTO();
                if (setMetaDataFile(activityScheduleDTO, projectIdInserted)) {
                    if (activityScheduleDAO.saveActivitySchedule(activityScheduleDTO, projectIdInserted)) {
                        StatusLabel.showSuccess(lblStatus, "Proyecto registrado correctamente.");
                        clearInputFields();
                    }
                }
            }

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    private boolean setMetaDataFile(ActivityScheduleDTO activityScheduleDTO, int projectIdInserted) {
        boolean saveStatus = false;
        String extension = FileUtils.getExtension(selectedDocument.getName());

        try {
            String uniqueName = FileUtils.generateUniqueName(String.valueOf(projectIdInserted), extension, currentPrefix);
            String finalPath = FileUtils.copyFile(selectedDocument, currentFolder, uniqueName);

            activityScheduleDTO.setOriginalName(selectedDocument.getName());
            activityScheduleDTO.setSavedName(uniqueName);
            activityScheduleDTO.setFilePath(finalPath);
            activityScheduleDTO.setSizeMb(selectedDocument.length() / FileUtils.BYTES_PER_MB);
            activityScheduleDTO.setExtension(extension);
            activityScheduleDTO.setUploadDate(LocalDateTime.now());
            saveStatus = true;

        } catch (IOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "No se puede guardar el archivo. Intente de nuevo.");
        }

        return saveStatus;
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml", "Cancelar", event);

    }

    private boolean validateRegistrationInputs() {
        boolean emptyFields = false;
        if (txtName.getText().isBlank() ||
                taDescription.getText().isBlank() ||
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
        taDescription.clear();
        txtPlacesAvailable.clear();
        cmbProjectManager.setValue(null);
        cmbLinkedOrganization.setValue(null);
        txtActivitiesScheduleFile.clear();
        selectedDocument = null;

    }

}