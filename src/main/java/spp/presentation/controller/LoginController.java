package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.TermDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.dto.SessionDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.UserDAO;
import spp.utils.logger.AppLogger;
import spp.utils.term.TermCalculator;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML private TextField txtEmail;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;
    private final UserDAO userDAO = new UserDAO();
    private final TermDAO termDAO = new TermDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();

    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtEmail,
                ViewConstant.PATTERN_EMAIL_CHARS, ViewConstant.MAX_LENGTH_EMAIL);
        InputFilter.applyFormatFilter(txtPassword,
                ViewConstant.PATTERN_PASSWORD_CHARS, ViewConstant.MAX_LENGTH_PASSWORD);

    }

    @FXML
    private void login(ActionEvent event) {
        if (!hasEmptyDataFields()) {

            String email = txtEmail.getText().trim();
            String password = txtPassword.getText().trim();

            try {
                LoginResultDTO result = userDAO.login(email, password);
                userDAO.obtainId(email);

                if (result.isSuccess()) {
                    synchronizeCurrentTerm();
                    ActiveSessionDTO.initialize(new SessionDTO(email, TermCalculator.getCurrentPeriod()));
                    StatusLabel.showSuccess(lblStatus, "Bienvenido al sistema.");
                    goToView(result.getUserType(), event);
                } else {
                    StatusLabel.showError(lblStatus, result.getMessage());
                }

            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "Credenciales ingresadas incorrectas");
            }

        }
    }


    private void goToView(String userType, ActionEvent event) {
        switch (userType) {
            case "Coordinador":
                ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                        "Menú Coordinador", event);
                break;
            case "Practicante":
                ViewNavigator.loadView("/spp/presentation/view/intern/InternMenuView.fxml",
                        "Menú Practicante", event);
                break;
            case "Profesor":
                ViewNavigator.loadView("/spp/presentation/view/instructor/InstructorMenuView.fxml",
                        "Menú Profesor", event);
                break;
            case "Administrador":
                ViewNavigator.loadView("/spp/presentation/view/admin/AdminMenuView.fxml",
                        "Menú Administrador", event);
                break;
            default:
                StatusLabel.showError(lblStatus, "Tipo de usuario no reconocido.");
        }

    }

    private boolean hasEmptyDataFields() {
        boolean emptyFields = false;

        if (txtEmail.getText().isBlank() || txtPassword.getText().isBlank()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            emptyFields = true;
        }

        return emptyFields;

    }


    private void synchronizeCurrentTerm() throws DAOException {
        String currentRealTerm = TermCalculator.getCurrentPeriod();
        String activeTermRegister = termDAO.findActiveTermName();

        if (activeTermRegister == null) {
            termDAO.insertTerm(currentRealTerm);
        } else if (!activeTermRegister.equals(currentRealTerm)) {
            termDAO.deactivateCurrentTerm();
            termDAO.insertTerm(currentRealTerm);
        }
    }

}