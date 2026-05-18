package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.ViewNavigator;

public class InstructorMenuController {

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);
    }

    @FXML
    private void goToNewActivityView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewActivityView.fxml",
                "Nueva actividad" , event);
    }

    @FXML
    private void goToMessageCenter(ActionEvent event) {

        MessageCenterController messageCenterController = ViewNavigator.loadView(
                "/spp/presentation/view/MessageCenterView.fxml",
                "Centro de mensajes", event);

        if (messageCenterController != null) {
            messageCenterController.setPreviousView("/spp/presentation/view/InstructorMenuView.fxml",
                    "Menú Profesor");
        }

    }

}
