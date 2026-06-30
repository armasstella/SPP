package spp.presentation.controller.intern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Window;
import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.ReportDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.FileGenerationException;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.htmlbuilder.FinalReportHtmlBuilder;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.filechooser.AllowedExtension;
import spp.utils.view.filechooser.FileChooserHelper;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class FinalReportGenerationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private FinalReportActivitiesController activitiesSectionController;
    @FXML private FinalReportProductsController productsSectionController;
    private final ReportDAO reportDAO = new ReportDAO();
    private final InternDAO internDAO = new InternDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        activitiesSectionController.setStatusLabel(lblStatus);
        productsSectionController.setStatusLabel(lblStatus);
    }

    @FXML
    private void generateReport(ActionEvent event) {
        List<ActivityDTO> selectedActivities = activitiesSectionController.getIncludedActivities();
        List<DeliverableProductDTO> selectedProducts = productsSectionController.getIncludedProducts();

        boolean isActivitiesEmpty = selectedActivities.isEmpty();
        boolean isProductsEmpty = selectedProducts.isEmpty();

        if (isActivitiesEmpty || isProductsEmpty) {
            StatusLabel.showError(lblStatus, "Incluye al menos una actividad y un producto entregable.");
        } else {
            boolean isConfirmed = AlertHelper.showConfirmation(
                    "Generar reporte",
                    "¿Seguro que desea generar el reporte con los elementos incluidos?"
            );

            if (isConfirmed) {
                executeReportGeneration(event, selectedActivities, selectedProducts);
            }
        }
    }

    private void executeReportGeneration(ActionEvent event, List<ActivityDTO> activities,
                                         List<DeliverableProductDTO> products) {
        String career = "Licenciatura en Ingeniería de Software";
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String reportType = DocumentType.FINAL_REPORT.getValue();

        try {
            String userEmail = ActiveSessionDTO.get().getEmail();
            String studentNumber = internDAO.findActiveStudentNumberByEmail(userEmail);

            ReportDTO reportDTO = reportDAO.getReportDetailByStudentNumber(studentNumber);
            reportDTO.setCareer(career);
            reportDTO.setReportType(reportType);

            LocalDate currentDate = LocalDate.now();
            String formattedDate = currentDate.format(dateFormatter);
            reportDTO.setReportDate(formattedDate);

            String generatedHtml = FinalReportHtmlBuilder.buildFinalReport(reportDTO, activities, products);
            File outputFile = chooseOutputFileFromHelper(event, studentNumber);

            if (outputFile != null) {
                HtmlToPdfConverter.convertToFile(generatedHtml, outputFile);
                activitiesSectionController.clearIncludedActivities();
                productsSectionController.clearIncludedProducts();

                AlertHelper.showMessage(
                        "Reporte generado", "El reporte se generó y guardó correctamente.");
            }
        } catch (DAOException | FileGenerationException exception) {
            StatusLabel.showError(lblStatus, exception.getMessage());
        }
    }

    private File chooseOutputFileFromHelper(ActionEvent event, String studentNumber) {
        Object eventSource = event.getSource();
        Node sourceNode = (Node) eventSource;
        Scene currentScene = sourceNode.getScene();
        Window currentWindow = currentScene.getWindow();

        return FileChooserHelper.chooseOutputFile(
                currentWindow, DocumentType.FINAL_REPORT, AllowedExtension.PDF);
    }

    @FXML
    private void goToInternMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/FinalReportMenu.fxml",
                "Menú de Reporte Final", event);
    }
}