package spp.utils.view;

import javafx.scene.control.Label;

public final class StatusLabel {

    private static final String STYLE_SUCCESS = "success";
    private static final String STYLE_ERROR = "error";

    private StatusLabel() {

    }

    public static void showSuccess(Label label, String message) {
        setStatus(label, message, STYLE_SUCCESS);

    }

    public static void showError(Label label, String message) {
        setStatus(label, message, STYLE_ERROR);

    }

    public static void clear(Label label) {
        if (label != null) {
            label.setText("");
            label.getStyleClass().removeAll(STYLE_ERROR, STYLE_SUCCESS);
        }

    }

    private static void setStatus(Label label, String message, String styleClass) {
        if (label != null) {
            label.setText(message);
            label.getStyleClass().removeAll(STYLE_ERROR, STYLE_SUCCESS);
            label.getStyleClass().add(styleClass);
        }

    }

}