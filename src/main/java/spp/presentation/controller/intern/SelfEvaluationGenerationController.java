package spp.presentation.controller.intern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Window;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.SelfEvaluationDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.htmlbuilder.SelfEvaluationHtmlBuilder;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.filechooser.AllowedExtension;
import spp.utils.view.filechooser.FileChooserHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class SelfEvaluationGenerationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private Label lblStudentName;
    @FXML private Label lblStudentNumber;
    @FXML private Button btnGenerate;
    @FXML private ToggleGroup tgQuestionFirst;
    @FXML private ToggleGroup tgQuestionSecond;
    @FXML private ToggleGroup tgQuestionThird;
    @FXML private ToggleGroup tgQuestionFourth;
    @FXML private ToggleGroup tgQuestionFifth;
    @FXML private ToggleGroup tgQuestionSixth;
    @FXML private ToggleGroup tgQuestionSeventh;
    @FXML private ToggleGroup tgQuestionEighth;
    @FXML private ToggleGroup tgQuestionNinth;
    @FXML private ToggleGroup tgQuestionTenth;
    private List<ToggleGroup> toggleGroups;
    private SelfEvaluationDTO currentEvaluation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeToggleGroupsList();
        loadStudentData();
    }

    private void initializeToggleGroupsList() {
        toggleGroups = List.of(tgQuestionFirst, tgQuestionSecond, tgQuestionThird, tgQuestionFourth,
                tgQuestionFifth, tgQuestionSixth, tgQuestionSeventh, tgQuestionEighth,
                tgQuestionNinth, tgQuestionTenth);
    }

    private void loadStudentData() {
        try {
            String activeEmail = ActiveSessionDTO.get().getEmail();
            InternDAO internDAO = new InternDAO();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(activeEmail);

            SelfEvaluationDAO selfEvaluationDAO = new SelfEvaluationDAO();
            currentEvaluation = selfEvaluationDAO.findEvaluationHeaderByStudentNumber(studentNumber);

            if (currentEvaluation == null) {
                StatusLabel.showError(lblStatus, "No cuentas con un proyecto u organización asignada para evaluar.");
                btnGenerate.setDisable(true);
            } else {
                lblStudentName.setText(currentEvaluation.getStudentName());
                lblStudentNumber.setText(currentEvaluation.getStudentNumber());
            }

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    @FXML
    public void generateEvaluation(ActionEvent event) {
        boolean allAnswered = areAllQuestionsAnswered();

        if (!allAnswered) {
            StatusLabel.showError(lblStatus, "Por favor, responde a todas las afirmaciones.");
        } else {
            processEvaluationGeneration(event);
        }
    }

    private void processEvaluationGeneration(ActionEvent event) {
        try {
            SelfEvaluationDTO evaluation = buildEvaluationDTO();
            String htmlContent = SelfEvaluationHtmlBuilder.buildSelfEvaluation(evaluation);

            File outputFile = chooseOutputFileFromHelper(event);

            if (outputFile != null) {
                HtmlToPdfConverter.convertToFile(htmlContent, outputFile);
                if (persistSelfEvaluation()) {
                    StatusLabel.showSuccess(lblStatus, "Autoevaluación generada correctamente.");
                } else {
                    StatusLabel.showError(lblStatus, "La autoevaluación no fue guardada. Intente nuevamente");
                }
            }
        } catch (FileGenerationException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private boolean persistSelfEvaluation() {
        boolean isSelfEvaluationPersisted = false;
        SelfEvaluationDAO selfEvaluationDAO = new SelfEvaluationDAO();
        try {
            if (selfEvaluationDAO.saveSelfEvaluation(ActiveSessionDTO.get().getEmail())){
                isSelfEvaluationPersisted = true;
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage() + ". Genere nuevamente");
        }

        return isSelfEvaluationPersisted;

    }

    private boolean areAllQuestionsAnswered() {
        boolean allAnswered = true;

        for (ToggleGroup group : toggleGroups) {
            if (group.getSelectedToggle() == null) {
                allAnswered = false;
                break;
            }
        }
        return allAnswered;
    }

    private SelfEvaluationDTO buildEvaluationDTO() {
        SelfEvaluationDTO evaluationDTO = this.currentEvaluation;
        int[] scores = new int[toggleGroups.size()];
        int totalScore = 0;

        for (int i = 0; i < toggleGroups.size(); i++) {
            RadioButton selectedButton = (RadioButton) toggleGroups.get(i).getSelectedToggle();
            int scoreValue = Integer.parseInt(selectedButton.getUserData().toString());
            scores[i] = scoreValue;
            totalScore += scoreValue;
        }

        evaluationDTO.setScores(scores);
        evaluationDTO.setFinalScore(totalScore);
        return evaluationDTO;
    }

    private File chooseOutputFileFromHelper(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Scene currentScene = sourceNode.getScene();
        Window currentWindow = currentScene.getWindow();

        return FileChooserHelper.chooseOutputFile(
                currentWindow,
                DocumentType.SELF_EVALUATION,
                AllowedExtension.PDF
        );
    }

    @FXML
    public void goToInternMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);
    }
}