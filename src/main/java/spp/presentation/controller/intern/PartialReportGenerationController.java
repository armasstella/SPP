package spp.presentation.controller.intern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.PartialReportDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.PartialReportActivityDTO;
import spp.businesslogic.dto.PartialReportDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.htmlbuilder.PartialReportHtmlBuilder;
import spp.utils.view.ViewConstant;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.filechooser.AllowedExtension;
import spp.utils.view.filechooser.FileChooserHelper;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.ResourceBundle;

public class PartialReportGenerationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private Label lblStudentName;
    @FXML private Label lblStudentNumber;
    @FXML private Label lblProjectName;
    @FXML private Label lblLinkedOrganization;
    @FXML private TextField txtReportNumber;
    @FXML private TextField txtTerm;
    @FXML private TextArea taObjective;
    @FXML private TextArea taMethodology;
    @FXML private TextArea taResults;
    @FXML private TextArea taObservations;
    @FXML private TextField txtActivityName;
    @FXML private TextArea taActivityDescription;
    @FXML private ComboBox<Integer> cmbWeek;
    @FXML private TextField txtPlannedTime;
    @FXML private TextField txtRealTime;
    @FXML private ListView<PartialReportActivityDTO> lvActivities;
    @FXML private Button btnGenerate;
    private ObservableList<PartialReportActivityDTO> activitiesObservableList;
    private PartialReportDTO currentPartialReport;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateWeekComboBox();
        setUpActivitiesList();
        loadReportHeader();
        loadActiveTerm();
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtReportNumber,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY);
        InputFilter.applyFormatFilter(txtTerm,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_TERM);
        InputFilter.applyFormatFilter(taObjective,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
        InputFilter.applyFormatFilter(taMethodology,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
        InputFilter.applyFormatFilter(taResults,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
        InputFilter.applyFormatFilter(taObservations,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
        InputFilter.applyFormatFilter(txtActivityName,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_INTERN_ACTIVITY_TITLE);
        InputFilter.applyFormatFilter(taActivityDescription,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_DESCRIPTION);
        InputFilter.applyFormatFilter(txtPlannedTime,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY);
        InputFilter.applyFormatFilter(txtRealTime,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_CAPACITY);

    }

    private void loadActiveTerm() {
        String activeTerm = ActiveSessionDTO.get().getActiveTerm();
        txtTerm.setText(activeTerm);
    }

    private void populateWeekComboBox() {
        int total_weeks = 8;
        ObservableList<Integer> weekNumbers = FXCollections.observableArrayList();
        for (int weekNumber = 1; weekNumber <= total_weeks; weekNumber++) {
            weekNumbers.add(weekNumber);
        }
        cmbWeek.setItems(weekNumbers);
    }

    private void setUpActivitiesList() {
        activitiesObservableList = FXCollections.observableArrayList();
        lvActivities.setItems(activitiesObservableList);
    }

    private void loadReportHeader() {
        try {
            String activeEmail = ActiveSessionDTO.get().getEmail();
            InternDAO internDAO = new InternDAO();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(activeEmail);

            PartialReportDAO partialReportDAO = new PartialReportDAO();
            currentPartialReport = partialReportDAO.findReportHeaderByStudentNumber(studentNumber);

            if (currentPartialReport == null) {
                StatusLabel.showError(lblStatus, "No cuentas con un proyecto u organización asignada.");
                btnGenerate.setDisable(true);
            } else {
                showHeaderData();
            }
        } catch (DAOException daoException) {
            StatusLabel.showError(lblStatus, daoException.getMessage());
        }
    }

    private void showHeaderData() {
        lblStudentName.setText(currentPartialReport.getStudentName());
        lblStudentNumber.setText(currentPartialReport.getStudentNumber());
        lblProjectName.setText(currentPartialReport.getProjectName());
        lblLinkedOrganization.setText(currentPartialReport.getLinkedOrganization());
    }

    @FXML
    public void addActivity(ActionEvent event) {
        if (isActivityFormInvalid()) {
            StatusLabel.showError(lblStatus, "Complete los datos de la actividad.");
            return;
        }
        PartialReportActivityDTO activity = buildActivityFromForm();
        activitiesObservableList.add(activity);
        clearActivityForm();
        StatusLabel.showSuccess(lblStatus, "Actividad agregada.");
    }

    private boolean isActivityFormInvalid() {
        String activityName = txtActivityName.getText().trim();
        String activityDescription = taActivityDescription.getText().trim();
        Integer selectedWeek = cmbWeek.getValue();
        String plannedTime = txtPlannedTime.getText().trim();
        String realTime = txtRealTime.getText().trim();
        return activityName.isEmpty() || activityDescription.isEmpty() || selectedWeek == null
                || plannedTime.isEmpty() || realTime.isEmpty();
    }

    private PartialReportActivityDTO buildActivityFromForm() {
        PartialReportActivityDTO activity = new PartialReportActivityDTO();
        activity.setName(txtActivityName.getText().trim());
        activity.setDescription(taActivityDescription.getText().trim());
        activity.setWeekNumber(cmbWeek.getValue());
        activity.setPlannedTime(txtPlannedTime.getText().trim());
        activity.setRealTime(txtRealTime.getText().trim());
        return activity;
    }

    private void clearActivityForm() {
        txtActivityName.clear();
        taActivityDescription.clear();
        cmbWeek.setValue(null);
        txtPlannedTime.clear();
        txtRealTime.clear();
    }

    @FXML
    public void generatePartialReport(ActionEvent event) {
        if (activitiesObservableList.isEmpty()) {
            StatusLabel.showError(lblStatus, "Agregue al menos una actividad.");
            return;
        }
        try {
            PartialReportDTO partialReport = buildPartialReportDTO();
            String htmlContent = PartialReportHtmlBuilder.buildPartialReport(partialReport);

            File outputFile = chooseOutputFile(event);
            if (outputFile != null) {
                HtmlToPdfConverter.convertToFile(htmlContent, outputFile);
                StatusLabel.showSuccess(lblStatus, "Informe parcial generado correctamente.");
            }
        } catch (FileGenerationException fileGenerationException) {
            StatusLabel.showError(lblStatus, fileGenerationException.getMessage());
        }
    }

    private PartialReportDTO buildPartialReportDTO() {
        String career = "Licenciatura en Ingeniería de Software";
        currentPartialReport.setCareer(career);
        DateTimeFormatter date_format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        currentPartialReport.setReportDate(LocalDate.now().format(date_format));
        currentPartialReport.setReportNumber(txtReportNumber.getText().trim());
        currentPartialReport.setReportPeriod(txtTerm.getText().trim());
        currentPartialReport.setObjective(taObjective.getText().trim());
        currentPartialReport.setMethodology(taMethodology.getText().trim());
        currentPartialReport.setResults(taResults.getText().trim());
        currentPartialReport.setObservations(taObservations.getText().trim());

        Collections.sort(activitiesObservableList);
        currentPartialReport.setActivities(activitiesObservableList);
        return currentPartialReport;
    }

    private File chooseOutputFile(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Scene currentScene = sourceNode.getScene();
        Window currentWindow = currentScene.getWindow();
        return FileChooserHelper.chooseOutputFile(currentWindow, DocumentType.PARTIAL_REPORT, AllowedExtension.PDF);
    }

    @FXML
    public void goToInternMenuView(ActionEvent event) {
        boolean userConfirmed = AlertHelper.showConfirmation("Regresar",
                "La información no se guarda y se perderá el progreso. ¿Desea regresar?");
        if (userConfirmed) {
            ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                    "Menú Practicante", event);
        }
    }
}
