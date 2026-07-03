package spp.presentation.controller.intern;

import com.dlsc.pdfviewfx.PDFView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.dao.UserDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ReviewedDocumentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.table.DoubleClickListener;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.table.TableViewConfigurator;
import spp.utils.view.window.ViewNavigator;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;

public class InternProgressController implements Initializable, DoubleClickListener<ReviewedDocumentDTO> {

    public PDFView pdfUploadView;
    @FXML private Label lblStatus;
    @FXML private PDFView pdfView;
    @FXML private TableView<ReviewedDocumentDTO> tblReviewedDocuments;
    @FXML private TableColumn<ReviewedDocumentDTO, String> colDocumentType;
    @FXML private TableColumn<ReviewedDocumentDTO, String> colStatus;
    @FXML private TableColumn<ReviewedDocumentDTO, String> colGrade;
    @FXML private TableColumn<ReviewedDocumentDTO, String> colReviewedDate;
    @FXML private TableColumn<ReviewedDocumentDTO, String> colComments;
    private ObservableList<ReviewedDocumentDTO> observableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainReviewedDocuments();
        TableViewConfigurator.enableDoubleClickSelection(tblReviewedDocuments, this);
    }

    @Override
    public void onItemSelected(ReviewedDocumentDTO selectedReviewedDocument) {
        displayPdf(selectedReviewedDocument.getPathFile());

    }

    private void displayPdf(String relativePath) {
        File pdfFile = new File(relativePath);
        if (pdfFile.exists()) {
            pdfView.load(pdfFile);
        } else {
            StatusLabel.showError(lblStatus, "El archivo PDF no se encontró en el servidor local.");
        }
    }

    private void setUpColumns() {
        GenericNestedSelector<ReviewedDocumentDTO> typeSelector =
                new GenericNestedSelector<>("documentType", "Valor no encontrado");
        GenericNestedSelector<ReviewedDocumentDTO> gradeStatusSelector =
                new GenericNestedSelector<>("status", "Valor no encontrado");
        GenericNestedSelector<ReviewedDocumentDTO> gradeSelector =
                new GenericNestedSelector<>("grade", "Valor no encontrado");
        GenericNestedSelector<ReviewedDocumentDTO> commentsSelector =
                new GenericNestedSelector<>("comments", "Valor no encontrado");
        GenericNestedSelector<ReviewedDocumentDTO> revisionDateSelector =
                new GenericNestedSelector<>("revisionDate", "Valor no encontrado");

        colDocumentType.setCellValueFactory(typeSelector);
        colStatus.setCellValueFactory(gradeStatusSelector);
        colGrade.setCellValueFactory(gradeSelector);
        colComments.setCellValueFactory(commentsSelector);
        colReviewedDate.setCellValueFactory(revisionDateSelector);
    }

    private void obtainReviewedDocuments() {
        InternDocumentDAO internDocumentDAO = new InternDocumentDAO();
        try {
            UserDAO userDAO = new UserDAO();
            int internId = userDAO.obtainId(ActiveSessionDTO.get().getEmail());
            List<ReviewedDocumentDTO> reviewedDocumentList = internDocumentDAO.findGradedDocumentsByInternEmail(internId);
            if (reviewedDocumentList.isEmpty()) {
                StatusLabel.showError(lblStatus, "No tiene documentos subidos");
            } else {
                StatusLabel.showSuccess(lblStatus, "Documentos encontrados");
            }
            ObservableList<ReviewedDocumentDTO> reviewedDocumentsObservableList = FXCollections.observableArrayList(reviewedDocumentList);
            tblReviewedDocuments.setItems(reviewedDocumentsObservableList);
        } catch (DAOException | NullPointerException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

    }

    @FXML
    private void goBackToMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);

    }


}