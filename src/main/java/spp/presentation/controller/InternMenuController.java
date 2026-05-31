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

    @FXML
    private void goToUploadDocumentsView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/UploadDocumentsView.fxml",
                "Subir documentos", event);

    }

    @FXML
    private void goToMessageCenter(ActionEvent event) {
        MessageCenterController messageCenterController = ViewNavigator.loadView(
                "/spp/presentation/view/MessageCenterView.fxml",
                "Centro de mensajes", event);

        if (messageCenterController != null) {
            messageCenterController.setPreviousView("/spp/presentation/view/InternMenuView.fxml",
                    "Menú Practicante");
        }

    }

}
