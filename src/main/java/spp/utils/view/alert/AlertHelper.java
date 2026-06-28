package spp.utils.view.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import java.util.Optional;

public class AlertHelper {

    public enum Option { FIRST, SECOND, NONE }

    private static final AlertStyleManager styleManager = new AlertStyleManager();
    private static final AlertFactory alertFactory = new AlertFactory(styleManager);

    public static boolean showConfirmation(String title, String message) {
        boolean isConfirmed = false;

        Alert confirmationAlert = alertFactory.createBaseAlert(
                Alert.AlertType.CONFIRMATION, title, message, ButtonType.YES, ButtonType.NO
        );

        styleManager.applyPrimaryStyleToButton(confirmationAlert.getDialogPane(), ButtonType.YES, "Sí, confirmar");
        styleManager.applySecondaryStyleToButton(confirmationAlert.getDialogPane(), ButtonType.NO, "Cancelar");

        Optional<ButtonType> userResult = confirmationAlert.showAndWait();

        if (userResult.isPresent() && userResult.get() == ButtonType.YES) {
            isConfirmed = true;
        }

        return isConfirmed;
    }

    public static void showErrorMessage(String title, String message) {
        Alert errorAlert = alertFactory.createBaseAlert(
                Alert.AlertType.ERROR, title, message, ButtonType.OK
        );

        styleManager.applyPrimaryStyleToButton(errorAlert.getDialogPane(), ButtonType.OK, "Aceptar");
        errorAlert.showAndWait();
    }

    public static void showMessage(String title, String message) {
        Alert informationAlert = alertFactory.createBaseAlert(
                Alert.AlertType.INFORMATION, title, message, ButtonType.OK
        );

        styleManager.applyPrimaryStyleToButton(informationAlert.getDialogPane(), ButtonType.OK, "Aceptar");
        informationAlert.showAndWait();
    }

    public static Option showTwoOptions(String title, String message, String firstLabel, String secondLabel) {
        Option selectedOption = Option.NONE;

        ButtonType firstOptionButton = new ButtonType(firstLabel);
        ButtonType secondOptionButton = new ButtonType(secondLabel);

        Alert customOptionsAlert = alertFactory.createBaseAlert(
                Alert.AlertType.CONFIRMATION, title, message, firstOptionButton, secondOptionButton
        );

        styleManager.applyPrimaryStyleToButton(customOptionsAlert.getDialogPane(), firstOptionButton);
        styleManager.applySecondaryStyleToButton(customOptionsAlert.getDialogPane(), secondOptionButton);

        Optional<ButtonType> userResult = customOptionsAlert.showAndWait();

        if (userResult.isPresent()) {
            if (userResult.get() == firstOptionButton) {
                selectedOption = Option.FIRST;
            } else if (userResult.get() == secondOptionButton) {
                selectedOption = Option.SECOND;
            }
        }

        return selectedOption;
    }
}