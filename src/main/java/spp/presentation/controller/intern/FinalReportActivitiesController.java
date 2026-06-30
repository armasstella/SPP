package spp.presentation.controller.intern;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.intern.listener.FinalActivityInclusionListener;
import spp.presentation.controller.intern.listener.IncludeFinalActivityModificationListener;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.table.TableViewConfigurator;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FinalReportActivitiesController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private Label lblActivitiesCounter;
    @FXML private TableView<ActivityDTO> tblActivities;
    @FXML private TableColumn<ActivityDTO, String> colActivityTitle;
    @FXML private TableColumn<ActivityDTO, String> colActivityDescription;
    @FXML private TableColumn<ActivityDTO, String> colActivityStartDate;
    @FXML private TableColumn<ActivityDTO, String> colActivityEndDate;
    @FXML private TableColumn<ActivityDTO, String> colActivityEstimatedTime;
    @FXML private TableColumn<ActivityDTO, String> colActivityEffectiveTime;
    @FXML private TableColumn<ActivityDTO, String> colActivityProgress;
    @FXML private TableColumn<ActivityDTO, String> colActivityObservations;
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
    private ObservableList<ActivityDTO> availableActivitiesObservableList;
    private ObservableList<ActivityDTO> includedActivitiesObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpActivityTableColumns();
        setUpIncludedActivities();
        obtainActivities();
        setUpActivitiesTablesClicks();
        updateActivityCounter();
    }

    public List<ActivityDTO> getIncludedActivities() {
        return new ArrayList<>(includedActivitiesObservableList);
    }

    public void clearIncludedActivities() throws DAOException {
        for (ActivityDTO activity : includedActivitiesObservableList) {
            int activityId = activity.getId();
            activityDAO.deleteActivity(activityId);
        }
        includedActivitiesObservableList.clear();
        updateActivityCounter();
    }

    public void setStatusLabel(Label sharedStatusLabel) {
        this.lblStatus = sharedStatusLabel;
    }

    private void setUpActivityTableColumns() {
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

        colActivityTitle.setCellValueFactory(titleSelector);
        colActivityDescription.setCellValueFactory(descriptionSelector);
        colActivityStartDate.setCellValueFactory(startDateSelector);
        colActivityEndDate.setCellValueFactory(endDateSelector);
        colActivityEstimatedTime.setCellValueFactory(estimatedTimeSelector);
        colActivityEffectiveTime.setCellValueFactory(effectiveTimeSelector);
        colActivityProgress.setCellValueFactory(progressSelector);
        colActivityObservations.setCellValueFactory(observationsSelector);

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
        InternDAO internDAO = new InternDAO();
        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);
            List<ActivityDTO> activityList = activityDAO.findFinalActivitiesByStudentNumber(studentNumber);

            availableActivitiesObservableList = FXCollections.observableArrayList(activityList);
            tblActivities.setItems(availableActivitiesObservableList);

        } catch (DAOException exception) {
            StatusLabel.showError(lblStatus, exception.getMessage());
        }
    }

    private void setUpActivitiesTablesClicks() {
        FinalActivityInclusionListener finalActivityInclusionListener = new FinalActivityInclusionListener(this);
        IncludeFinalActivityModificationListener includeFinalActivityModificationListener = new IncludeFinalActivityModificationListener(this);

        TableViewConfigurator.enableDoubleClickSelection(tblActivities, finalActivityInclusionListener);
        TableViewConfigurator.enableDoubleClickSelection(tblIncludedActivities, includeFinalActivityModificationListener);
    }

    public void includeActivity(ActivityDTO activity) {
        availableActivitiesObservableList.remove(activity);
        includedActivitiesObservableList.add(activity);
        updateActivityCounter();
    }

    private void updateActivityCounter() {
        int activitiesCount = includedActivitiesObservableList.size();
        String counterText = activitiesCount + " actividades incluidas.";
        lblActivitiesCounter.setText(counterText);
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
        updateActivityCounter();
    }

    private void openActivityEdit(ActivityDTO activity) {
        try {
            URL fxmlResource = getClass().getResource("/spp/presentation/view/intern/ActivityEditionView.fxml");
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

            boolean isUpdated = editController.isUpdated();

            if (isUpdated) {
                tblIncludedActivities.refresh();
                StatusLabel.showSuccess(lblStatus, "Actividad actualizada correctamente.");
            }
        } catch (IOException exception) {
            StatusLabel.showError(lblStatus, "Error al abrir la edición de la actividad");
        }
    }



}