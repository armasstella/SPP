package spp.presentation.controller.instructor;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import spp.businesslogic.compliance.document.InternDocumentManager;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.InternDocumentReviewDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.instructor.listener.InternSelectionListener;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReviewDocumentController implements Initializable, InternSelectionListener {

    @FXML private Label lblStatus;
    @FXML private InternSelectorController internSelectorController;
    @FXML private DocumentEvaluatorController documentEvaluatorController;

    private InternDTO currentlySelectedIntern;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        internSelectorController.setInternSelectionListener(this);
        loadInitialData();
        documentEvaluatorController.setStatusLabel(lblStatus);
        internSelectorController.setStatusLabel(lblStatus);
    }

    private void loadInitialData() {
        try {
            String professorEmail = ActiveSessionDTO.get().getEmail();
            InternDocumentManager internDocumentManager = new InternDocumentManager();
            List<InternDTO> prioritizedInterns = internDocumentManager.getPrioritizedInternsForProfessor(professorEmail);

            internSelectorController.displayInterns(prioritizedInterns);

        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, "Error al cargar la lista de practicantes asignados.");
        }
    }

    @Override
    public void onInternSelected(InternDTO intern) {
        if (intern != null) {
            this.currentlySelectedIntern = intern;
            documentEvaluatorController.loadDocumentForIntern(intern);
            StatusLabel.clear(lblStatus);
        }
    }

    @FXML
    private void saveEvaluation(ActionEvent event) {
        boolean hasSelection = currentlySelectedIntern != null;

        if (!hasSelection) {
            StatusLabel.showError(lblStatus, "Por favor, seleccione un practicante de la lista.");
        } else {
            Integer grade = documentEvaluatorController.getSelectedGrade();
            String observations = documentEvaluatorController.getObservations();

            boolean isGradeValid = grade != null;

            if (!isGradeValid) {
                StatusLabel.showError(lblStatus, "Debe asignar una calificación obligatoriamente.");
            } else {
                List<InternDocumentReviewDTO> documents = currentlySelectedIntern.getDocuments();
                InternDocumentReviewDTO targetDocument = this.getFirstPendingDocument(documents);

                boolean hasDocument = targetDocument != null;

                if (!hasDocument) {
                    StatusLabel.showError(lblStatus, "El practicante seleccionado no tiene documentos pendientes de evaluar.");
                } else {
                    try {
                        int documentId = targetDocument.getInternDocumentId();
                        int gradeValue = grade.intValue();

                        InternDocumentDAO internDocumentDAO = new InternDocumentDAO();
                        boolean isUpdated = internDocumentDAO.assignGrade(documentId, gradeValue, observations);

                        if (isUpdated) {
                            StatusLabel.showSuccess(lblStatus, "Evaluación registrada correctamente.");
                            this.loadInitialData();
                            this.currentlySelectedIntern = null;
                            documentEvaluatorController.loadDocumentForIntern(null);
                        } else {
                            StatusLabel.showError(lblStatus, "No se pudo actualizar la calificación en la base de datos.");
                        }

                    } catch (DAOException exception) {
                        StatusLabel.showError(lblStatus, exception.getMessage());
                    }
                }
            }
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

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                "Menú Instructor", event);
    }

}