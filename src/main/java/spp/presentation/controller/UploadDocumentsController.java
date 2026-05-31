package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InitialDocumentDAO;
import spp.businesslogic.dao.InternDAO;
import spp.utils.file.FileUtils;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class UploadDocumentsController {

    @FXML private Label lblStatus;
    @FXML private TextField txtStudentNumber;
    @FXML private Button btnUploadClassSchedule;
    @FXML private Label lblSelectedDocument;

    private File selectedDocument;
    private InitialDocumentDTO initialDocumentDTO = new InitialDocumentDTO();
    private final InitialDocumentDAO initialDocumentDAO = new InitialDocumentDAO();
    private InternDAO internDAO = new InternDAO();

    @FXML
    private void cancel(ActionEvent event){
        ViewNavigator.loadView("/spp/presentation/view/InternMenuView.fxml",
                "Menú Practicante", event);
    }

    @FXML
    private void uploadClassSchedule(ActionEvent event) {
        if (validateEmptyInputs() && searchStudent()) {
            if (searchClassSchedule()) {
                StatusLabel.showError(lblStatus, "Ya ha subido el horario.\nComuníquese con el coordinador.");
            } else {
                initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.CLASS_SCHEDULE));
                selectFile(event);
            }
        }
    }

    private boolean searchClassSchedule() {
        boolean existsClassSchedule = false;
        try {
            existsClassSchedule = initialDocumentDAO.searchClassScheduleForIntern(txtStudentNumber.getText().trim());
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al buscar horario.");
        }
        return existsClassSchedule;
    }

    @FXML
    private void uploadActivitiesSchedule(ActionEvent event) {
        if (validateEmptyInputs() && searchStudent()) {
            if (searchActivitiesSchedule()) {
                StatusLabel.showError(lblStatus, "Ya ha subido la calendarización de actividades." +
                        "\nComuníquese con el coordinador.");
            } else {
                initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.ACTIVITIES_SCHEDULE));
                selectFile(event);
            }

        }
    }

    private boolean searchActivitiesSchedule() {
        boolean existsActivitiesSchedule = false;
        try {
            existsActivitiesSchedule = initialDocumentDAO.searchActivitiesScheduleForIntern(txtStudentNumber.getText().trim());
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al buscar calendarización de actividades.");
        }
        return existsActivitiesSchedule;
    }


    @FXML
    private void confirm() {
        if (validateEmptyInputs()) {
            return;
        }

        if (searchStudent()) {
            if (saveFile()) {
                if (!saveDataDocument()) {
                    return;
                }
            } else {
                return;
            }

            StatusLabel.showSuccess(lblStatus, "Archivo cargado exitosamente");
            lblSelectedDocument.setText("No hay archivo seleccionado");
            txtStudentNumber.clear();
            selectedDocument = null;
        }
    }

    private boolean searchStudent() {
        boolean existsStudentNumber = false;
        try {
            existsStudentNumber = internDAO.searchStudentNumberRegister(txtStudentNumber.getText().trim());
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "La matricula ingresada no es correcta");
        }
        return existsStudentNumber;
    }

    private boolean validateEmptyInputs() {
        if (selectedDocument == null) {
            StatusLabel.showError(lblStatus, "No se ha elegido un archivo.");
            return true;
        }

        if (txtStudentNumber.getText().isBlank()) {
            StatusLabel.showError(lblStatus, "Completa el campo de matrícula.");
            return true;
        }

        String extension = FileUtils.getExtension(selectedDocument.getName());

        if (!FileUtils.ALLOWED_EXTENSIONS.contains(extension)) {
            StatusLabel.showError(lblStatus, "Formato invalido. Solo se acepta PDF o DOCX.");
            return true;
        }
        if (selectedDocument.length() == 0) {
            StatusLabel.showError(lblStatus, "El documento está vacío y no puede guardarse.");
            return true;
        }
        if (selectedDocument.length() > FileUtils.MAX_BYTES) {
            StatusLabel.showError(lblStatus, "El tamaño de archivo excede el permitido.");
            return true;
        }

        return false;
    }

    private boolean saveFile() {
        boolean saveStatus = false;
        String extension  = FileUtils.getExtension(selectedDocument.getName());
        String uniqueName = FileUtils.generateUniqueName(txtStudentNumber.getText().trim(), extension);

        try {
            String finalPath = FileUtils.copyFile(selectedDocument, uniqueName);

            initialDocumentDTO.setOriginalName(selectedDocument.getName());
            initialDocumentDTO.setSavedName(uniqueName);
            initialDocumentDTO.setFilePath(finalPath);
            initialDocumentDTO.setSizeMb(selectedDocument.length() / FileUtils.BYTES_PER_MB);
            initialDocumentDTO.setExtension(extension);
            initialDocumentDTO.setUploadDate(LocalDateTime.now());
            saveStatus = true;

        } catch (IOException e) {
            AppLogger.logError(e);
            lblStatus.setText("No se puede guardar el archivo. Intente de nuevo.");
        }
        return saveStatus;
    }

    private boolean saveDataDocument() {
        boolean success = false;
        try {
            success = initialDocumentDAO.saveDocument(txtStudentNumber.getText().trim(), initialDocumentDTO);
        } catch (DAOException e) {
            lblStatus.setText("Error al guardar documento: 1.");
        }

        if (!success) {
            lblStatus.setText("Error al guardar documento. Intenta de nuevo.");
        }

        return success;
    }

    private void selectFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar horario");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documentos permitidos", "*.pdf", "*.docx")
        );

        Stage stage = (Stage) btnUploadClassSchedule.getScene().getWindow();
        selectedDocument = fileChooser.showOpenDialog(stage);

        if (selectedDocument != null) {
            lblSelectedDocument.setText("Archivo seleccionado: " + selectedDocument.getName());
        }
    }
}
