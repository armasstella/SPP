package spp.presentation.controller.instructor;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import spp.businesslogic.dao.InstructorDAO;
import spp.businesslogic.dao.UserDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.presentation.controller.user.MessageCenterController;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.window.ViewNavigator;

import java.net.URL;
import java.util.ResourceBundle;


public class InstructorMenuController implements Initializable {

    @FXML VBox vbAvailableOptions;
    @FXML VBox vbErrorStatusMessage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        verifyCourseAssignation();
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);

    }

    @FXML
    private void goToMessageCenter(ActionEvent event) {
        MessageCenterController messageCenterController = ViewNavigator.loadView(
                "/spp/presentation/view/user/MessageCenterView.fxml",
                "Centro de mensajes", event);

        if (messageCenterController != null) {
            messageCenterController.setPreviousView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                    "Menú Profesor");
        }

    }

    @FXML
    private void goToPresentationTemplateUploadView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/PresentationTemplateUploadView.fxml",
                "Subir plantilla presentación", event);

    }

    @FXML
    private void goToReviewDocumentsView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/ReviewDocumentsView.fxml",
                "Revisión Documentos", event);

    }

    @FXML
    private void goToPracticeReleaseView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/instructor/PracticeReleaseView.fxml",
                "Revisión Documentos", event);

    }

    private void verifyCourseAssignation() {
        boolean hasInstructorCourseAssigned = false;

        try {
            InstructorDAO instructorDAO = new InstructorDAO();
            UserDAO userDAO = new UserDAO();
            int instructorId = userDAO.obtainId(ActiveSessionDTO.get().getEmail());
            hasInstructorCourseAssigned = instructorDAO.hasInstructorCourseAssigned(instructorId);
        } catch (DAOException e) {
            AlertHelper.showErrorMessage("Error", e.getMessage());
        }

        if (hasInstructorCourseAssigned) {
            vbAvailableOptions.setVisible(true);
            vbErrorStatusMessage.setVisible(false);
        } else {
            vbAvailableOptions.setVisible(false);
            vbErrorStatusMessage.setVisible(true);
        }

    }


}
