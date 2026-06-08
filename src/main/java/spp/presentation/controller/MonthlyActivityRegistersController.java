package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dto.ActivityDTO;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;


public class MonthlyActivityRegistersController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<ActivityDTO> tblActivities;
    @FXML private TableColumn<ActivityDTO, String> colTitle;
    @FXML private TableColumn<ActivityDTO, String> colDescription;
    @FXML private TableColumn<ActivityDTO, String> colStartDate;
    @FXML private TableColumn<ActivityDTO, String> colEndDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void goBackToMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/InternenuView.fxml",
                "Menú Practicante", event);
    }

    @FXML
    private void goToActivityRegistrationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/ActivityRegistrationView.fxml",
                "Registro actividades", event);
    }

    @FXML
    private void goToMonthlyReportGenerationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/MonthlyReportGenerationView.fxml",
                "Generar reporte mensual", event);
    }
}
