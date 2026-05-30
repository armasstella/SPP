package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.utils.view.ViewNavigator;

public class CoordinatorMenuController {

    @FXML
    private void goToNewInternView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewInternView.fxml",
                "Registrar Practicante", event);
    }

    @FXML
    private void goToInternDeactivationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/InternDeactivationView.fxml",
                "Inactivar Practicante", event);
    }

    @FXML
    private void goToNewLinkedOrganizationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewLinkedOrganizationView.fxml",
                "Registrar Organización", event);
    }

    @FXML
    private void goToNewProjectManagerView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewProjectManagerView.fxml",
                "Registrar Encargado de Proyecto", event);
    }

    @FXML
    private void goToNewProjectView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewProjectView.fxml",
                "Registrar Proyecto", event);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Iniciar sesión", event);
    }

    @FXML
    private void goToProjectAssignmentView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/ProjectAssignmentView.fxml",
                "Asignar Proyecto", event);
    }

    @FXML
    private void goToProjectUpdateView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/ProjectUpdateView.fxml",
                "Actualizar Proyecto", event);
    }

    @FXML
    private void goToProjectDeletionView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/ProjectDeletionView.fxml",
                "Eliminar Proyecto", event);
    }

    @FXML
    private void goToReportGenerationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/ReportGenerationView.fxml",
                "Generar Reporte", event);
    }

    @FXML
    private void goToGroupAssignationToInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/GroupAssignationToInstructorView.fxml",
                "Asignar grupo", event);
    }

    @FXML
    private void goToCourseInformationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CourseInformationView.fxml",
                "Cursos", event);
    }

    @FXML
    private void goToMessageCenter(ActionEvent event) {
        MessageCenterController messageCenterController = ViewNavigator.loadView(
                "/spp/presentation/view/MessageCenterView.fxml",
                "Centro de mensajes", event);

        if (messageCenterController != null) {
            messageCenterController.setPreviousView("/spp/presentation/view/CoordinatorMenuView.fxml",
                    "Menú Coordinador");
        }

    }
}
