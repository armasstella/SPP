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
import spp.businesslogic.dao.DeliverableProductDAO;
import spp.businesslogic.dao.ReportDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ActivityDAO;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.presentation.controller.intern.listener.DeliverableProductInclusionListener;
import spp.presentation.controller.intern.listener.FinalActivityInclusionListener;
import spp.presentation.controller.intern.listener.IncludeDeliverableProductModificationListener;
import spp.presentation.controller.intern.listener.IncludeFinalActivityModificationListener;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.htmlbuilder.FinalReportHtmlBuilder;
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

public class FinalReportGenerationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private Label lblActivitiesCounter;
    @FXML private Label lblProductsCounter;

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

    @FXML private TableView<DeliverableProductDTO> tblDeliverableProducts;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductName;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductDescription;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductProgress;
    @FXML private TableColumn<DeliverableProductDTO, String> colDeliverableProductObservations;

    @FXML private TableView<DeliverableProductDTO> tblIncludedDeliverableProducts;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductName;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductDescription;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductProgress;
    @FXML private TableColumn<DeliverableProductDTO, String> colChosenDeliverableProductObservations;

    private final ActivityDAO activityDAO = new ActivityDAO();
    private final DeliverableProductDAO deliverableProductDAO = new DeliverableProductDAO();
    private final InternDAO internDAO = new InternDAO();

    private ObservableList<ActivityDTO> availableActivitiesObservableList;
    private ObservableList<ActivityDTO> includedActivitiesObservableList;
    private ObservableList<DeliverableProductDTO> availableDeliverableProductObservableList;
    private ObservableList<DeliverableProductDTO> includedDeliverableProductObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpActivityTableColumns();
        setUpDeliverableProductTableColumns();

        setUpIncludedActivities();
        setUpIncludedDeliverableProducts();

        obtainActivities();
        obtainDeliverableProducts();

        setUpActivitiesTablesClicks();
        setUpDeliverableProductsTablesClicks();

        updateActivityCounter();
        updateDeliverableProductCounter();
    }

    private void setUpDeliverableProductTableColumns() {
        GenericNestedSelector<DeliverableProductDTO> nameSelector = new GenericNestedSelector<>("name", "Sin nombre");
        GenericNestedSelector<DeliverableProductDTO> descriptionSelector = new GenericNestedSelector<>("description", "Sin descripcion");
        GenericNestedSelector<DeliverableProductDTO> progressSelector = new GenericNestedSelector<>("progress", "Sin avance");
        GenericNestedSelector<DeliverableProductDTO> observationsSelector = new GenericNestedSelector<>("observations", "Sin observaciones");

        colDeliverableProductName.setCellValueFactory(nameSelector);
        colDeliverableProductDescription.setCellValueFactory(descriptionSelector);
        colDeliverableProductProgress.setCellValueFactory(progressSelector);
        colDeliverableProductObservations.setCellValueFactory(observationsSelector);

        colChosenDeliverableProductName.setCellValueFactory(nameSelector);
        colChosenDeliverableProductDescription.setCellValueFactory(descriptionSelector);
        colChosenDeliverableProductProgress.setCellValueFactory(progressSelector);
        colChosenDeliverableProductObservations.setCellValueFactory(observationsSelector);
    }

    private void setUpActivityTableColumns() {
        GenericNestedSelector<ActivityDTO> titleSelector = new GenericNestedSelector<>("title", "Sin título");
        GenericNestedSelector<ActivityDTO> descriptionSelector = new GenericNestedSelector<>("description", "Sin descripción");
        GenericNestedSelector<ActivityDTO> startDateSelector = new GenericNestedSelector<>("startDateText", "");
        GenericNestedSelector<ActivityDTO> endDateSelector = new GenericNestedSelector<>("endDateText", "");
        GenericNestedSelector<ActivityDTO> estimatedTimeSelector = new GenericNestedSelector<>("estimatedTime", "0");
        GenericNestedSelector<ActivityDTO> effectiveTimeSelector = new GenericNestedSelector<>("effectiveTime", "0");
        GenericNestedSelector<ActivityDTO> progressSelector = new GenericNestedSelector<>("progress", "0");
        GenericNestedSelector<ActivityDTO> observationsSelector = new GenericNestedSelector<>("observations", "Sin observaciones");

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

    private void setUpIncludedDeliverableProducts() {
        includedDeliverableProductObservableList = FXCollections.observableArrayList();
        tblIncludedDeliverableProducts.setItems(includedDeliverableProductObservableList);
    }

    private void obtainActivities() {
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

    private void obtainDeliverableProducts() {
        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

            List<DeliverableProductDTO> deliverableProductList = deliverableProductDAO.findDeliverableProductsByStudentNumber(studentNumber);
            availableDeliverableProductObservableList = FXCollections.observableArrayList(deliverableProductList);
            tblDeliverableProducts.setItems(availableDeliverableProductObservableList);

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

    private void setUpDeliverableProductsTablesClicks() {
        DeliverableProductInclusionListener deliverableProductInclusionListener = new DeliverableProductInclusionListener(this);
        IncludeDeliverableProductModificationListener includeDeliverableProductModificationListener = new IncludeDeliverableProductModificationListener(this);

        TableViewConfigurator.enableDoubleClickSelection(tblDeliverableProducts, deliverableProductInclusionListener);
        TableViewConfigurator.enableDoubleClickSelection(tblIncludedDeliverableProducts, includeDeliverableProductModificationListener);
    }

    public void includeDeliverableProduct(DeliverableProductDTO deliverableProduct) {
        availableDeliverableProductObservableList.remove(deliverableProduct);
        includedDeliverableProductObservableList.add(deliverableProduct);
        updateDeliverableProductCounter();
    }

    public void processProductModificationAction(DeliverableProductDTO deliverableProduct) {
        boolean choice = AlertHelper.showConfirmation("Producto elegido", "¿Desea sacar este producto entregable?");

        if (choice) {
            excludeDeliverableProduct(deliverableProduct);
        }
    }

    private void excludeDeliverableProduct(DeliverableProductDTO deliverableProduct) {
        includedDeliverableProductObservableList.remove(deliverableProduct);
        availableDeliverableProductObservableList.add(deliverableProduct);
        // CORRECCIÓN: Actualizar el contador de productos, no de actividades
        updateDeliverableProductCounter();
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

    private void updateActivityCounter() {
        int activitiesCount = includedActivitiesObservableList.size();
        String counterText = activitiesCount + " actividades incluidas.";
        lblActivitiesCounter.setText(counterText);
    }

    private void updateDeliverableProductCounter() {
        int deliverableProductsCount = includedDeliverableProductObservableList.size();
        String counterText = deliverableProductsCount + " productos entregables incluidos.";
        lblProductsCounter.setText(counterText);
    }

    @FXML
    private void generateReport(ActionEvent event) {
        boolean isActivitiesListEmpty = includedActivitiesObservableList.isEmpty();
        boolean isDeliverableProductsListEmpty = includedDeliverableProductObservableList.isEmpty();

        if (isActivitiesListEmpty || isDeliverableProductsListEmpty) {
            StatusLabel.showError(lblStatus, "Incluye al menos una actividad y un producto entregable.");
        } else {
            boolean isConfirmed = AlertHelper.showConfirmation(
                    "Generar reporte",
                    "¿Seguro que desea generar el reporte con las actividades y productos entregables incluidos?"
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
        String reportType = "FINAL";

        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

            ReportDTO reportDTO = reportDAO.getReportDetailByStudentNumber(studentNumber);
            reportDTO.setCareer(career);
            reportDTO.setReportType(reportType);

            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.format(dateFormatter);
            reportDTO.setReportDate(formattedDate);

            List<ActivityDTO> activitiesList = new ArrayList<>(includedActivitiesObservableList);
            List<DeliverableProductDTO> deliverableProductList = new ArrayList<>(includedDeliverableProductObservableList);
            String generatedHtml = FinalReportHtmlBuilder.buildFinalReport(reportDTO, activitiesList, deliverableProductList);

            File outputFile = chooseOutputFileFromHelper(event, studentNumber);

            if (outputFile != null) {
                HtmlToPdfConverter.convertToFile(generatedHtml, outputFile);
                deleteIncludedActivities();
                deleteIncludedDeliverableProducts();
                AlertHelper.showMessage("Reporte generado", "El reporte se generó y guardó correctamente.");
            }
        } catch (DAOException | FileGenerationException exception) {
            StatusLabel.showError(lblStatus, exception.getMessage());
        }
    }

    private void deleteIncludedActivities() throws DAOException {
        for (ActivityDTO activity : includedActivitiesObservableList) {
            int activityId = activity.getId();
            activityDAO.deleteActivity(activityId);
        }
        includedActivitiesObservableList.clear();
        updateActivityCounter();
    }

    private void deleteIncludedDeliverableProducts() throws DAOException {
        for (DeliverableProductDTO deliverableProduct : includedDeliverableProductObservableList) {
            int deliverableProductId = deliverableProduct.getId();
            deliverableProductDAO.deleteDeliverableProduct(deliverableProductId);
        }
        includedDeliverableProductObservableList.clear();
        updateDeliverableProductCounter();
    }

    private File chooseOutputFileFromHelper(ActionEvent event, String studentNumber) {
        File selectedOutputFile = null;

        Object eventSource = event.getSource();
        Node sourceNode = (Node) eventSource;
        Scene currentScene = sourceNode.getScene();
        Window currentWindow = currentScene.getWindow();

        DocumentType reportType = DocumentType.FINAL_REPORT;
        AllowedExtension pdfExtension = AllowedExtension.PDF;

        selectedOutputFile = FileChooserHelper.chooseOutputFile(currentWindow, reportType, pdfExtension);

        return selectedOutputFile;
    }

    @FXML
    private void goToInternMenuView(ActionEvent event) {
        ViewNavigator.loadView(
                "/spp/presentation/view/intern/FinalReportMenu.fxml",
                "Menú de Reporte Final",
                event
        );
    }
}