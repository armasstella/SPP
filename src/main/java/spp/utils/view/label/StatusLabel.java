package spp.utils.view.label;


import javafx.animation.PauseTransition;
import javafx.scene.control.Label;
import javafx.util.Duration;


public final class StatusLabel {

    private static final String STYLE_SUCCESS = "success";
    private static final String STYLE_ERROR = "error";
    private static final String PAUSE_KEY = "CURRENT_PAUSE_TRANSITION";
    private static final int SECONDS = 5;

    private StatusLabel() {
    }

    public static void showSuccess(Label label, String message) {
        setStatus(label, message, STYLE_SUCCESS);
        hideLabelAfterDelay(label, SECONDS);
    }

    public static void showError(Label label, String message) {
        setStatus(label, message, STYLE_ERROR);
        hideLabelAfterDelay(label, SECONDS);
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

    public static void hideLabelAfterDelay(Label label, int seconds) {
        if (label == null) return;

        PauseTransition oldPause = (PauseTransition) label.getProperties().get(PAUSE_KEY);
        if (oldPause != null) {
            oldPause.stop();
        }

        PauseTransition newPause = new PauseTransition(Duration.seconds(seconds));
        newPause.setOnFinished(new LabelCloserHandler(label));

        label.getProperties().put(PAUSE_KEY, newPause);
        newPause.play();
    }
}