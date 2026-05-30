package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.exceptions.DAOException;
import spp.utils.view.AlertHelper;
import spp.utils.view.ViewNavigator;

public class CoordinatorMenuController {

    CourseDAO courseDAO = new CourseDAO();

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

        try {
            if (courseDAO.searchCourses()) {
                ViewNavigator.loadView("/spp/presentation/view/ReportGenerationView.fxml",
                        "Generar Reporte", event);
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("No puede realizar la operación",
                    "No hay información de cursos para extraer información.\nRegistre cursos primero.");
        }

    }

    @FXML
    private void goToGroupAssignationToInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/GroupAssignationToInstructorView.fxml",
                "Asignar grupo", event);
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
