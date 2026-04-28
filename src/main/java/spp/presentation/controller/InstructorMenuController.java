package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import spp.utils.logger.AppLogger;

import java.io.IOException;

public class InstructorMenuController {

    @FXML
    private void goToLoginView(ActionEvent event) {
        loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        loadView(fxmlPath, title, event, null);
    }

    private void loadView(String fxmlPath, String title, ActionEvent event, CoordinatorController.ToggleMode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            if (mode != null) {
                CoordinatorController ctrl = loader.getController();
                ctrl.setToggleMode(mode);
            }

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 420, 380));
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            AppLogger.logError(e);
        }

    }
}
