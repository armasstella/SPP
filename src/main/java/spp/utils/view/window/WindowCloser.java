package spp.utils.view.window;

import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;

public class WindowCloser {

    private WindowCloser() {
    }

    public static void closeWindowFromEvent(Event event) {
        if (event != null) {
            Object eventSource = event.getSource();
            boolean isSourceNode = eventSource instanceof Node;

            if (isSourceNode) {
                Node sourceNode = (Node) eventSource;
                Scene currentScene = sourceNode.getScene();

                boolean hasValidScene = currentScene != null;

                if (hasValidScene) {
                    Window currentWindow = currentScene.getWindow();
                    boolean isStage = currentWindow instanceof Stage;

                    if (isStage) {
                        Stage currentStage = (Stage) currentWindow;
                        currentStage.close();
                    }
                }
            }
        }
    }

}
