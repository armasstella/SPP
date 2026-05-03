package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}

    @FXML
    private void goToAddCoordinatorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewCoordinatorView.fxml",
                "Registrar Coordinador", event);
    }

    @FXML
    private void goToInactivateCoordinatorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorDeactivationView.fxml",
                "Inactivar Coordinador", event);
    }

    @FXML
    private void goToAddInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewInstructorView.fxml",
                "Registrar Profesor", event);
    }

    @FXML
    private void goToInactivateInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/InstructorDeactivationView.fxml",
                "Inactivar Profesor", event);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);
    }
}