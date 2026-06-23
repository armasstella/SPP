package spp.presentation.controller.coordinator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.IndicatorReportDAO;
import spp.businesslogic.dao.TermDAO;
import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.dto.IndicatorReportDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.enums.GenderFilter;
import spp.businesslogic.enums.YesNoAllFilter;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.file.HtmlToPdfConverter;
import spp.utils.htmlbuilder.IndicatorReportHtmlBuilder;
import spp.utils.logger.AppLogger;
import spp.utils.view.FileChooserUtil;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class IndicatorReportController implements Initializable {

    @FXML private ComboBox<GenderFilter> cmbFilterGender;
    @FXML private ComboBox<YesNoAllFilter> cmbFilterLanguage;
    @FXML private ComboBox<String> cmbFilterPeriod;
    @FXML private TextField txtFilterMinAge;
    @FXML private TextField txtFilterMaxAge;
    @FXML private Label lblStatus;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeComboBoxes();
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFilter(txtFilterMinAge, InputFilter.NUMERIC_PATTERN, 2);
        InputFilter.applyFilter(txtFilterMaxAge, InputFilter.NUMERIC_PATTERN, 2);
    }

    private void initializeComboBoxes() {
        for (GenderFilter filter : GenderFilter.values()) {
            cmbFilterGender.getItems().add(filter);
        }
        cmbFilterGender.getSelectionModel().selectFirst();

        for (YesNoAllFilter filter : YesNoAllFilter.values()) {
            cmbFilterLanguage.getItems().add(filter);
        }
        cmbFilterLanguage.getSelectionModel().selectFirst();
        TermDAO termDAO = new TermDAO();

        try {
            List<String> periods = termDAO.findTermNames();
            cmbFilterPeriod.getItems().add("Todos");
            for (String period : periods) {
                cmbFilterPeriod.getItems().add(period);
            }
            cmbFilterPeriod.getSelectionModel().selectFirst();

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "No se pudieron cargar los periodos.");
            cmbFilterPeriod.getItems().add("Todos");
            cmbFilterPeriod.getSelectionModel().selectFirst();
        }

    }

    @FXML
    public void generateReport(ActionEvent event) {
        IndicatorFilterDTO filters = buildIndicatorFilterDTO();
        IndicatorReportDAO indicatorDAO = new IndicatorReportDAO();

        try {
            IndicatorReportDTO reportData = indicatorDAO.getStaticsByIndicators(filters);
            reportData.setGenerationDate(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            String html = IndicatorReportHtmlBuilder.buildIndicatorReport(reportData);

            File outputFile = FileChooserUtil.chooseOutputFile(event, DocumentType.INDICATOR_REPORT);
            if (outputFile == null) {
                return;
            }

            HtmlToPdfConverter.convertToFile(html, outputFile);
            StatusLabel.showSuccess(lblStatus, "El reporte de indicadores ha sido generado exitosamente.");

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "No se pudo acceder a la base de datos. Intente más tarde.");

        } catch (IOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, "No se pudo generar el reporte en formato PDF. Intente nuevamente.");
        }

    }

    private IndicatorFilterDTO buildIndicatorFilterDTO() {
        IndicatorFilterDTO filters = new IndicatorFilterDTO();
        filters.setGender(cmbFilterGender.getValue());
        filters.setIndigenousLanguage(cmbFilterLanguage.getValue());
        filters.setPeriod(cmbFilterPeriod.getValue());
        String minAgeStr = txtFilterMinAge.getText().trim();
        if (!minAgeStr.isEmpty()) {
            filters.setMinAge(Integer.parseInt(minAgeStr));
        }
        String maxAgeStr = txtFilterMaxAge.getText().trim();
        if (!maxAgeStr.isEmpty()) {
            filters.setMaxAge(Integer.parseInt(maxAgeStr));
        }

        return filters;

    }

    @FXML
    public void goToCoordinatorMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);

    }

}
