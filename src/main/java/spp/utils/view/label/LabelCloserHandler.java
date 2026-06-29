package spp.utils.view.label;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;

public class LabelCloserHandler implements EventHandler<ActionEvent> {
    private final Label label;
    private static final String PAUSE_KEY = "PAUSE_KEY";

    public LabelCloserHandler(Label label) {
        this.label = label;
    }

    @Override
    public void handle(ActionEvent event) {
        if (label != null) {
            label.setText("");
            label.getProperties().remove(PAUSE_KEY);
        }
    }
}