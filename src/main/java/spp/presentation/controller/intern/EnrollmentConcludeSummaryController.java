package spp.presentation.controller.intern;

import com.dlsc.pdfviewfx.PDFView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
// Importa tu DAO de documentos aquí, ej: import spp.businesslogic.dao.DocumentDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.InternEnrollmentConcludeDTO;
import spp.businesslogic.dto.InternDocumentDTO; // <-- DTO actualizado
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EnrollmentConcludeSummaryController implements Initializable {

    @FXML private Label lblStudentName;
    @FXML private Label lblStudentNumber;
    @FXML private Label lblProjectName;
    @FXML private Label lblCompanyName;
    @FXML private Label lblInstructorName;
    @FXML private Label lblFinalGrade;
    @FXML private ComboBox<InternDocumentDTO> cmbUploadedDocuments;
    @FXML private PDFView pdfView;
    @FXML private Label lblStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSummaryInformation();
        loadAllDocuments();
    }

    private void loadSummaryInformation() {
        String activeEmail = ActiveSessionDTO.get().getEmail();
        ProfessionalPracticeEnrollmentDAO enrollmentDAO = new ProfessionalPracticeEnrollmentDAO();
        try {
            InternEnrollmentConcludeDTO enrollmentDTO = enrollmentDAO.getEnrollmentConcludeDatayByInternEmail(activeEmail);
            if (enrollmentDTO != null) {
                lblStudentName.setText("Nombre: " + enrollmentDTO.getStudentName());
                lblStudentNumber.setText("Matrícula: " + enrollmentDTO.getStudentNumber());
                lblProjectName.setText("Proyecto: " + enrollmentDTO.getProjectName());
                lblCompanyName.setText("Empresa: " + enrollmentDTO.getCompanyName());
                lblInstructorName.setText("Profesor: " + enrollmentDTO.getInstructorName());

                if (enrollmentDTO.getFinalGrade() != null) {
                    lblFinalGrade.setText("Calificación Final: " + enrollmentDTO.getFinalGrade());
                } else {
                    lblFinalGrade.setText("Calificación Final: Pendiente");
                }
            }
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void loadAllDocuments() {
        String email = ActiveSessionDTO.get().getEmail();
        InternDocumentDAO internDocumentDAO = new InternDocumentDAO();

        try {
            List<InternDocumentDTO> documents = internDocumentDAO.getDocumentsByConcludedEnrollment(email);

            if (!documents.isEmpty()) {
                cmbUploadedDocuments.setItems(FXCollections.observableArrayList(documents));
                StatusLabel.showSuccess(lblStatus, "Documentos cargados correctamente.");
            } else {
                StatusLabel.showError(lblStatus, "No se encontraron documentos para esta inscripción.");
            }
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus,  e.getMessage());
        }
    }

    @FXML
    public void onDocumentSelected(ActionEvent event) {
        InternDocumentDTO selectedDocument = cmbUploadedDocuments.getValue();
        if (selectedDocument != null) {
            displayPdf(selectedDocument.getFilePath());
        }
    }

    private void displayPdf(String relativePath) {
        File pdfFile = new File(relativePath);
        if (pdfFile.exists()) {
            pdfView.load(pdfFile);
            StatusLabel.showSuccess(lblStatus, "Documento visualizado correctamente.");
        } else {
            StatusLabel.showError(lblStatus, "El archivo PDF no se encontró en la ruta especificada.");
        }
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml", "Inicia sesión", event);
    }
}