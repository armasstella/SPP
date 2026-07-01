package spp.presentation.controller.intern;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.dao.PrioritizedProjectDAO;
import spp.businesslogic.dao.ProfessionalPracticeEnrollmentDAO;
import spp.businesslogic.dao.ProjectDAO;
import spp.businesslogic.dao.SelfEvaluationDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.user.MessageCenterController;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.window.ViewNavigator;

import java.net.URL;
import java.util.ResourceBundle;


public class InternMenuController implements Initializable {

    @FXML private BorderPane rootMenuPane;
    ProfessionalPracticeEnrollmentDAO professionalPracticeEnrollmentDAO = new ProfessionalPracticeEnrollmentDAO();
    InternDocumentDAO  internDocumentDAO = new InternDocumentDAO();

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
        if (searchInternProjectAssigned()) {
            if (!searchMonthlyReports()) {
                ViewNavigator.loadView("/spp/presentation/view/intern/MonthlyReportMenu.fxml",
                        "Menú de Reportes Mensuales", event);
            } else {
                AlertHelper.showMessage("Aviso", "Ya has subido al sistema tus reportes mensuales.");
            }
        } else {
            showNoProjectAssignationMessage();
        }

    }

    @FXML
    private void goToUploadDocumentsView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/UploadDocumentsView.fxml",
                "Subir documentos", event);

    }

    @FXML
    private void goToAvailableProjectsView(ActionEvent event) {
        if (!searchInternProjectAssigned()) {
            if (!searchPrioritizedProjects()) {
                if (searchMinimumProjects()) {
                    ViewNavigator.loadView("/spp/presentation/view/intern/AvailableProjectsView.fxml",
                            "Proyectos disponibles", event);
                }
            }
        } else {
            AlertHelper.showMessage("Aviso", "Ya tienes un proyecto asignado");
        }


    }

    @FXML
    private void goToSelfEvaluationView(ActionEvent event) {
        if (searchInternProjectAssigned()) {
            if (!searchSelfEvaluation()) {
                ViewNavigator.loadView("/spp/presentation/view/intern/SelfEvaluationGenerationView.fxml",
                        "Generar Autoevaluación", event);
            } else {
                AlertHelper.showMessage("Aviso", "Ya has realizado la autoevaluación");
            }

        } else {
            showNoProjectAssignationMessage();
        }

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
        if (searchInternProjectAssigned()) {
            if (!searchPartialReport()) {
                ViewNavigator.loadView("/spp/presentation/view/intern/PartialReportGenerationView.fxml",
                        "Generar Reporte Parcial", event);
            } else {
                AlertHelper.showMessage("Aviso", "Ya has subido al sistema tu reporte parcial.");
            }

        } else {
            showNoProjectAssignationMessage();
        }

    }

    @FXML
    private void goToFinalActivityRegistrationView(ActionEvent event) {
        if (searchInternProjectAssigned()) {
            if (!searchFinalReport()) {
                ViewNavigator.loadView("/spp/presentation/view/intern/FinalReportMenu.fxml",
                        "Menú de Reporte Final", event);
            } else {
                AlertHelper.showMessage("Aviso", "Ya has subido al sistema tu reporte final.");
            }

        } else {
            showNoProjectAssignationMessage();
        }

    }

    private boolean searchPrioritizedProjects() {
        boolean hasPrioritizedProjects = false;

        try {
            PrioritizedProjectDAO prioritizedProjectDAO = new PrioritizedProjectDAO();
            hasPrioritizedProjects = prioritizedProjectDAO.findPrioritizedProjectsByInternEmail(ActiveSessionDTO.get().getEmail());
            if (hasPrioritizedProjects) {
                AlertHelper.showMessage("Operación no disponible",
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

    private boolean searchInternProjectAssigned() {
        boolean hasProjectAssigned = false;
        try {
            if (professionalPracticeEnrollmentDAO.hasProjectAssignedInEnrollment(
                    ActiveSessionDTO.get().getEmail())) {
                hasProjectAssigned = true;
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        System.out.println("hasProjectAssigned: " + hasProjectAssigned);
        return hasProjectAssigned;
    }

    private void showNoProjectAssignationMessage() {
        AlertHelper.showMessage("Operación no disponible",
                "No cuentas con un proyecto asignado.");
    }

    private boolean searchMonthlyReports() {
        boolean hasMonthlyReports = false;
        try {
            if (internDocumentDAO.hasAllMonthlyReports(ActiveSessionDTO.get().getEmail())) {
                hasMonthlyReports = true;
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return hasMonthlyReports;
    }

    private boolean searchPartialReport() {
        boolean hasPartialReport = false;
        try {
            if (internDocumentDAO.hasPartialReport(ActiveSessionDTO.get().getEmail())) {
                hasPartialReport = true;
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return hasPartialReport;
    }

    private boolean searchFinalReport() {
        boolean hasFinalReport = false;
        try {
            if (internDocumentDAO.hasFinalReport(ActiveSessionDTO.get().getEmail())) {
                hasFinalReport = true;
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return hasFinalReport;
    }

    private boolean searchSelfEvaluation() {
        boolean hasDoneSelfEvaluation = false;
        SelfEvaluationDAO selfEvaluationDAO = new SelfEvaluationDAO();
        try {
            if (selfEvaluationDAO.hasSelfEvaluation(ActiveSessionDTO.get().getEmail())){
                hasDoneSelfEvaluation = true;
            }
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        return hasDoneSelfEvaluation;
    }


}
