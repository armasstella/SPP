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
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import spp.businesslogic.dao.FinalReportDAO;
import spp.businesslogic.dao.ReportDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.utils.htmlbuilder.FinalReportHtmlBuilder;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.view.AlertHelper;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
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
        colTitle.setCellValueFactory(new GenericNestedSelector<>("title", "Sin título"));
        colDescription.setCellValueFactory(new GenericNestedSelector<>("description", "Sin descripción"));
        colStartDate.setCellValueFactory(new GenericNestedSelector<>("startDateText", ""));
        colEndDate.setCellValueFactory(new GenericNestedSelector<>("endDateText", ""));
        colEstimatedTime.setCellValueFactory(new GenericNestedSelector<>("estimatedTime", "0"));
        colEffectiveTime.setCellValueFactory(new GenericNestedSelector<>("effectiveTime", "0"));
        colProgress.setCellValueFactory(new GenericNestedSelector<>("progress", "0"));
        colObservations.setCellValueFactory(new GenericNestedSelector<>("observations", "Sin observaciones"));

        colChosenActivityTitle.setCellValueFactory(new GenericNestedSelector<>("title", "Sin título"));
        colChosenActivityDescription.setCellValueFactory(new GenericNestedSelector<>("description", "Sin descripción"));
        colChosenActivityStartDate.setCellValueFactory(new GenericNestedSelector<>("startDateText", ""));
        colChosenActivityEndDate.setCellValueFactory(new GenericNestedSelector<>("endDateText", ""));
        colChosenActivityEstimatedTime.setCellValueFactory(new GenericNestedSelector<>("estimatedTime", "0"));
        colChosenActivityEffectiveTime.setCellValueFactory(new GenericNestedSelector<>("effectiveTime", "0"));
        colChosenActivityProgress.setCellValueFactory(new GenericNestedSelector<>("progress", "0"));
        colChosenActivityObservations.setCellValueFactory(new GenericNestedSelector<>("observations", "Sin observaciones"));

    }

    private void setUpIncludedActivities() {
        includedActivitiesObservableList = FXCollections.observableArrayList();
        tblIncludedActivities.setItems(includedActivitiesObservableList);

    }

    private void obtainActivities() {
        try {
            String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
            List<ActivityDTO> activityList = activityDAO.findActivitiesByStudentNumber(studentNumber);
            availableActivitiesObservableList = FXCollections.observableArrayList(activityList);
            tblActivities.setItems(availableActivitiesObservableList);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al obtener actividades");
        }

    }

    private void setUpClicks() {
        tblActivities.setOnMouseClicked(event -> {
            ActivityDTO selectedActivity = tblActivities.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 1 && selectedActivity != null) {
                includeActivity(selectedActivity);
            }
        });
        tblIncludedActivities.setOnMouseClicked(event -> {
            ActivityDTO selectedActivity = tblIncludedActivities.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 1 && selectedActivity != null) {
                handleIncludedActivityClick(selectedActivity);
            }
        });

    }

    private void includeActivity(ActivityDTO activity) {
        availableActivitiesObservableList.remove(activity);
        includedActivitiesObservableList.add(activity);
        updateCounter();

    }

    private void handleIncludedActivityClick(ActivityDTO activity) {
        AlertHelper.Option choice = AlertHelper.showTwoOptions(
                "Actividad incluida",
                "¿Qué deseas hacer con esta actividad?",
                "Actualizarla",
                "Sacarla del reporte");

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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/spp/presentation/view/intern/ActivityEditView.fxml"));
            Parent editRoot = loader.load();
            ActivityEditController editController = loader.getController();
            editController.setActivity(activity);

            Stage editStage = new Stage();
            editStage.setTitle("Editar actividad");
            editStage.initModality(Modality.APPLICATION_MODAL);
            editStage.setScene(new Scene(editRoot));
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
        lblCounter.setText(includedActivitiesObservableList.size() + " actividades incluidas.");

    }

    @FXML
    private void generateReport(ActionEvent event) {
        if (includedActivitiesObservableList.isEmpty()) {
            StatusLabel.showError(lblStatus, "Incluye al menos una actividad.");
        }
        else {
            if (!AlertHelper.showConfirmation("Generar reporte",
                    "¿Seguro que desea generar el reporte con las actividades incluidas?")) {
            } else {
                ReportDAO reportDAO = new ReportDAO();
                String career = "Licenciatura en Ingeniería de Software";
                DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                String REPORT_TYPE = "MENSUAL";

                try {
                    String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
                    ReportDTO reportDTO = reportDAO.getReportDetailByStudentNumber(studentNumber);
                    reportDTO.setCareer(career);
                    reportDTO.setReportType(REPORT_TYPE);
                    reportDTO.setReportDate(LocalDate.now().format(DATE_FORMAT));
                    reportDTO.setTotalHours(String.valueOf(sumIncludedEffectiveTime()));

                    String html = FinalReportHtmlBuilder.buildFinalReport(reportDTO,
                            new ArrayList<>(includedActivitiesObservableList));

                    File outputFile = chooseOutputFile(event, studentNumber);
                    if (outputFile != null) {
                        HtmlToPdfConverter.convertToFile(html, outputFile);
                        deleteIncludedActivities();
                        AlertHelper.showMessage("Reporte generado",
                                "El reporte se generó y guardó correctamente.");
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, "Error al generar el reporte");
                } catch (IOException e) {
                    StatusLabel.showError(lblStatus, "Error al generar el PDF");
                }   
            }
        }
    }

    private void deleteIncludedActivities() throws DAOException {
        for (ActivityDTO activity : includedActivitiesObservableList) {
            activityDAO.deleteActivity(activity.getId());
        }
        includedActivitiesObservableList.clear();
        updateCounter();

    }

    private int sumIncludedEffectiveTime() {
        int totalEffectiveTime = 0;
        for (ActivityDTO activity : includedActivitiesObservableList) {
            totalEffectiveTime += activity.getEffectiveTime();
        }
        return totalEffectiveTime;

    }

    private File chooseOutputFile(ActionEvent event, String studentNumber) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar reporte");
        fileChooser.setInitialFileName("reporte_" + studentNumber + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        Window window = ((Node) event.getSource()).getScene().getWindow();
        return fileChooser.showSaveDialog(window);

    }

    @FXML
    private void goToInternMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/MonthlyActivityRegistersView.fxml",
                "Menú Practicante", event);

    }
}
