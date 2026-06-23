package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.Window;
import spp.businesslogic.dao.FinalReportDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InitialDocumentDAO;
import spp.businesslogic.dao.InternDAO;
import spp.utils.file.FileUtils;
import spp.utils.logger.AppLogger;
import spp.utils.view.FileChooserUtil;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;


public class UploadDocumentsController {

    @FXML private Label lblStatus;
    @FXML private Label lblSelectedDocument;
    private File selectedDocument;
    private String currentFolder;
    private String currentPrefix;
    private final InitialDocumentDTO initialDocumentDTO = new InitialDocumentDTO();
    private final InitialDocumentDAO initialDocumentDAO = new InitialDocumentDAO();
    private final InternDAO internDAO = new InternDAO();

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);

    }

    @FXML
    private void uploadClassSchedule(ActionEvent event) {
        if (existsClassSchedule()) {
            StatusLabel.showError(lblStatus, "Ya ha subido el horario.\nComuníquese con el coordinador.");
            return;
        }

        if (selectFile(event, "Seleccionar horario")) {
            initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.CLASS_SCHEDULE));
            currentFolder = "./documents/schedules/";
            currentPrefix = "schedule";
        }

    }

    @FXML
    private void uploadActivitiesSchedule(ActionEvent event) {
        if (existsActivitiesSchedule()) {
            StatusLabel.showError(lblStatus, "Ya ha subido la calendarización de actividades.\nComuníquese con el coordinador.");
            return;
        }

        if (selectFile(event, "Seleccionar calendarización de actividades")) {
            initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.ACTIVITIES_SCHEDULE));
            currentFolder = "./documents/activities/";
            currentPrefix = "activity";
        }

    }

    @FXML
    private void uploadPSP(ActionEvent event) {
        if (existsPSP()) {
            StatusLabel.showError(lblStatus, "Ya ha subido la bitácora PSP.\nComuníquese con el coordinador.");
            return;
        }

        if (selectFile(event, "Seleccionar bitácora PSP")) {
            initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.PSP));
            currentFolder = "./documents/psp/";
            currentPrefix = "psp";
        }

    }

    @FXML
    private void uploadPartialReport(ActionEvent event) {
        if (existsPartialReport()) {
            StatusLabel.showError(lblStatus, "Ya ha subido el reporte parcial.\nComuníquese con el coordinador.");
            return;
        }

        if (selectFile(event, "Seleccionar reporte parcial")) {
            initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.INDICATOR_REPORT));
            currentFolder = "./documents/reports/partial/";
            currentPrefix = "partial";
        }

    }

    @FXML
    private void uploadSelfEvaluation(ActionEvent event) {
        if (existsSelfEvaluation()) {
            StatusLabel.showError(lblStatus, "Ya ha subido la autoevaluación.\nComuníquese con el coordinador.");
            return;
        }

        if (selectFile(event, "Seleccionar autoevaluación")) {
            initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.SELF_EVALUATION));
            currentFolder = "./documents/evaluations/self/";
            currentPrefix = "self_evaluation";
        }

    }

    @FXML
    private void uploadEvaluationLinkedOrganization(ActionEvent event) {
        if (existsEvaluationLinkedOrganization()) {
            StatusLabel.showError(lblStatus, "Ya ha subido la evaluación de la organización.\nComuníquese con el coordinador.");
            return;
        }

        if (selectFile(event, "Seleccionar evaluación de organización vinculada")) {
            initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.EVALUATION_LINKED_ORGANIZATION));
            currentFolder = "./documents/evaluations/organization/";
            currentPrefix = "organization_evaluation";
        }

    }

    @FXML
    private void uploadFinalReport(ActionEvent event) {
        if (existsFinalReport()) {
            StatusLabel.showError(lblStatus, "Ya ha subido el reporte final.\nComuníquese con el coordinador.");
            return;
        }

        if (selectFile(event, "Seleccionar reporte final")) {
            initialDocumentDTO.setDocumentType(String.valueOf(DocumentType.FINAL_REPORT));
            currentFolder = "./documents/reports/final/";
            currentPrefix = "final";
        }

    }

    private boolean existsClassSchedule() {
        boolean exists = false;
        try {
            exists = initialDocumentDAO.hasClassScheduleByInternEmail(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al buscar el horario.");
        }
        return exists;

    }

    private boolean existsActivitiesSchedule() {
        boolean exists = false;
        try {
            exists = initialDocumentDAO.hasActivitiesScheduleByInternEmail(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al buscar la calendarización.");
        }
        return exists;

    }

    private boolean existsPSP() {
        boolean exists = false;
        try {
            exists = initialDocumentDAO.hasPSPByInternEmail(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al buscar la bitácora PSP.");
        }
        return exists;

    }

    private boolean existsPartialReport() {
        boolean exists = false;
        try {
            exists = initialDocumentDAO.hasPartialReportByInternEmail(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al buscar el reporte parcial.");
        }
        return exists;

    }

    private boolean existsSelfEvaluation() {
        boolean exists = false;
        try {
            exists = initialDocumentDAO.hasSelfEvaluationByInternEmail(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al buscar la autoevaluación.");
        }
        return exists;

    }

    private boolean existsEvaluationLinkedOrganization() {
        boolean exists = false;
        try {
            exists = initialDocumentDAO.hasEvaluationLinkedOrganizationByInternEmail(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al buscar la evaluación de la organización.");
        }
        return exists;

    }

    private boolean existsFinalReport() {
        boolean exist = false;
        FinalReportDAO finalReportDAO = new FinalReportDAO();
        try {
            exist = finalReportDAO.hasFinalReportByInternEmail(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al buscar el reporte final.");
        }
        return exist;

    }

    private boolean selectFile(ActionEvent event, String dialogTitle) {
        Window window = ((Node) event.getSource()).getScene().getWindow();
        File file = FileChooserUtil.selectSingleFile(window, dialogTitle);

        if (file != null) {
            selectedDocument = file;
            lblSelectedDocument.setText("Archivo seleccionado: " + selectedDocument.getName());
            StatusLabel.showSuccess(lblStatus, "Archivo listo para subir.");
            return true;
        }
        return false;
    }

    @FXML
    private void confirm() {
        if (validateEmptyInputs()) {
            return;
        }

        if (setFileMetadata()) {
            if (!saveDataDocument()) {
                return;
            }
        } else {
            return;
        }

        StatusLabel.showSuccess(lblStatus, "Archivo cargado exitosamente");
        lblSelectedDocument.setText("Ningún archivo seleccionado");
        selectedDocument = null;
        currentFolder = null;
        currentPrefix = null;
    }

    private boolean validateEmptyInputs() {
        if (selectedDocument == null) {
            StatusLabel.showError(lblStatus, "No se ha elegido un archivo.");
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

    private boolean setFileMetadata() {
        boolean saveStatus = false;
        String extension = FileUtils.getExtension(selectedDocument.getName());

        try {
            String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
            String uniqueName = FileUtils.generateUniqueName(studentNumber, extension, currentPrefix);
            String finalPath = FileUtils.copyFile(selectedDocument, currentFolder, uniqueName);

            initialDocumentDTO.setOriginalName(selectedDocument.getName());
            initialDocumentDTO.setSavedName(uniqueName);
            initialDocumentDTO.setFilePath(finalPath);
            initialDocumentDTO.setSizeMb(selectedDocument.length() / FileUtils.BYTES_PER_MB);
            initialDocumentDTO.setExtension(extension);
            initialDocumentDTO.setUploadDate(LocalDateTime.now());
            saveStatus = true;

        } catch (IOException | DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "No se puede guardar el archivo. Intente de nuevo.");
        }

        return saveStatus;
    }

    private boolean saveDataDocument() {
        boolean success = false;
        try {
            String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
            success = initialDocumentDAO.saveDocument(studentNumber, initialDocumentDTO);
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al guardar documento");
        }

        if (!success) {
            StatusLabel.showError(lblStatus, "Error al guardar documento. Intenta de nuevo.");
        }

        return success;
    }
}
