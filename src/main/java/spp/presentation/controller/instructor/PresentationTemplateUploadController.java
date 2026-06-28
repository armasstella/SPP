package spp.presentation.controller.instructor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import spp.utils.view.filechooser.FileChooserUtil;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.io.File;
import java.time.LocalDateTime;

public class PresentationTemplateUploadController {

    @FXML
    private Label lblStatus;
    @FXML private Label lblSelectedDocument;
    @FXML private Button btnUploadPresentationTemplate;
    private File selectedDocument;
    private String currentFolder;
    private String currentPrefix;
    private final PresentationTemplateDTO presentationTemplateDTO = new PresentationTemplateDTO();
    private final PresentationTemplateDAO presentationTemplateDAO = new PresentationTemplateDAO();

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                "Menú Profesor", event);

    }

    @FXML
    private void uploadPresentationTemplate(ActionEvent event) {
        if (selectFile(event, "Seleccionar horario")) {
            presentationTemplateDTO.setDocumentType(String.valueOf(DocumentType.PRESENTATION_TEMPLATE));
            currentFolder = "./documents/instructor_documents/presentationTemplates/";
            currentPrefix = "presentationTemplate";
        }

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
        if (validateFileInputs()) {
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

    private boolean validateFileInputs() {
        boolean areInputFieldsEmpty = false;
        if (selectedDocument == null) {
            StatusLabel.showError(lblStatus, "No se ha elegido un archivo.");
            areInputFieldsEmpty = true;
        }

        String extension = FileUtils.getExtension(selectedDocument.getName());

        if (!FileUtils.ALLOWED_EXTENSIONS.contains(extension)) {
            StatusLabel.showError(lblStatus, "Formato invalido. Solo se acepta PDF o DOCX.");
            areInputFieldsEmpty = true;
        }
        if (selectedDocument.length() == 0) {
            StatusLabel.showError(lblStatus, "El documento está vacío y no puede guardarse.");
            areInputFieldsEmpty = true;
        }
        if (selectedDocument.length() > FileUtils.MAX_BYTES) {
            StatusLabel.showError(lblStatus, "El tamaño de archivo excede el permitido.");
            areInputFieldsEmpty = true;
        }

        return areInputFieldsEmpty;
    }

    private boolean setFileMetadata() {
        boolean saveStatus = false;
        String extension = FileUtils.getExtension(selectedDocument.getName());

        try {
            InstructorDAO instructorDAO = new InstructorDAO();
            String personalNumber = instructorDAO.findActivePersonalNumberByEmail(
                    ActiveSessionDTO.get().getEmail());
            String uniqueName = FileUtils.generateUniqueName(personalNumber, extension, currentPrefix);
            String finalPath = FileUtils.copyFile(selectedDocument, currentFolder, uniqueName);

            presentationTemplateDTO.setOriginalName(selectedDocument.getName());
            presentationTemplateDTO.setSavedName(uniqueName);
            presentationTemplateDTO.setFilePath(finalPath);
            presentationTemplateDTO.setSizeMb(selectedDocument.length() / FileUtils.BYTES_PER_MB);
            presentationTemplateDTO.setExtension(extension);
            presentationTemplateDTO.setUploadDate(LocalDateTime.now());
            saveStatus = true;

        } catch (FileManagementException | DAOException  e) {
            StatusLabel.showError(lblStatus, "No se puede guardar el archivo. Intente de nuevo.");
        }

        return saveStatus;
    }

    private boolean saveDataDocument() {
        boolean success = false;
        try {
            InstructorDAO instructorDAO = new InstructorDAO();
            String personalNumber = instructorDAO.findActivePersonalNumberByEmail(
                    ActiveSessionDTO.get().getEmail());
            success = presentationTemplateDAO.saveDocument(personalNumber, presentationTemplateDTO);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al guardar documento");
        }

        if (!success) {
            StatusLabel.showError(lblStatus, "Error al guardar documento. Intenta de nuevo.");
        }

        return success;
    }

}
