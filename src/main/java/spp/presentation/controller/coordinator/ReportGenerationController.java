package spp.presentation.controller.coordinator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.window.ViewNavigator;


public class ReportGenerationController {

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);

    }

    @FXML
    private void generateReport(ActionEvent event) {

    }

}
