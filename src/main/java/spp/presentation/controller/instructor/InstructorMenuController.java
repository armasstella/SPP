package spp.presentation.controller.instructor;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.presentation.controller.user.MessageCenterController;
import spp.utils.view.ViewNavigator;


public class InstructorMenuController {

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);

    }

    @FXML
    private void goToMessageCenter(ActionEvent event) {
        MessageCenterController messageCenterController = ViewNavigator.loadView(
                "/spp/presentation/view/user/MessageCenterView.fxml",
                "Centro de mensajes", event);

        if (messageCenterController != null) {
            messageCenterController.setPreviousView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                    "Menú Profesor");
        }

    }

}
