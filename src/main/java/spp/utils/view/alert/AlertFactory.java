package spp.utils.view.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AlertFactory {

    private final AlertStyleManager styleManager;

    public AlertFactory(AlertStyleManager styleManager) {
        this.styleManager = styleManager;
    }

    public Alert createBaseAlert(Alert.AlertType alertType, String title, String message, ButtonType... buttonTypes) {
        Alert createdAlert = new Alert(alertType);
        createdAlert.setTitle(title);
        createdAlert.setHeaderText(null);
        createdAlert.setContentText(message);
        createdAlert.getButtonTypes().setAll(buttonTypes);

        styleManager.applyMainStylesheet(createdAlert.getDialogPane());

        return createdAlert;
    }
}