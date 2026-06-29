package spp.presentation.controller.intern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import spp.businesslogic.dao.ReportDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.presentation.controller.intern.listener.ActivityInclusionListener;
import spp.presentation.controller.intern.listener.IncludedActivityModificationListener;
import spp.utils.htmlbuilder.FinalReportHtmlBuilder;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.view.filechooser.AllowedExtension;
import spp.utils.view.filechooser.FileChooserHelper;
import spp.utils.view.table.TableViewConfigurator;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MonthlyReportGenerationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private Label lblCounter;
    @FXML private TableView<ActivityDTO> tblActivities;
    @FXML private TableColumn<ActivityDTO, String> colTitle;
    @FXML private TableColumn<ActivityDTO, String> colDescription;
    @FXML private TableColumn<ActivityDTO, String> colStartDate;
    @FXML private TableColumn<ActivityDTO, String> colEndDate;
    @FXML private TableColumn<ActivityDTO, String> colEstimatedTime;
    @FXML private TableColumn<ActivityDTO, String> colEffectiveTime;
    @FXML private TableColumn<ActivityDTO, String> colProgress;
    @FXML private TableColumn<ActivityDTO, String> colObservations;
    @FXML private TableView<ActivityDTO> tblIncludedActivities;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityTitle;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityDescription;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityStartDate;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityEndDate;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityEstimatedTime;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityEffectiveTime;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityProgress;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityObservations;
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final InternDAO internDAO = new InternDAO();
    private ObservableList<ActivityDTO> availableActivitiesObservableList;
    private ObservableList<ActivityDTO> includedActivitiesObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        setUpIncludedActivities();
        obtainActivities();
        setUpClicks();
        updateCounter();
    }

    private void setUpColumns() {
        GenericNestedSelector<ActivityDTO> titleSelector =
                new GenericNestedSelector<>("title", "Sin título");
        GenericNestedSelector<ActivityDTO> descriptionSelector =
                new GenericNestedSelector<>("description", "Sin descripción");
        GenericNestedSelector<ActivityDTO> startDateSelector =
                new GenericNestedSelector<>("startDateText", "");
        GenericNestedSelector<ActivityDTO> endDateSelector =
                new GenericNestedSelector<>("endDateText", "");
        GenericNestedSelector<ActivityDTO> estimatedTimeSelector =
                new GenericNestedSelector<>("estimatedTime", "0");
        GenericNestedSelector<ActivityDTO> effectiveTimeSelector =
                new GenericNestedSelector<>("effectiveTime", "0");
        GenericNestedSelector<ActivityDTO> progressSelector =
                new GenericNestedSelector<>("progress", "0");
        GenericNestedSelector<ActivityDTO> observationsSelector =
                new GenericNestedSelector<>("observations", "Sin observaciones");

        colTitle.setCellValueFactory(titleSelector);
        colDescription.setCellValueFactory(descriptionSelector);
        colStartDate.setCellValueFactory(startDateSelector);
        colEndDate.setCellValueFactory(endDateSelector);
        colEstimatedTime.setCellValueFactory(estimatedTimeSelector);
        colEffectiveTime.setCellValueFactory(effectiveTimeSelector);
        colProgress.setCellValueFactory(progressSelector);
        colObservations.setCellValueFactory(observationsSelector);

        colChosenActivityTitle.setCellValueFactory(titleSelector);
        colChosenActivityDescription.setCellValueFactory(descriptionSelector);
        colChosenActivityStartDate.setCellValueFactory(startDateSelector);
        colChosenActivityEndDate.setCellValueFactory(endDateSelector);
        colChosenActivityEstimatedTime.setCellValueFactory(estimatedTimeSelector);
        colChosenActivityEffectiveTime.setCellValueFactory(effectiveTimeSelector);
        colChosenActivityProgress.setCellValueFactory(progressSelector);
        colChosenActivityObservations.setCellValueFactory(observationsSelector);
    }

    private void setUpIncludedActivities() {
        includedActivitiesObservableList = FXCollections.observableArrayList();
        tblIncludedActivities.setItems(includedActivitiesObservableList);
    }

    private void obtainActivities() {
        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

            List<ActivityDTO> activityList = activityDAO.findActivitiesByStudentNumber(studentNumber);
            availableActivitiesObservableList = FXCollections.observableArrayList(activityList);
            tblActivities.setItems(availableActivitiesObservableList);

        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void setUpClicks() {
        ActivityInclusionListener activityInclusionListener =
                new ActivityInclusionListener(this);
        IncludedActivityModificationListener includedActivityModificationListener =
                new IncludedActivityModificationListener(this);

        TableViewConfigurator.enableDoubleClickSelection(tblActivities, activityInclusionListener);
        TableViewConfigurator.enableDoubleClickSelection(tblIncludedActivities, includedActivityModificationListener);
    }

    public void includeActivity(ActivityDTO activity) {
        availableActivitiesObservableList.remove(activity);
        includedActivitiesObservableList.add(activity);
        updateCounter();
    }

    public void processActivityModificationAction(ActivityDTO activity) {
        AlertHelper.Option choice = AlertHelper.showTwoOptions(
                "Actividad incluida",
                "¿Qué deseas hacer con esta actividad?",
                "Actualizarla",
                "Sacarla del reporte"
        );

        if (choice == AlertHelper.Option.FIRST) {
            openActivityEdit(activity);
        } else if (choice == AlertHelper.Option.SECOND) {
            excludeActivity(activity);
        }
    }

    private void excludeActivity(ActivityDTO activity) {
        includedActivitiesObservableList.remove(activity);
        availableActivitiesObservableList.add(activity);
        updateCounter();
    }

    private void openActivityEdit(ActivityDTO activity) {
        try {
            URL fxmlResource = getClass().getResource("/spp/presentation/view/intern/ActivityEditView.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlResource);
            Parent editRoot = loader.load();

            ActivityEditController editController = loader.getController();
            editController.setActivity(activity);

            Stage editStage = new Stage();
            editStage.setTitle("Editar actividad");
            editStage.initModality(Modality.APPLICATION_MODAL);

            Scene editScene = new Scene(editRoot);
            editStage.setScene(editScene);
            editStage.showAndWait();

            if (editController.isUpdated()) {
                tblIncludedActivities.refresh();
                StatusLabel.showSuccess(lblStatus, "Actividad actualizada correctamente.");
            }
        } catch (IOException e) {
            StatusLabel.showError(lblStatus, "Error al abrir la edición de la actividad");
        }
    }

    private void updateCounter() {
        int activitiesCount = includedActivitiesObservableList.size();
        String counterText = activitiesCount + " actividades incluidas.";
        lblCounter.setText(counterText);
    }

    @FXML
    private void generateReport(ActionEvent event) {
        boolean isListEmpty = includedActivitiesObservableList.isEmpty();

        if (isListEmpty) {
            StatusLabel.showError(lblStatus, "Incluye al menos una actividad.");
        } else {
            boolean isConfirmed = AlertHelper.showConfirmation(
                    "Generar reporte",
                    "¿Seguro que desea generar el reporte con las actividades incluidas?"
            );

            if (isConfirmed) {
                executeReportGeneration(event);
            }
        }
    }

    private void executeReportGeneration(ActionEvent event) {
        ReportDAO reportDAO = new ReportDAO();
        String career = "Licenciatura en Ingeniería de Software";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String reportType = "MENSUAL";

        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

            ReportDTO reportDTO = reportDAO.getReportDetailByStudentNumber(studentNumber);
            reportDTO.setCareer(career);
            reportDTO.setReportType(reportType);

            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.format(dateFormatter);
            reportDTO.setReportDate(formattedDate);

            int effectiveTimeSum = sumIncludedEffectiveTime();
            reportDTO.setTotalHours(String.valueOf(effectiveTimeSum));

            List<ActivityDTO> activitiesList = new ArrayList<>(includedActivitiesObservableList);
            String generatedHtml = FinalReportHtmlBuilder.buildFinalReport(reportDTO, activitiesList);

            File outputFile = chooseOutputFileFromHelper(event, studentNumber);

            if (outputFile != null) {
                HtmlToPdfConverter.convertToFile(generatedHtml, outputFile);
                deleteIncludedActivities();
                AlertHelper.showMessage("Reporte generado", "El reporte se generó y guardó correctamente.");
            }
        } catch (DAOException | FileGenerationException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }
    }

    private void deleteIncludedActivities() throws DAOException {
        for (ActivityDTO activity : includedActivitiesObservableList) {
            int activityId = activity.getId();
            activityDAO.deleteActivity(activityId);
        }
        includedActivitiesObservableList.clear();
        updateCounter();
    }

    private int sumIncludedEffectiveTime() {
        int totalEffectiveTime = 0;

        for (ActivityDTO activity : includedActivitiesObservableList) {
            int currentEffectiveTime = activity.getEffectiveTime();
            totalEffectiveTime += currentEffectiveTime;
        }

        return totalEffectiveTime;
    }

    private File chooseOutputFileFromHelper(ActionEvent event, String studentNumber) {
        File selectedOutputFile = null;

        Node sourceNode = (Node) event.getSource();
        Scene currentScene = sourceNode.getScene();
        Window currentWindow = currentScene.getWindow();

        DocumentType reportType = DocumentType.MONTHLY_REPORT;
        AllowedExtension pdfExtension = AllowedExtension.PDF;

        selectedOutputFile = FileChooserHelper.chooseOutputFile(
                currentWindow,
                reportType,
                pdfExtension
        );

        return selectedOutputFile;
    }

    @FXML
    private void goToInternMenuView(ActionEvent event) {
        ViewNavigator.loadView(
                "/spp/presentation/view/intern/MonthlyActivityRegistersView.fxml",
                "Menú Practicante",
                event
        );
    }
}