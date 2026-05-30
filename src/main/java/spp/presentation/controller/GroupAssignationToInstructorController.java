package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.ViewNavigator;

public class GroupAssignationToInstructorController {

    @FXML
    private void cancel (ActionEvent event) {
        ViewNavigator.loadView(
                "/spp/presentation/view/CoordinatorMenuView.fxml", "Menú Coordinador", event);
    }

}
