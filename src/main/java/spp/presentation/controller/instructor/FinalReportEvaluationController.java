package spp.presentation.controller.instructor;

import com.dlsc.pdfviewfx.PDFView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.FinalReportDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ReportDocumentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.ViewNavigator;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FinalReportEvaluationController implements Initializable {

    @FXML private ComboBox<InternDTO> cmbInterns;
    @FXML private ComboBox<ReportDocumentDTO> cmbInternDocuments;
    @FXML private PDFView pdfView;
    @FXML private TextField txtGrade;
    @FXML private Button btnAssignGrade;
    @FXML private Button btnModifyGrade;
    @FXML private Label lblStatus;

    private final FinalReportDAO finalReportDAO = new FinalReportDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadInternsForProfessor();
        resetEvaluationArea();
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtGrade,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY );
    }

    private void loadInternsForProfessor() {
        try {
            List<InternDTO> interns = finalReportDAO.getAssignedInternsByProfessorEmail(ActiveSessionDTO.get().getEmail());
            cmbInterns.setItems(FXCollections.observableArrayList(interns));

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al cargar la lista de estudiantes.");
        }
    }

    @FXML
    public void onInternSelected(ActionEvent event) {
        InternDTO selectedIntern = cmbInterns.getValue();
        if (selectedIntern != null) {
            loadDocumentsForIntern(selectedIntern.getStudentNumber());
        }
    }

    private void loadDocumentsForIntern(String studentNumber) {
        cmbInternDocuments.getItems().clear();
        resetEvaluationArea();

        try {
            List<ReportDocumentDTO> documentsList = finalReportDAO.getFinalReportsByIntern(studentNumber);

            if (!documentsList.isEmpty()) {
                cmbInternDocuments.setItems(FXCollections.observableArrayList(documentsList));
                cmbInternDocuments.setDisable(false);
                StatusLabel.showSuccess(lblStatus, "Reportes cargados. Seleccione uno para evaluar.");
            } else {
                StatusLabel.showError(lblStatus, "El estudiante seleccionado no tiene reportes finales subidos.");
            }

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al cargar los documentos.");
        }
    }

    @FXML
    public void onDocumentSelected(ActionEvent event) {
        ReportDocumentDTO selectedDocument = cmbInternDocuments.getValue();
        if (selectedDocument != null) {
            displayPdf(selectedDocument.getFilePath());
            configureGradeButtons(selectedDocument);
        }
    }

    private void displayPdf(String relativePath) {
        File pdfFile = new File(relativePath);
        if (pdfFile.exists()) {
            pdfView.load(pdfFile);
        } else {
            StatusLabel.showError(lblStatus, "El archivo PDF no se encontró en el servidor local.");
        }
    }

    private void configureGradeButtons(ReportDocumentDTO document) {
        txtGrade.setDisable(false);
        if (document.isGraded()) {
            txtGrade.setText(String.valueOf(document.getGrade()));
            btnAssignGrade.setDisable(true);
            btnModifyGrade.setDisable(false);
        } else {
            txtGrade.setText("");
            btnAssignGrade.setDisable(false);
            btnModifyGrade.setDisable(true);
        }
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtGrade.getText().isBlank()) {
            emptyFields = true;
        }

        return emptyFields;
    }

    private boolean hasValidGradeRange() {
        boolean validRange = false;
        int grade = Integer.parseInt(txtGrade.getText());

        if (grade >= ViewConstant.MIN_GRADE && grade <= ViewConstant.MAX_GRADE) {
            validRange = true;
        }

        return validRange;
    }

    private boolean isGradeInputValid() {
        boolean isValid = false;

        if (hasEmptyFields()) {
            StatusLabel.showError(lblStatus, "Ingrese una calificación válida.");
        } else {
            if (hasValidGradeRange()) {
                isValid = true;
            } else {
                StatusLabel.showError(lblStatus, "La calificación debe ser un número entre 0 y 10.");
            }
        }

        return isValid;
    }

    @FXML
    public void assignGrade(ActionEvent event) {
        if (isGradeInputValid()) {
            try {
                ReportDocumentDTO selectedDocument = cmbInternDocuments.getValue();
                int grade = Integer.parseInt(txtGrade.getText());
                String email = ActiveSessionDTO.get().getEmail();

                if (finalReportDAO.assignGrade(selectedDocument.getDocumentId(), email, grade)) {
                    selectedDocument.setGraded(true);
                    selectedDocument.setGrade(grade);
                    configureGradeButtons(selectedDocument);

                    StatusLabel.showSuccess(lblStatus, "Calificación asignada correctamente.");
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "Error al guardar la calificación.");
            }
        }
    }

    @FXML
    public void modifyGrade(ActionEvent event) {
        if (isGradeInputValid()) {
            try {
                ReportDocumentDTO selectedDocument = cmbInternDocuments.getValue();
                int grade = Integer.parseInt(txtGrade.getText());

                if (finalReportDAO.updateGrade(selectedDocument.getDocumentId(), grade)) {
                    selectedDocument.setGrade(grade);
                    StatusLabel.showSuccess(lblStatus, "Calificación actualizada correctamente.");
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "Error al actualizar la calificación.");
            }
        }
    }

    private void resetEvaluationArea() {
        cmbInternDocuments.setDisable(true);
        txtGrade.clear();
        txtGrade.setDisable(true);
        btnAssignGrade.setDisable(true);
        btnModifyGrade.setDisable(true);
    }

    @FXML
    public void goToProfessorMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                "Menú Profesor", event);
    }
}
