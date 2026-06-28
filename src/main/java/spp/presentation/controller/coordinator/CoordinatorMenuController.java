package spp.presentation.controller.coordinator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.businesslogic.dao.CourseDAO;
import spp.businesslogic.dao.LinkedOrganizationDAO;
import spp.businesslogic.dao.ProjectManagerDAO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.user.MessageCenterController;
import spp.utils.view.AlertHelper;
import spp.utils.view.ViewNavigator;


public class CoordinatorMenuController {

    CourseDAO courseDAO = new CourseDAO();

    @FXML
    private void goToNewInternView(ActionEvent event) {
        if (searchCourse()) {
            ViewNavigator.loadView("/spp/presentation/view/coordinator/NewInternView.fxml",
                    "Registrar Practicante", event);
        } else {
            AlertHelper.showErrorMessage("No puede realizar la operación",
                    "No hay Experiencias Educativas registradas");

        }

    }

    @FXML
    private void goToInternDeactivationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/InternDeactivationView.fxml",
                "Inactivar Practicante", event);

    }

    @FXML
    private void goToNewLinkedOrganizationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/NewLinkedOrganizationView.fxml",
                "Registrar Organización", event);

    }

    @FXML
    private void goToNewProjectManagerView(ActionEvent event) {
        if (searchLinkedOrganization()) {
            ViewNavigator.loadView("/spp/presentation/view/coordinator/NewProjectManagerView.fxml",
                    "Registrar Encargado de Proyecto", event);
        } else {
            AlertHelper.showErrorMessage("No puede realizar la operación",
                    "No hay información registrada de Organizaciones Vinculadas");
        }

    }

    @FXML
    private void goToNewProjectView(ActionEvent event) {
        if (searchLinkedOrganization()) {
            if (searchProjectManager()) {
                ViewNavigator.loadView("/spp/presentation/view/coordinator/NewProjectView.fxml",
                        "Registrar Proyecto", event);
            } else {
                AlertHelper.showErrorMessage("No se puede realizar la operación",
                        "No hay información registrada de Encargados de Proyecto");
            }

        } else {
            AlertHelper.showErrorMessage("No puede realizar la operación",
                    "No hay información registrada de Organizaciones Vinculadas");
        }


    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Iniciar sesión", event);

    }

    @FXML
    private void goToProjectAssignmentView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/ProjectAssignmentView.fxml",
                "Asignar Proyecto", event);

    }

    @FXML
    private void goToProjectUpdateView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/ProjectUpdateView.fxml",
                "Actualizar Proyecto", event);

    }

    @FXML
    private void goToProjectDeletionView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/ProjectDeletionView.fxml",
                "Eliminar Proyecto", event);

    }

    @FXML
    private void goToIndicatorReportView(ActionEvent event) {
        if (searchCourse()) {
            ViewNavigator.loadView("/spp/presentation/view/coordinator/IndicatorReportView.fxml",
                    "Generar Reporte", event);
        } else {
            AlertHelper.showErrorMessage("No puede realizar la operación",
                    "No hay Experiencias Educativas registradas");

        }

    }

    @FXML
    private void goToGroupAssignationToInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/GroupAssignationToInstructorView.fxml",
                "Asignar grupo", event);

    }

    @FXML
    private void goToCourseInformationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CourseInformationView.fxml",
                "Cursos", event);

    }

    @FXML
    private void goToMessageCenter(ActionEvent event) {
        MessageCenterController messageCenterController = ViewNavigator.loadView(
                "/spp/presentation/view/user/MessageCenterView.fxml",
                "Centro de mensajes", event);

        if (messageCenterController != null) {
            messageCenterController.setPreviousView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                    "Menú Coordinador");
        }

    }

    private boolean searchLinkedOrganization() {
        boolean exists = false;

        try {
            LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();
            exists = linkedOrganizationDAO.existsLinkedOrganizations();
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return exists;

    }

    private boolean searchProjectManager() {
        boolean exists = false;

        try {
            ProjectManagerDAO projectManagerDAO = new ProjectManagerDAO();
            exists = projectManagerDAO.existsProjectManagers();
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return exists;

    }

    private boolean searchCourse() {
        boolean exists = false;
        try {
            CourseDAO courseDAO = new CourseDAO();
            exists = courseDAO.existsRegisteredCourses();
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return exists;
    }
}
