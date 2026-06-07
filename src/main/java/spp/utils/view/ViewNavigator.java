package spp.utils.view;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import spp.utils.logger.AppLogger;
import java.io.IOException;


public class ViewNavigator {

    public static <T> T loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene currentScene = stage.getScene();

            if (currentScene != null) {
                currentScene.setRoot(root);
            } else {
                stage.setScene(new Scene(root));
            }

            stage.setTitle(title);
            stage.show();

            return loader.getController();
        } catch (IOException e) {
            AppLogger.logError(e);
            return null;
        }
    }

}