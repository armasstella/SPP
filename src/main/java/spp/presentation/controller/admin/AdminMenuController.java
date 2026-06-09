package spp.presentation.controller.admin;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.ViewNavigator;


public class AdminMenuController {

    @FXML
    private void goToAddCoordinatorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/admin/NewCoordinatorView.fxml",
                "Registrar Coordinador", event);

    }

    @FXML
    private void goToInactivateCoordinatorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/admin/CoordinatorDeactivationView.fxml",
                "Inactivar Coordinador", event);

    }

    @FXML
    private void goToAddInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/admin/NewInstructorView.fxml",
                "Registrar Profesor", event);

    }

    @FXML
    private void goToInactivateInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/admin/InstructorDeactivationView.fxml",
                "Inactivar Profesor", event);

    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);

    }

}