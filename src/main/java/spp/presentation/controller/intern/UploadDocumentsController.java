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
import spp.businesslogic.enums.DocumentationPhase;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InitialDocumentDAO;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.exceptions.FileManagementException;
import spp.utils.file.DocumentUploadConfiguration;
import spp.utils.file.FileUtils;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import spp.utils.view.filechooser.FileChooserHelper;
import spp.utils.view.filechooser.AllowedExtension;
import java.io.File;
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
    private final FinalReportDAO finalReportDAO = new FinalReportDAO();

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);
    }

    @FXML
    private void uploadClassSchedule(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.CLASS_SCHEDULE,
                "./documents/intern_documents/schedules/",
                "schedule",
                DocumentationPhase.INITIAL
        );
        processDocumentSelection(event, documentConfiguration);
    }

    @FXML
    private void uploadPSP(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.PSP,
                "./documents/intern_documents/psp/",
                "psp",
                DocumentationPhase.CLOSURE
        );
        processDocumentSelection(event, documentConfiguration);
    }

    @FXML
    private void uploadMonthlyReport(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.MONTHLY_REPORT,
                "./documents/intern_documents/monthly_reports/",
                "monthly",
                DocumentationPhase.PRACTICE
        );
        processDocumentSelection(event, documentConfiguration);
    }

    @FXML
    private void uploadPartialReport(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.PSP,
                "./documents/intern_documents/partial_reports/",
                "partial",
                DocumentationPhase.PRACTICE
        );
        processDocumentSelection(event, documentConfiguration);
    }

    @FXML
    private void uploadActivitiesPlan(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.ACTIVITIES_PLAN,
                "./documents/intern_documents/activities_plan",
                "activities_plan",
                DocumentationPhase.PRACTICE
        );
        processDocumentSelection(event, documentConfiguration);

    }

    @FXML
    private void uploadSelfEvaluation(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.SELF_EVALUATION,
                "./documents/intern_documents/self_evaluations/",
                "self_evaluation",
                DocumentationPhase.CLOSURE
        );
        processDocumentSelection(event, documentConfiguration);
    }

    @FXML
    private void uploadEvaluationLinkedOrganization(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.EVALUATION_LINKED_ORGANIZATION,
                "./documents/intern_documents/evaluations_linked_organizations/",
                "evaluation_linked_organization",
                DocumentationPhase.CLOSURE
        );
        processDocumentSelection(event, documentConfiguration);
    }

    @FXML
    private void uploadFinalReport(ActionEvent event) {
        DocumentUploadConfiguration documentConfiguration = new DocumentUploadConfiguration(
                DocumentType.FINAL_REPORT,
                "./documents/intern_documents/reports/final/",
                "final",
                DocumentationPhase.CLOSURE
        );
        processDocumentSelection(event, documentConfiguration);
    }

    private void processDocumentSelection(ActionEvent event, DocumentUploadConfiguration documentConfiguration) {
        if (canUploadPhase(documentConfiguration.getPhase())) {
            if (documentExists(documentConfiguration.getType())) {
                StatusLabel.showError(lblStatus, "Ya ha subido este documento.\nComuníquese con el coordinador.");
            } else {
                Window currentWindow = ((Node) event.getSource()).getScene().getWindow();
                File file = FileChooserHelper.selectSingleFile(currentWindow, "SELECCIONAR DOCUMENTO",
                        AllowedExtension.PDF, AllowedExtension.DOCX);

                if (file != null) {
                    selectedDocument = file;
                    initialDocumentDTO.setDocumentType(String.valueOf(documentConfiguration.getType()));
                    currentFolder = documentConfiguration.getFolder();
                    currentPrefix = documentConfiguration.getPrefix();

                    lblSelectedDocument.setText("Archivo seleccionado: " + selectedDocument.getName());
                    StatusLabel.showSuccess(lblStatus, "Archivo listo para subir.");
                }
            }
        }
    }

    private boolean canUploadPhase(DocumentationPhase documentationPhasephase) {
        boolean isAllowed = false;

        if (documentationPhasephase == DocumentationPhase.INITIAL) {
            isAllowed = true;
        } else if (documentationPhasephase == DocumentationPhase.PRACTICE) {
            // Aquí llamas a tu DAO (idealmente una vista/procedimiento SQL) para saber si los iniciales están calificados
            isAllowed = areInitialDocumentsQualified();
        } else if (documentationPhasephase == DocumentationPhase.CLOSURE) {
            // Aquí verificas si los de prácticas están calificados
            isAllowed = arePracticeDocumentsQualified();
        }

        if (!isAllowed) {
            StatusLabel.showError(lblStatus, "No puedes subir este documento aún. Completa y aprueba la fase anterior.");
        }

        return isAllowed;
    }

    private boolean documentExists(DocumentType type) {
        boolean exists = false;
        String email = ActiveSessionDTO.get().getEmail();

        try {
            if (type == DocumentType.CLASS_SCHEDULE) {
                exists = initialDocumentDAO.hasClassScheduleByInternEmail(email);
            } else if (type == DocumentType.ACTIVITIES_SCHEDULE) {
                exists = initialDocumentDAO.hasActivitiesScheduleByInternEmail(email);
            } else if (type == DocumentType.PSP) {
                exists = initialDocumentDAO.hasPSPByInternEmail(email);
            } else if (type == DocumentType.INDICATOR_REPORT) {
                exists = initialDocumentDAO.hasPartialReportByInternEmail(email);
            } else if (type == DocumentType.SELF_EVALUATION) {
                exists = initialDocumentDAO.hasSelfEvaluationByInternEmail(email);
            } else if (type == DocumentType.EVALUATION_LINKED_ORGANIZATION) {
                exists = initialDocumentDAO.hasEvaluationLinkedOrganizationByInternEmail(email);
            } else if (type == DocumentType.FINAL_REPORT) {
                exists = finalReportDAO.hasFinalReportByInternEmail(email);
            }
            // Agrega aquí los else if faltantes (Reporte Mensual, Plan Actividades) según tus métodos DAO
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al buscar el documento en la base de datos.");
        }

        return exists;
    }

    @FXML
    private void confirm() {
        if (isValidDocument()) {
            if (setFileMetadata()) {
                if (saveDataDocument()) {
                    StatusLabel.showSuccess(lblStatus, "Archivo cargado exitosamente");
                    resetSelection();
                }
            }
        }
    }

    private boolean isValidDocument() {
        boolean isValid = false;

        if (selectedDocument == null) {
            StatusLabel.showError(lblStatus, "No se ha elegido un archivo.");
        } else {
            String extension = FileUtils.getExtension(selectedDocument.getName());

            if (!FileUtils.ALLOWED_EXTENSIONS.contains(extension)) {
                StatusLabel.showError(lblStatus, "Formato invalido. Solo se acepta PDF o DOCX.");
            } else if (selectedDocument.length() == 0) {
                StatusLabel.showError(lblStatus, "El documento está vacío y no puede guardarse.");
            } else if (selectedDocument.length() > FileUtils.MAX_BYTES) {
                StatusLabel.showError(lblStatus, "El tamaño de archivo excede el permitido.");
            } else {
                isValid = true;
            }
        }

        return isValid;
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
        } catch (FileManagementException | DAOException e) {
            StatusLabel.showError(lblStatus, "No se puede guardar el archivo. Intente de nuevo.");
        }

        return saveStatus;
    }

    private boolean saveDataDocument() {
        boolean success = false;

        try {
            String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
            success = initialDocumentDAO.saveDocument(studentNumber, initialDocumentDTO);

            if (!success) {
                StatusLabel.showError(lblStatus, "Error al guardar documento. Intenta de nuevo.");
            }
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al guardar documento");
        }

        return success;
    }

    private void resetSelection() {
        lblSelectedDocument.setText("Ningún archivo seleccionado");
        selectedDocument = null;
        currentFolder = null;
        currentPrefix = null;
    }

    private boolean areInitialDocumentsQualified() {
        // boolean qualified = false;
        // try {
        //    qualified = documentDAO.checkInitialDocsStatus(ActiveSessionDTO.get().getEmail());
        // } catch(...)
        // return qualified;
        return true;
    }

    private boolean arePracticeDocumentsQualified() {
        // boolean qualified = false;
        // try {
        //    qualified = documentDAO.checkPracticelDocsStatus(ActiveSessionDTO.get().getEmail());
        // } catch(...)
        // return qualified;
        return true;
    }
}