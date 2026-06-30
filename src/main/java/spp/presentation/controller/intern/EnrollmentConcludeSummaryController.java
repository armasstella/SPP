package spp.presentation.controller.intern;

import com.dlsc.pdfviewfx.PDFView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.InternEnrollmentConcludeDTO;
import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class EnrollmentConcludeSummaryController implements Initializable {

    @FXML private Label lblStudentName;
    @FXML private Label lblStudentNumber;
    @FXML private Label lblProjectName;
    @FXML private Label lblCompanyName;
    @FXML private Label lblInstructorName;
    @FXML private Label lblFinalGrade;
    @FXML private ComboBox<String> cmbDocumentCategory;
    @FXML private ComboBox<ReportDocumentFileDTO> cmbUploadedDocuments;
    @FXML private PDFView pdfView;
    @FXML private Label lblStatus;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSummaryInformation();
        loadDocumentCategories();
        resetDocumentSelectionArea();
    }

    private void loadSummaryInformation() {
        String activeEmail = ActiveSessionDTO.get().getEmail();
        ProfessionalPracticeEnrollmentDAO professionalPracticeEnrollmentDAO = new ProfessionalPracticeEnrollmentDAO();
        try {
            InternEnrollmentConcludeDTO enrollmentConcludeDTO = professionalPracticeEnrollmentDAO.getEnrollmentConcludeDatayByInternEmail(activeEmail);
            if (enrollmentConcludeDTO != null) {
                lblStudentName.setText("Nombre: " + enrollmentConcludeDTO.getStudentName());
                lblStudentNumber.setText("Matrícula: " + enrollmentConcludeDTO.getStudentNumber());
                lblProjectName.setText("Proyecto: " + enrollmentConcludeDTO.getProjectName());
                lblCompanyName.setText("Empresa: " + enrollmentConcludeDTO.getCompanyName());
                lblInstructorName.setText("Profesor: " + enrollmentConcludeDTO.getInstructorName());

                if (enrollmentConcludeDTO.getFinalGrade() != null) {
                    lblFinalGrade.setText("Calificación Final: " + enrollmentConcludeDTO.getFinalGrade());
                } else {
                    lblFinalGrade.setText("Calificación Final: Pendiente");
                }
            }
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void loadDocumentCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("Documentos Iniciales");
        categories.add("Documentos de Prácticas");
        categories.add("Documentos de Cierre");

        cmbDocumentCategory.setItems(FXCollections.observableArrayList(categories));
    }

    @FXML
    public void onCategorySelected(ActionEvent event) {
        String selectedCategory = cmbDocumentCategory.getValue();
        if (selectedCategory != null) {
            loadDocumentsByCategory(selectedCategory);
        }
    }

    private void loadDocumentsByCategory(String category) {
        cmbUploadedDocuments.getItems().clear();
        cmbUploadedDocuments.setDisable(true);
        // Reseteamos el visor PDF por si había otro abierto
        // pdfView.unload(); // Dependiendo de la API específica de pdfviewfx, puede haber un clear() o se deja vacío

        // Supuesta llamada al DAO para recuperar la lista de documentos según la categoría elegida:
        // try {
        //     List<ReportDocumentFileDTO> documents = documentDAO.getDocumentsByCategoryAndIntern(category, ActiveSessionDTO.get().getEmail());
        //     if (!documents.isEmpty()) {
        //         for (ReportDocumentFileDTO doc : documents) {
        //             cmbUploadedDocuments.getItems().add(doc);
        //         }
        //         cmbUploadedDocuments.setDisable(false);
        //         StatusLabel.showSuccess(lblStatus, "Documentos cargados para la categoría: " + category);
        //     } else {
        //         StatusLabel.showError(lblStatus, "No hay documentos subidos en esta categoría.");
        //     }
        // } catch (DAOException e) {
        //     StatusLabel.showError(lblStatus, e.getMessage());
        // }
    }

    @FXML
    public void onDocumentSelected(ActionEvent event) {
        ReportDocumentFileDTO selectedDocument = cmbUploadedDocuments.getValue();
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
            StatusLabel.showError(lblStatus, "El archivo PDF no se encontró en el servidor local.");
        }
    }

    private void resetDocumentSelectionArea() {
        cmbDocumentCategory.getSelectionModel().clearSelection();
        cmbUploadedDocuments.getItems().clear();
        cmbUploadedDocuments.setDisable(true);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);

    }
}