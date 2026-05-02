package spp.utils.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import spp.utils.logger.AppLogger;
import java.io.IOException;

public class ViewNavigator {

    public static void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ViewNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            AppLogger.logError(e);
        }
    }
}