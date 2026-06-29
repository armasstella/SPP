package spp.utils.view.alert;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

public class AlertStyleManager {

    private static final String CSS_PATH = "/spp/presentation/css/MainStyle.css";
    private static final String CUSTOM_ALERT_CLASS = "custom-alert";
    private static final String BASE_BUTTON_CLASS = "button";
    private static final String PRIMARY_BUTTON_CLASS = "btn-primary";
    private static final String SECONDARY_BUTTON_CLASS = "btn-back";

    public void applyMainStylesheet(DialogPane dialogPane) {
        try {
            String cssExternalForm = getClass().getResource(CSS_PATH).toExternalForm();
            dialogPane.getStylesheets().add(cssExternalForm);
            dialogPane.getStyleClass().add(CUSTOM_ALERT_CLASS);
        } catch (Exception exception) {
            AppLogger.log(ExceptionLevel.WARN, exception);
        }
    }

    public void applyPrimaryStyleToButton(DialogPane dialogPane, ButtonType buttonType) {
        applyStyleToButton(dialogPane, buttonType, PRIMARY_BUTTON_CLASS);
    }

    public void applyPrimaryStyleToButton(DialogPane dialogPane, ButtonType buttonType, String customText) {
        Button button = applyStyleToButton(dialogPane, buttonType, PRIMARY_BUTTON_CLASS);
        if (button != null) {
            button.setText(customText);
        }
    }

    public void applySecondaryStyleToButton(DialogPane dialogPane, ButtonType buttonType) {
        applyStyleToButton(dialogPane, buttonType, SECONDARY_BUTTON_CLASS);
    }

    public void applySecondaryStyleToButton(DialogPane dialogPane, ButtonType buttonType, String customText) {
        Button button = applyStyleToButton(dialogPane, buttonType, SECONDARY_BUTTON_CLASS);
        if (button != null) {
            button.setText(customText);
        }
    }

    private Button applyStyleToButton(DialogPane dialogPane, ButtonType buttonType, String specificStyleClass) {
        Button targetButton = (Button) dialogPane.lookupButton(buttonType);
        if (targetButton != null) {
            targetButton.getStyleClass().clear();
            targetButton.getStyleClass().addAll(BASE_BUTTON_CLASS, specificStyleClass);
        }
        return targetButton;
    }
}