package spp.presentation.controller.instructor;

import com.dlsc.pdfviewfx.PDFView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.InternDocumentReviewDTO;
import spp.utils.view.label.StatusLabel;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DocumentEvaluatorController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private PDFView pdfView;
    @FXML private ComboBox<Integer> cmbGrade;
    @FXML private TextArea taObservations;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeGradeComboBox();
    }

    public void setStatusLabel(Label sharedStatusLabel) {
        this.lblStatus = sharedStatusLabel;
    }

    private void initializeGradeComboBox() {
        List<Integer> grades = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ObservableList<Integer> gradesObservableList = FXCollections.observableArrayList(grades);
        cmbGrade.setItems(gradesObservableList);
    }

    public void loadDocumentForIntern(InternDTO intern) {
        boolean isValidIntern = intern != null;

        if (isValidIntern) {
            List<InternDocumentReviewDTO> documents = intern.getDocuments();
            boolean hasDocuments = documents != null && !documents.isEmpty();

            if (hasDocuments) {
                InternDocumentReviewDTO targetDocument = this.getFirstPendingDocument(documents);

                boolean hasValidDocument = targetDocument != null;
                if (hasValidDocument) {
                    String documentPath = targetDocument.getFilePath();

                    boolean hasValidPath = documentPath != null;
                    if (hasValidPath) {
                       displayPdf(documentPath);
                    }
                }
            }

            taObservations.clear();
            cmbGrade.getSelectionModel().clearSelection();
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

    private InternDocumentReviewDTO getFirstPendingDocument(List<InternDocumentReviewDTO> documents) {
        int index = 0;
        int totalDocuments = documents.size();
        boolean found = false;
        InternDocumentReviewDTO pendingDocument = null;

        while (index < totalDocuments && !found) {
            InternDocumentReviewDTO currentDoc = documents.get(index);
            boolean isGraded = currentDoc.isGraded();

            if (!isGraded) {
                pendingDocument = currentDoc;
                found = true;
            }

            index++;
        }

        return pendingDocument;
    }

    public Integer getSelectedGrade() {
        Integer score = cmbGrade.getSelectionModel().getSelectedItem();
        return score;
    }

    public String getObservations() {
        String comments = taObservations.getText().trim();
        return comments;
    }
}