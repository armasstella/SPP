package spp.presentation.controller.intern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Window;
import spp.businesslogic.compliance.document.DocumentationRegistry;
import spp.businesslogic.compliance.document.DocumentationWorkflowManager;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.UserDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.InternDocumentDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.enums.DocumentationPhase;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.FileManagementException;
import spp.utils.file.DocumentUploadConfiguration;
import spp.utils.file.FileUtils;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.filechooser.AllowedExtension;
import spp.utils.view.filechooser.FileChooserHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;

import java.io.File;
import java.time.LocalDateTime;

public class UploadDocumentsController {

    @FXML private Label lblStatus;
    @FXML private Label lblSelectedDocument;

    private File selectedDocument;
    private String currentFolder;
    private String currentPrefix;

    private final InternDocumentDTO internDocumentDTO = new InternDocumentDTO();
    private final InternDocumentDAO internDocumentDAO = new InternDocumentDAO();
    private final InternDAO internDAO = new InternDAO();
    private final DocumentationRegistry documentationRegistry = new DocumentationRegistry();

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView(
                "/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante",
                event
        );
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
                DocumentationPhase.PRACTICE
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
                DocumentType.PARTIAL_REPORT,
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
        UserDAO  userDAO = new UserDAO();
        int internId = -1;
        try {
            internId = userDAO.obtainId(ActiveSessionDTO.get().getEmail());
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        DocumentationWorkflowManager workflowManager = new DocumentationWorkflowManager(internId);
        DocumentType targetType = documentConfiguration.getType();
        boolean isUploadAllowed = workflowManager.isUploadAllowed(targetType);

        if (!isUploadAllowed) {
            StatusLabel.showError(lblStatus, "No puedes subir este documento aún. Completa y aprueba la fase anterior.");
        } else {
            verifyExistenceAndSelectDocument(event, documentConfiguration);
        }
    }

    private void verifyExistenceAndSelectDocument(ActionEvent event, DocumentUploadConfiguration documentConfiguration) {
        String userEmail = ActiveSessionDTO.get().getEmail();
        DocumentType targetType = documentConfiguration.getType();

        try {
            boolean documentExists = documentationRegistry.isDocumentAlreadyUploaded(targetType, userEmail);

            if (documentExists) {
                StatusLabel.showError(lblStatus, "Ya ha subido este documento.\nComuníquese con el coordinador.");
            } else {
                openFileChooser(event, documentConfiguration);
            }
        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, "Error al buscar el documento en la base de datos.");
        }
    }

    private void openFileChooser(ActionEvent event, DocumentUploadConfiguration documentConfiguration) {
        Object eventSource = event.getSource();
        Node sourceNode = (Node) eventSource;
        Scene currentScene = sourceNode.getScene();
        Window currentWindow = currentScene.getWindow();

        String dialogTitle = "SELECCIONAR DOCUMENTO";
        AllowedExtension pdfExtension = AllowedExtension.PDF;
        AllowedExtension docxExtension = AllowedExtension.DOCX;

        File chosenFile = FileChooserHelper.selectSingleFile(
                currentWindow,
                dialogTitle,
                pdfExtension,
                docxExtension
        );

        if (chosenFile != null) {
            selectedDocument = chosenFile;

            DocumentType configurationType = documentConfiguration.getType();
            String documentTypeString = String.valueOf(configurationType);
            internDocumentDTO.setDocumentType(documentTypeString);

            currentFolder = documentConfiguration.getFolder();
            currentPrefix = documentConfiguration.getPrefix();

            String fileName = selectedDocument.getName();
            String statusMessage = "Archivo seleccionado: " + fileName;

            lblSelectedDocument.setText(statusMessage);
            StatusLabel.showSuccess(lblStatus, "Archivo listo para subir.");
        }
    }

    @FXML
    private void confirm() {
        boolean isDocumentValid = isValidDocument();

        if (isDocumentValid) {
            boolean isMetadataConfigured = setFileMetadata();
            if (isMetadataConfigured) {
                boolean isDataSaved = saveDataDocument();
                if (isDataSaved) {
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
            String fileName = selectedDocument.getName();
            String extension = FileUtils.getExtension(fileName);
            long fileLength = selectedDocument.length();

            boolean isAllowedExtension = FileUtils.ALLOWED_EXTENSIONS.contains(extension);

            if (!isAllowedExtension) {
                StatusLabel.showError(lblStatus, "Formato invalido. Solo se acepta PDF o DOCX.");
            } else if (fileLength == 0) {
                StatusLabel.showError(lblStatus, "El documento está vacío y no puede guardarse.");
            } else if (fileLength > FileUtils.MAX_BYTES) {
                StatusLabel.showError(lblStatus, "El tamaño de archivo excede el permitido.");
            } else {
                isValid = true;
            }
        }

        return isValid;
    }

    private boolean setFileMetadata() {
        boolean saveStatus = false;

        if (selectedDocument != null) {
            String fileName = selectedDocument.getName();
            String extension = FileUtils.getExtension(fileName);

            try {
                String userEmail = ActiveSessionDTO.get().getEmail();
                String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

                String uniqueName = FileUtils.generateUniqueName(studentNumber, extension, currentPrefix);
                String finalPath = FileUtils.copyFile(selectedDocument, currentFolder, uniqueName);

                long fileLengthBytes = selectedDocument.length();
                double fileLengthMb = (double) fileLengthBytes / FileUtils.BYTES_PER_MB;
                LocalDateTime currentDateTime = LocalDateTime.now();

                internDocumentDTO.setOriginalName(fileName);
                internDocumentDTO.setSavedName(uniqueName);
                internDocumentDTO.setFilePath(finalPath);
                internDocumentDTO.setSizeMb(fileLengthMb);
                internDocumentDTO.setExtension(extension);
                internDocumentDTO.setUploadDate(currentDateTime);

                saveStatus = true;
            } catch (FileManagementException | DAOException exception) {
                StatusLabel.showError(lblStatus, "No se puede guardar el archivo. Intente de nuevo.");
            }
        }

        return saveStatus;
    }

    private boolean saveDataDocument() {
        boolean success = false;

        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

            boolean isDocumentSaved = internDocumentDAO.saveDocument(studentNumber, internDocumentDTO);

            if (isDocumentSaved) {
                success = true;
            } else {
                StatusLabel.showError(lblStatus, "Error al guardar documento. Intenta de nuevo.");
            }
        } catch (DAOException exception) {
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

}