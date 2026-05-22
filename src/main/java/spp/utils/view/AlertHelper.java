package spp.utils.view;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import spp.utils.logger.AppLogger;

import java.util.Optional;

public class AlertHelper {

    public static boolean showConfirmation(String title, String message) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle(title);
        confirm.setHeaderText(null);
        confirm.setContentText(message);

        confirm.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

        DialogPane dialogPane = confirm.getDialogPane();

        try {
            String cssPath = AlertHelper.class.getResource("/spp/presentation/css/MainStyle.css").toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            dialogPane.getStyleClass().add("custom-alert");
        } catch (Exception e) {
            AppLogger.logError(e);
        }

        Button btnYes = (Button) dialogPane.lookupButton(ButtonType.YES);
        if (btnYes != null) {
            btnYes.getStyleClass().clear();
            btnYes.getStyleClass().addAll("button", "btn-primary");
            btnYes.setText("Sí, confirmar");
        }

        Button btnNo = (Button) dialogPane.lookupButton(ButtonType.NO);
        if (btnNo != null) {
            btnNo.getStyleClass().clear();
            btnNo.getStyleClass().addAll("button", "btn-back");
            btnNo.setText("Cancelar");
        }

        Optional<ButtonType> result = confirm.showAndWait();
        return result.isPresent() && result.get() == ButtonType.YES;
    }
}