package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.CoordinatorDAO;
import spp.dataaccess.dao.InternDAO;
import spp.dataaccess.dao.InstructorDAO;
import spp.dataaccess.dao.UserDAO;
import spp.utils.view.ViewNavigator;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;

    private static final int TYPE_INTERN = 1;
    private static final int TYPE_ACADEMIC = 2;
    private static final int TYPE_ADMIN = 3;
    private static final int TYPE_UNKNOWN = -1;

    @FXML
    private void login(ActionEvent event) {

        if (validateEmptyFields()) {
            return;
        }

        String user = txtEmail.getText().trim();
        String password = txtPassword.getText().trim();

        int userType = determinateTypeUser(user);

        if (userType == TYPE_UNKNOWN) {
            return;
        }

        boolean accessGranted = false;

        try {
            switch (userType) {
                case TYPE_INTERN:
                    InternDAO internDAO = new InternDAO();
                    accessGranted = internDAO.login(user, password);
                    break;
                case TYPE_ACADEMIC:
                    CoordinatorDAO coordinatorDAO = new CoordinatorDAO();
                    if (coordinatorDAO.existCoordinator(user)) {
                        accessGranted = coordinatorDAO.login(user, password);

                    } else {
                        InstructorDAO instructorDAO = new InstructorDAO();
                        accessGranted = instructorDAO.login(user, password);
                    }
                    break;
                case TYPE_ADMIN:
                    UserDAO userDAO = new UserDAO();
                    accessGranted = userDAO.login(user, password);
                    break;
            }
        } catch (DAOException e) {
            showError("Fallo en conexión al logear");
        }

        if (accessGranted) {
            showSuccess("Bienvenido al sistema.");
            goToView(accessGranted, event, userType);
        } else {
            showError("Usuario o contraseña incorrectos.");
        }
    }

    private void goToView(boolean accessGranted, ActionEvent event, int userType) {
        if (accessGranted){
            switch (userType) {
                case TYPE_INTERN:
                    goToInternMainMenu(event);
                    break;
                case TYPE_ADMIN:
                    goToAdminView(event);
                    break;
                case TYPE_ACADEMIC:
                    goToCoordinatorMainMenu(event);
                    break;
            }
        }

    }

    private void goToCoordinatorMainMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Menú Principal", event);
    }

    private void goToInstructorMainMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/MainMenuInstructorView.fxml",
                "Menú Principal", event);
    }

    private void goToInternMainMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/InternMenuView.fxml",
                "Menú Principal", event);
    }

    private void goToAdminView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                "Menú Principal", event);
    }

    private int determinateTypeUser(String user) {

        if (user == null || user.isEmpty()) {
            showError("El campo de usuario no puede estar vacío.");
            return TYPE_UNKNOWN;
        }

        char firstCharacter = user.charAt(0);

        if (firstCharacter == 'S' || firstCharacter == 's') {
            return TYPE_INTERN;
        }

        if (Character.isDigit(firstCharacter)) {
            return TYPE_ACADEMIC;
        }

        if (firstCharacter == '#') {
            return TYPE_ADMIN;
        }

        showError("Verifique el dato ingresado en el campo de usuario.");
        return TYPE_UNKNOWN;
    }

    private boolean validateEmptyFields() {
        boolean emptyFields = false;
        if (txtEmail.getText().isBlank() || txtPassword.getText().isBlank()) {
            showError("Completa todos los campos obligatorios.");
            emptyFields = true;
        }
        return emptyFields;
    }

    private void showSuccess(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("success");
    }

    private void showError(String message) {
        lblStatus.setText(message);
        lblStatus.getStyleClass().removeAll("error", "success");
        lblStatus.getStyleClass().add("error");
    }
}