package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import spp.businesslogic.dao.PrioritizedProjectDAO;
import spp.businesslogic.dao.ProjectDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.user.MessageCenterController;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.window.ViewNavigator;


public class InternMenuController {

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);

    }

    @FXML
    private void goToMonthlyActivityRegistrationView(ActionEvent event) {
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
        if (!searchPrioritizedProjects()) {
            if (searchMinimumProjects()) {
                ViewNavigator.loadView("/spp/presentation/view/intern/AvailableProjectsView.fxml",
                        "Proyectos disponibles", event);
            }
        }

    }

    private boolean searchPrioritizedProjects() {
        boolean hasPrioritizedProjects = false;

        try {
            PrioritizedProjectDAO prioritizedProjectDAO = new PrioritizedProjectDAO();
            hasPrioritizedProjects = prioritizedProjectDAO.findPrioritizedProjectsByInternEmail(ActiveSessionDTO.get().getEmail());
            if (hasPrioritizedProjects) {
                AlertHelper.showErrorMessage("Operación no disponible",
                        "Ya has seleccionado tres proyectos");
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return hasPrioritizedProjects;

    }

    private boolean searchMinimumProjects() {
        boolean hasMinimumProjectsForActiveTerm = false;
        try {
            ProjectDAO projectDAO = new ProjectDAO();
            hasMinimumProjectsForActiveTerm = projectDAO.hasMinimumProjectsForActiveTerm();
            if (!hasMinimumProjectsForActiveTerm) {
                AlertHelper.showErrorMessage("Error", "No hay suficientes proyectos disponibles\n" +
                        "Revise después.");
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return hasMinimumProjectsForActiveTerm;

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

    @FXML
    private void goToPartialReportView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/PartialReporteView.fxml",
                "Reporte Parcial", event);
    }

    @FXML
    private void goToFinalActivityRegistrationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/FinalActivityRegistersView.fxml",
                "Reporte Final", event);
    }

}
