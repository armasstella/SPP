package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.ViewNavigator;


public class ActivityRegistrationController {

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                "Menú Practicante", event);
    }

    @FXML
    private void saveActivity(ActionEvent event) {

    }


}
