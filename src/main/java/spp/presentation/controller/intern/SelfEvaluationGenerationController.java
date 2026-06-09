package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import spp.businesslogic.dao.SelfEvaluationDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.htmlbuilder.SelfEvaluationHtmlBuilder;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    private final SelfEvaluationDAO selfEvaluationDAO = new SelfEvaluationDAO();
    private SelfEvaluationDTO currentEvaluation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeToggleGroupsList();
        loadStudentData();

    }

    private void initializeToggleGroupsList() {
        toggleGroups = new ArrayList<>();
        toggleGroups.add(tgQuestionFirst);
        toggleGroups.add(tgQuestionSecond);
        toggleGroups.add(tgQuestionThird);
        toggleGroups.add(tgQuestionFourth);
        toggleGroups.add(tgQuestionFifth);
        toggleGroups.add(tgQuestionSixth);
        toggleGroups.add(tgQuestionSeventh);
        toggleGroups.add(tgQuestionEighth);
        toggleGroups.add(tgQuestionNinth);
        toggleGroups.add(tgQuestionTenth);

    }

    private void loadStudentData() {
        try {
            currentEvaluation = selfEvaluationDAO.obtainEvaluationData(ActiveSessionDTO.get().getEmail());

            if (currentEvaluation == null) {
                StatusLabel.showError(lblStatus, "No cuentas con un proyecto u organización asignada para evaluar.");
                btnGenerate.setDisable(true);
                return;
            }
            System.out.println(currentEvaluation);
            System.out.println(currentEvaluation.getStudentName());
            System.out.println(currentEvaluation.getStudentNumber());
            lblStudentName.setText(currentEvaluation.getStudentName());
            lblStudentNumber.setText(currentEvaluation.getStudentNumber());

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al cargar datos del alumno.");
        }
    }

    private SelfEvaluationDTO buildEvaluationDTO() {
        SelfEvaluationDTO selfEvaluationDTO = this.currentEvaluation;

        int[] scores = new int[10];
        int totalScore = 0;

        for (int i = 0; i < toggleGroups.size(); i++) {
            RadioButton selected = (RadioButton) toggleGroups.get(i).getSelectedToggle();
            int score = Integer.parseInt(selected.getUserData().toString());
            scores[i] = score;
            totalScore += score;
        }

        selfEvaluationDTO.setScores(scores);
        selfEvaluationDTO.setFinalScore(totalScore);

        return selfEvaluationDTO;
    }

    @FXML
    public void generateEvaluation(ActionEvent event) {
        if (!validateAllQuestionsAnswered()) {
            StatusLabel.showError(lblStatus, "Por favor, responde a todas las afirmaciones.");
            return;
        }

        try {
            SelfEvaluationDTO evaluation = buildEvaluationDTO();
            String html = SelfEvaluationHtmlBuilder.build(evaluation);

            File outputFile = chooseOutputFile(event, evaluation.getStudentNumber());
            if (outputFile == null) {
                return;
            }

            HtmlToPdfConverter.convertToFile(html, outputFile);
            StatusLabel.showSuccess(lblStatus, "Autoevaluación generada correctamente.");
        } catch (IOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "Error al generar el PDF.");
        }

    }

    private boolean validateAllQuestionsAnswered() {
        for (ToggleGroup tg : toggleGroups) {
            if (tg.getSelectedToggle() == null) {
                return false;
            }
        }
        return true;

    }

    private File chooseOutputFile(ActionEvent event, String studentNumber) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Autoevaluación");
        fileChooser.setInitialFileName("PRAIS_03_" + studentNumber + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        Window window = ((Node) event.getSource()).getScene().getWindow();
        return fileChooser.showSaveDialog(window);

    }

    @FXML
    public void goToInternMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);

    }

}