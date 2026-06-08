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


public class MonthlyReportGenerationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<ActivityDTO> tblActivities;
    @FXML private TableColumn<ActivityDTO, String> colTitle;
    @FXML private TableColumn<ActivityDTO, String> colDescription;
    @FXML private TableColumn<ActivityDTO, String> colStartDate;
    @FXML private TableColumn<ActivityDTO, String> colEndDate;
    @FXML private TableView<ActivityDTO> tblChosenActivities;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityTitle;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityDescription;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityStartDate;
    @FXML private TableColumn<ActivityDTO, String> colChosenActivityEndDate;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void goToInternMenuView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/InternMenuView.fxml",
                "Menú Practicante", event);
    }

    @FXML
    private void generateReport(ActionEvent event) {

    }

}
