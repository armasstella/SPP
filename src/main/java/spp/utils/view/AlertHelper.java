package spp.utils.view;


import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import spp.utils.logger.AppLogger;
import java.util.Optional;


public class AlertHelper {

    public enum Option { FIRST, SECOND, NONE }

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

    public static void showErrorMessage(String title, String message) {
        Alert confirm = new Alert(Alert.AlertType.ERROR);
        confirm.setTitle(title);
        confirm.setHeaderText(null);
        confirm.setContentText(message);
        confirm.getButtonTypes().setAll(ButtonType.OK);
        DialogPane dialogPane = confirm.getDialogPane();

        try {
            String cssPath = AlertHelper.class.getResource("/spp/presentation/css/MainStyle.css").toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            dialogPane.getStyleClass().add("custom-alert");
        } catch (Exception e) {
            AppLogger.logError(e);
        }

        Button btnYes = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (btnYes != null) {
            btnYes.getStyleClass().clear();
            btnYes.getStyleClass().addAll("button", "btn-primary");
            btnYes.setText("Aceptar");
        }

        confirm.showAndWait();

    }

    public static void showMessage(String title, String message) {
        Alert confirm = new Alert(Alert.AlertType.INFORMATION);
        confirm.setTitle(title);
        confirm.setHeaderText(null);
        confirm.setContentText(message);
        confirm.getButtonTypes().setAll(ButtonType.OK);
        DialogPane dialogPane = confirm.getDialogPane();

        try {
            String cssPath = AlertHelper.class.getResource("/spp/presentation/css/MainStyle.css").
                    toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            dialogPane.getStyleClass().add("custom-alert");
        } catch (Exception e) {
            AppLogger.logError(e);
        }

        Button btnYes = (Button) dialogPane.lookupButton(ButtonType.OK);
        if (btnYes != null) {
            btnYes.getStyleClass().clear();
            btnYes.getStyleClass().addAll("button", "btn-primary");
            btnYes.setText("Aceptar");
        }

        confirm.showAndWait();

    }

    public static Option showTwoOptions(String title, String message, String firstLabel, String secondLabel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        ButtonType firstButton = new ButtonType(firstLabel);
        ButtonType secondButton = new ButtonType(secondLabel);
        alert.getButtonTypes().setAll(firstButton, secondButton);
        DialogPane dialogPane = alert.getDialogPane();

        try {
            String cssPath = AlertHelper.class.getResource("/spp/presentation/css/MainStyle.css").toExternalForm();
            dialogPane.getStylesheets().add(cssPath);
            dialogPane.getStyleClass().add("custom-alert");
        } catch (Exception e) {
            AppLogger.logError(e);
        }

        Button firstStyledButton = (Button) dialogPane.lookupButton(firstButton);
        if (firstStyledButton != null) {
            firstStyledButton.getStyleClass().clear();
            firstStyledButton.getStyleClass().addAll("button", "btn-primary");
        }
        Button secondStyledButton = (Button) dialogPane.lookupButton(secondButton);
        if (secondStyledButton != null) {
            secondStyledButton.getStyleClass().clear();
            secondStyledButton.getStyleClass().addAll("button", "btn-back");
        }

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isEmpty()) {
            return Option.NONE;
        }
        if (result.get() == firstButton) {
            return Option.FIRST;
        }
        if (result.get() == secondButton) {
            return Option.SECOND;
        }
        return Option.NONE;
    }

}