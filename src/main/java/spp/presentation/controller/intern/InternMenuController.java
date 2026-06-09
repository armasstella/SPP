package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.businesslogic.dao.PrioritizedProjectDAO;
import spp.businesslogic.dao.ProjectDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.presentation.controller.user.MessageCenterController;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.ViewNavigator;


public class InternMenuController {

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);

    }

    @FXML
    private void goToMonthlyActivityRegistrationView(ActionEvent event) {
        //TODO: Validar que el practicante en su inscripción tenga asignada una ee
        ViewNavigator.loadView("/spp/presentation/view/intern/MonthlyActivityRegistersView.fxml",
                "Registro de actividad", event);
    }

    @FXML
    private void goToUploadDocumentsView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/UploadDocumentsView.fxml",
                "Subir documentos", event);

    }

    @FXML
    private void goToAvailableProjectsView(ActionEvent event) {

        if (searchPrioritizedProjects()) {
            AlertHelper.showMessage("Operación no permitida", "Ya has seleccionado tres proyectos");
        } else {
            if (searchMinimumProjects()) {
                ViewNavigator.loadView("/spp/presentation/view/intern/AvailableProjectsView.fxml",
                        "Proyectos disponibles", event);
            } else {
                AlertHelper.showErrorMessage("Operación no disponible",
                        "No hay proyectos suficientes para elección del practicante");
            }
        }

    }

    private boolean searchPrioritizedProjects() {
        boolean hasPrioritizedProjects = false;

        try {
            PrioritizedProjectDAO prioritizedProjectDAO = new PrioritizedProjectDAO();
            if (prioritizedProjectDAO.searchPrioritizedProjectsRegister(ActiveSessionDTO.get().getEmail())) {
                hasPrioritizedProjects = true;
            }

        } catch (Exception e) {
            AppLogger.logError(e);
            AlertHelper.showErrorMessage("Error", "Error al realizar operación.\n" +
                    "Intente más tarde.");

        }

        return hasPrioritizedProjects;

    }

    private boolean searchMinimumProjects() {
        boolean isThisOptionAllowed = false;
        try {
            ProjectDAO projectDAO = new ProjectDAO();
            if (projectDAO.verifyMinimumProjects()) {
                isThisOptionAllowed = true;
            } else {
                AlertHelper.showErrorMessage("Operación no disponible",
                        "No hay proyectos suficientes para elección del practicante");
            }

            //TODO: Validar si el practicante ya seleccionó 3 proyectos.

        } catch (Exception e) {
            AppLogger.logError(e);
            AlertHelper.showErrorMessage("Error", "Error al realizar operación.\n" +
                    "Intente más tarde.");
        }

        return isThisOptionAllowed;

    }

    @FXML
    private void goToSelfEvaluationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/SelfEvaluationGenerationView.fxml",
                "Subir documentos", event);

    }

    @FXML
    private void goToMessageCenter(ActionEvent event) {
        MessageCenterController messageCenterController = ViewNavigator.loadView(
                "/spp/presentation/view/user/MessageCenterView.fxml",
                "Centro de mensajes", event);

        if (messageCenterController != null) {
            messageCenterController.setPreviousView("/spp/presentation/view/intern/InternMenuView.fxml",
                    "Menú Practicante");
        }

    }

}
