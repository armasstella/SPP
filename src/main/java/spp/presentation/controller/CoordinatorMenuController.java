package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.ViewNavigator;

public class CoordinatorMenuController {

    @FXML
    private void goToAddInternView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewInternView.fxml",
                "Registrar Practicante", event);
    }

    @FXML
    private void goToInactivateInternView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/InactivateInternView.fxml",
                "Inactivar Practicante", event);
    }

    @FXML
    private void goToAddLinkedOrganizationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewLinkedOrganizationView.fxml",
                "Registrar Organización", event);
    }

    @FXML
    private void goToAddProjectManagerView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewProjectManagerView.fxml",
                "Registrar Encargado de Proyecto", event);
    }

    @FXML
    private void goToAddProjectView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewProjectView.fxml",
                "Registrar Proyecto", event);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Iniciar sesión", event);
    }
}
