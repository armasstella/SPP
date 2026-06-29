package spp.presentation.controller.instructor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Window;
import spp.businesslogic.dao.InstructorDAO;
import spp.businesslogic.dao.PresentationTemplateDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.PresentationTemplateDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.FileManagementException;
import spp.utils.file.FileUtils;
import spp.utils.view.filechooser.AllowedExtension;
import spp.utils.view.filechooser.FileChooserHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.io.File;
import java.time.LocalDateTime;

public class PresentationTemplateUploadController {

    @FXML private Label lblStatus;
    @FXML private Label lblSelectedDocument;
    @FXML private Button btnUploadPresentationTemplate;

    private File selectedDocument;
    private String currentFolder;
    private String currentPrefix;

    private final PresentationTemplateDTO presentationTemplateDTO = new PresentationTemplateDTO();
    private final PresentationTemplateDAO presentationTemplateDAO = new PresentationTemplateDAO();

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView(
                "/spp/presentation/view/instructor/InstructorMenuView.fxml",
                "Menú Profesor",
                event
        );
    }

    @FXML
    private void uploadPresentationTemplate(ActionEvent event) {
        boolean isFileSelected = selectFile(event, "Seleccionar horario");

        if (isFileSelected) {
            String documentTypeString = String.valueOf(DocumentType.PRESENTATION_TEMPLATE);
            presentationTemplateDTO.setDocumentType(documentTypeString);
            currentFolder = "./documents/instructor_documents/presentationTemplates/";
            currentPrefix = "presentationTemplate";
        }
    }

    private boolean selectFile(ActionEvent event, String dialogTitle) {
        boolean isFileSelected = false;

        Node sourceNode = (Node) event.getSource();
        Scene currentScene = sourceNode.getScene();
        Window currentWindow = currentScene.getWindow();

        File file = FileChooserHelper.selectSingleFile(
                currentWindow,
                dialogTitle,
                AllowedExtension.PDF,
                AllowedExtension.DOCX
        );

        if (file != null) {
            selectedDocument = file;
            String fileName = selectedDocument.getName();

            lblSelectedDocument.setText("Archivo seleccionado: " + fileName);
            StatusLabel.showSuccess(lblStatus, "Archivo listo para subir.");

            isFileSelected = true;
        }

        return isFileSelected;
    }

    @FXML
    private void confirm() {
        boolean hasErrors = hasValidationErrors();

        if (!hasErrors) {
            boolean isMetadataSet = setFileMetadata();

            if (isMetadataSet) {
                boolean isDataSaved = saveDataDocument();

                if (isDataSaved) {
                    StatusLabel.showSuccess(lblStatus, "Archivo cargado exitosamente");
                    lblSelectedDocument.setText("Ningún archivo seleccionado");
                    selectedDocument = null;
                    currentFolder = null;
                    currentPrefix = null;
                }
            }
        }
    }

    private boolean hasValidationErrors() {
        boolean hasErrors = false;

        if (selectedDocument == null) {
            StatusLabel.showError(lblStatus, "No se ha elegido un archivo.");
            hasErrors = true;
        } else {
            String fileName = selectedDocument.getName();
            String extension = FileUtils.getExtension(fileName);
            long fileLength = selectedDocument.length();

            if (!FileUtils.ALLOWED_EXTENSIONS.contains(extension)) {
                StatusLabel.showError(lblStatus, "Formato invalido. Solo se acepta PDF o DOCX.");
                hasErrors = true;
            } else if (fileLength == 0) {
                StatusLabel.showError(lblStatus, "El documento está vacío y no puede guardarse.");
                hasErrors = true;
            } else if (fileLength > FileUtils.MAX_BYTES) {
                StatusLabel.showError(lblStatus, "El tamaño de archivo excede el permitido.");
                hasErrors = true;
            }
        }

        return hasErrors;
    }

    private boolean setFileMetadata() {
        boolean saveStatus = false;

        if (selectedDocument != null) {
            String fileName = selectedDocument.getName();
            String extension = FileUtils.getExtension(fileName);

            try {
                InstructorDAO instructorDAO = new InstructorDAO();

                String userEmail = ActiveSessionDTO.get().getEmail();
                String personalNumber = instructorDAO.findActivePersonalNumberByEmail(userEmail);

                String uniqueName = FileUtils.generateUniqueName(personalNumber, extension, currentPrefix);
                String finalPath = FileUtils.copyFile(selectedDocument, currentFolder, uniqueName);

                long fileLength = selectedDocument.length();
                double sizeInMb = (double) fileLength / FileUtils.BYTES_PER_MB;
                LocalDateTime currentDateTime = LocalDateTime.now();

                presentationTemplateDTO.setOriginalName(fileName);
                presentationTemplateDTO.setSavedName(uniqueName);
                presentationTemplateDTO.setFilePath(finalPath);
                presentationTemplateDTO.setSizeMb(sizeInMb);
                presentationTemplateDTO.setExtension(extension);
                presentationTemplateDTO.setUploadDate(currentDateTime);

                saveStatus = true;

            } catch (FileManagementException | DAOException e) {
                StatusLabel.showError(lblStatus, e.getMessage());
            }
        }

        return saveStatus;
    }

    private boolean saveDataDocument() {
        boolean success = false;

        try {
            InstructorDAO instructorDAO = new InstructorDAO();

            String userEmail = ActiveSessionDTO.get().getEmail();
            String personalNumber = instructorDAO.findActivePersonalNumberByEmail(userEmail);

            success = presentationTemplateDAO.saveDocument(personalNumber, presentationTemplateDTO);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }

        if (!success) {
            StatusLabel.showError(lblStatus, "Error al guardar documento. Intenta de nuevo.");
        }

        return success;
    }
}