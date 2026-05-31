package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.UserDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;


public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void login(ActionEvent event) {
        if (validateEmptyDataFields()) {
            return;
        }

        String email = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        try {
            LoginResultDTO result = userDAO.login(email, password);
            int idUser = userDAO.obtainId(email);

            if (result == null) {
                StatusLabel.showError(lblStatus, "Usuario o contraseña incorrectos.");
                return;
            }

            StatusLabel.showSuccess(lblStatus, "Bienvenido al sistema.");
            goToView(result.getUserType(), event);

        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    private void goToView(String userType, ActionEvent event) {
        switch (userType) {
            case "Coordinador":
                ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                        "Menú Coordinador", event);
                break;
            case "Practicante":
                ViewNavigator.loadView("/spp/presentation/view/InternMenuView.fxml",
                        "Menú Practicante", event);
                break;
            case "Profesor":
                ViewNavigator.loadView("/spp/presentation/view/InstructorMenuView.fxml",
                        "Menú Profesor", event);
                break;
            case "Administrador":
                ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                        "Menú Administrador", event);
                break;
            default:
                StatusLabel.showError(lblStatus, "Tipo de usuario no reconocido.");
        }

    }

    private boolean validateEmptyDataFields() {
        boolean emptyFields = false;

        if (txtEmail.getText().isBlank() || txtPassword.getText().isBlank()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            emptyFields = true;
        }

        return emptyFields;

    }

}