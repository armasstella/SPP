package spp.utils.view.window;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.io.IOException;
import java.net.URL;

public class ViewNavigator {

    public static <T> T loadView(String fxmlPath, String title, ActionEvent event) {
        T controllerToReturn = null;

        try {
            URL resourceUrl = ViewNavigator.class.getResource(fxmlPath);
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();

            Object eventSource = event.getSource();
            Node sourceNode = (Node) eventSource;
            Scene currentScene = sourceNode.getScene();
            Window currentWindow = currentScene.getWindow();
            Stage stage = (Stage) currentWindow;

            if (currentScene != null) {
                currentScene.setRoot(root);
            } else {
                Scene newScene = new Scene(root);
                stage.setScene(newScene);
            }

            stage.setTitle(title);
            stage.show();

            controllerToReturn = loader.getController();

        } catch (IOException exception) {
            AppLogger.log(ExceptionLevel.FATAL, exception);
        }

        return controllerToReturn;
    }

    public static <T> T loadView(String fxmlPath, String title, Stage stage) {
        T controllerToReturn = null;

        try {
            URL resourceUrl = ViewNavigator.class.getResource(fxmlPath);
            FXMLLoader loader = new FXMLLoader(resourceUrl);
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            controllerToReturn = loader.getController();

        } catch (IOException exception) {
            AppLogger.log(ExceptionLevel.FATAL, exception);
        }

        return controllerToReturn;
    }
}