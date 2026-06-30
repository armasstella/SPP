package spp.presentation.controller.instructor;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.compliance.document.DocumentationWorkflowManager;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.enums.DocumentationPhase;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class PracticeReleaseController implements Initializable {

    private static final int MINIMUM_GRADE = 0;
    private static final int MAXIMUM_GRADE = 10;
    @FXML private ComboBox<InternDTO> cmbInterns;
    @FXML private TextField txtFinalGrade;
    @FXML private Label lblStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadReleasableInterns();
    }

    private void loadReleasableInterns() {
        try {
            String professorEmail = ActiveSessionDTO.get().getEmail();
            InternDAO internDAO = new InternDAO();
            List<InternDTO> assignedInterns = internDAO.getInternsReadyForReleaseByProfessorEmail(professorEmail);
            ObservableList<InternDTO> releasableInterns = filterReleasableInterns(assignedInterns);
            cmbInterns.setItems(releasableInterns);
        } catch (DAOException daoException) {
            StatusLabel.showError(lblStatus, daoException.getMessage());
        }
    }

    private ObservableList<InternDTO> filterReleasableInterns(List<InternDTO> assignedInterns) {
        ObservableList<InternDTO> releasableInterns = FXCollections.observableArrayList();

        for (InternDTO intern : assignedInterns) {
            int internId = intern.getId();
            DocumentationWorkflowManager workflowManager = new DocumentationWorkflowManager(internId);
            DocumentationPhase currentPhase = workflowManager.getCurrentPhase();
            boolean isInClosurePhase = currentPhase == DocumentationPhase.CLOSURE;
            boolean hasNoFinalGrade = intern.getFinalGrade() == null;
            if (isInClosurePhase && hasNoFinalGrade) {
                releasableInterns.add(intern);
            }
        }

        return releasableInterns;
    }

    @FXML
    public void assignFinalGrade(ActionEvent event) {
        InternDTO selectedIntern = cmbInterns.getValue();
        String gradeText = txtFinalGrade.getText().trim();

        if (selectedIntern == null) {
            StatusLabel.showError(lblStatus, "Seleccione un practicante.");
        } else if (isGradeInvalid(gradeText)) {
            StatusLabel.showError(lblStatus, "La calificación debe ser un número entre 0 y 10.");
        } else {
            confirmAndAssignGrade(selectedIntern, gradeText);
        }
    }

    private boolean isGradeInvalid(String gradeText) {
        boolean isInvalid = false;

        if (gradeText.isEmpty()) {
            isInvalid = true;
        } else {
            try {
                int grade = Integer.parseInt(gradeText);
                if (grade < MINIMUM_GRADE || grade > MAXIMUM_GRADE) {
                    isInvalid = true;
                }
            } catch (NumberFormatException numberFormatException) {
                isInvalid = true;
            }
        }

        return isInvalid;
    }

    private void confirmAndAssignGrade(InternDTO selectedIntern, String gradeText) {
        boolean professorConfirmed = AlertHelper.showConfirmation("Asignar calificación final",
                "Una vez asignada la calificación, el practicante pasará a estado 'Concluida'. ¿Desea continuar?");
        if (professorConfirmed) {
            saveFinalGrade(selectedIntern, gradeText);
        }
    }

    private void saveFinalGrade(InternDTO selectedIntern, String gradeText) {
        try {
            int finalGrade = Integer.parseInt(gradeText);
            int internId = selectedIntern.getId();
            String studentNumber = selectedIntern.getStudentNumber();
            ProfessionalPracticeEnrollmentDAO practiceEnrollmentDAODAO = new ProfessionalPracticeEnrollmentDAO();
            boolean gradeAssigned = practiceEnrollmentDAODAO.assignFinalGrade(internId, studentNumber, finalGrade);
            if (gradeAssigned) {
                removeReleasedIntern(selectedIntern);
                AlertHelper.showMessage("Calificación asignada",
                        "La calificación se asignó y el practicante quedó en estado 'Concluido'.");
            } else {
                StatusLabel.showError(lblStatus, "No se pudo asignar la calificación.");
            }
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void removeReleasedIntern(InternDTO releasedIntern) {
        ObservableList<InternDTO> releasableInterns = cmbInterns.getItems();
        releasableInterns.remove(releasedIntern);
        cmbInterns.setValue(null);
        txtFinalGrade.clear();
    }

    @FXML
    public void goToInstructorMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                "Menú Profesor", event);
    }
}
