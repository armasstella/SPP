package spp.presentation.controller.intern;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spp.businesslogic.dao.PrioritizedProjectDAO;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
import spp.businesslogic.dao.ProjectDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.user.MessageCenterController;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.window.ViewNavigator;

import java.net.URL;
import java.util.ResourceBundle;


public class InternMenuController implements Initializable {

    @FXML private BorderPane rootMenuPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(this::enableViewByEnrollmentStatus);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);

    }

    @FXML
    private void goToMonthlyActivityRegistrationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/MonthlyReportMenu.fxml",
                "Menú de Reportes Mensuales", event);
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
                "Generar Autoevaluación", event);

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
        ViewNavigator.loadView("/spp/presentation/view/intern/PartialReportGenerationView.fxml",
                "Generar Reporte Parcial", event);
    }

    @FXML
    private void goToFinalActivityRegistrationView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/FinalReportMenu.fxml",
                "Menú de Reporte Final", event);
    }

    private void enableViewByEnrollmentStatus() {
        if (hasEnrollmentConclude()) {
            AlertHelper.showMessage("Prácticas finalizadas", "Ha concluido sus practicas profesionales");
            Stage currentStage = (Stage) rootMenuPane.getScene().getWindow();
            ViewNavigator.loadView("/spp/presentation/view/intern/EnrollmentConcludeSummaryView.fxml",
                    "Prácticas Concluidas", currentStage);
        }
    }

    private boolean hasEnrollmentConclude() {
        boolean isEnrollmentConclude = false;

        ProfessionalPracticeEnrollmentDAO professionalPracticeEnrollmentDAO
                = new ProfessionalPracticeEnrollmentDAO();
        try {
            if (professionalPracticeEnrollmentDAO.isPracticeCompletedByInternEmail(
                    ActiveSessionDTO.get().getEmail())) {
                isEnrollmentConclude = true;
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return isEnrollmentConclude;
    }


}
