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

public class CoordinatorMenuController {

    @FXML
    private void goToAddInternView(ActionEvent event) {
        loadView("/spp/presentation/view/NewInternView.fxml",
                "Registrar Practicante", event);
    }

    @FXML
    private void goToInactivateInternView(ActionEvent event) {
        loadView("/spp/presentation/view/InactivateInternView.fxml",
                "Inactivar Practicante", event);
    }

    @FXML
    private void goToAddLinkedOrganizationView(ActionEvent event) {
        loadView("/spp/presentation/view/NewLinkedOrganizationView.fxml",
                "Registrar Organización", event);
    }

    @FXML
    private void goToAddProjectManagerView(ActionEvent event) {
        loadView("/spp/presentation/view/NewProjectManagerView.fxml",
                "Registrar Encargado de Proyecto", event);
    }

    @FXML
    private void goToAddProjectView(ActionEvent event) {
        loadView("/spp/presentation/view/NewProjectView.fxml",
                "Registrar Proyecto", event);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        loadView("/spp/presentation/view/LoginView.fxml",
                "Iniciar sesión", event);
    }

    private void loadView(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
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
