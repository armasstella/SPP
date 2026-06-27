package spp.presentation.controller.coordinator;

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
import spp.utils.view.FileChooserUtil;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewConstant;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
        loadLinkedOrganizationInComboBox();
        cmbLinkedOrganization.getSelectionModel().selectedItemProperty().addListener(this);
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
            StatusLabel.showError(lblStatus, "Error al cargar encargados.");
        }
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
                int projectIdInserted = projectDAO.registerProject(projectDTO);
                if (projectIdInserted > ViewConstant.ID_ZERO_INVALID) {
                    ActivityScheduleDAO activityScheduleDAO = new ActivityScheduleDAO();
                    ActivityScheduleDTO activityScheduleDTO = new ActivityScheduleDTO();
                    if (setMetaDataFile(activityScheduleDTO, projectIdInserted)) {
                        if (activityScheduleDAO.saveActivitySchedule(activityScheduleDTO, projectIdInserted)) {
                            StatusLabel.showSuccess(lblStatus, "Proyecto registrado correctamente.");
                            clearInputFields();
                        }
                    }
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "No se pudo registrar el proyecto");
            }
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
        taDescription.clear();
        txtPlacesAvailable.clear();
        cmbProjectManager.setValue(null);
        cmbLinkedOrganization.setValue(null);
        txtActivitiesScheduleFile.clear();
        selectedDocument = null;

    }
}
