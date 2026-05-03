package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.ViewNavigator;

public class InternMenuController {

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);
    }

}
