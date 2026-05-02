package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.CoordinatorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPersonalNumber;
    @FXML private TextField txtPassword;

    @FXML private TextField txtPersonalNumberToggle;
    @FXML private Label lblTitle;
    @FXML private Label lblSubtitle;
    @FXML private Button btnConfirm;

    @FXML private Label lblStatus;

    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

    public enum ToggleMode { ACTIVATE, INACTIVATE }
    private ToggleMode toggleMode;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
        if (toggleMode != null) {
            applyToggleModeUI();
        }
    }

    @FXML
    private void goToAddCoordinatorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewCoordinatorView.fxml",
                "Registrar Coordinador", event);
    }

    @FXML
    private void goToAddInstructorView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/NewCoordinatorView.fxml",
                "Registrar Profesor", event);
    }

    @FXML
    private void goToActivateCoordinatorView(ActionEvent event) {
        loadViewWithMode("/spp/presentation/view/CoordinatorChangeStatusView.fxml",
                "Activar Coordinador", event, ToggleMode.ACTIVATE);
    }

    @FXML
    private void goToInactivateCoordinatorView(ActionEvent event) {
        loadViewWithMode("/spp/presentation/view/CoordinatorChangeStatusView.fxml",
                "Inactivar Coordinador", event, ToggleMode.INACTIVATE);
    }

    @FXML
    private void goToActiveInstructorView(ActionEvent event) {
        loadViewWithMode("/spp/presentation/view/InstructorChangeStatusView.fxml",
                "Inactivar Profesor", event,
                ToggleMode.INACTIVATE);
    }

    @FXML
    private void goToInactiveInstructorView(ActionEvent event) {
        loadViewWithMode("/spp/presentation/view/CoordinatorChangeStatusView.fxml",
                "Inactivar Coordinador", event,
                ToggleMode.INACTIVATE);
    }

    @FXML
    private void goToMainMenu(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/MainMenuView.fxml", "Menú Principal", event);
    }

    @FXML
    private void goToLoginView(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/LoginView.fxml",
                "Inicia sesión", event);
    }

    private void setAllCoordinatorDTO(ActionEvent event, CoordinatorDTO coordinatorDTO) {

        coordinatorDTO.setFirstName(txtFirstName.getText().trim());
        coordinatorDTO.setSecondName(txtSecondName.getText().trim());
        coordinatorDTO.setFirstLastName(txtFirstLastName.getText().trim());
        coordinatorDTO.setSecondLastName(txtSecondLastName.getText().trim());
        coordinatorDTO.setEmail(txtEmail.getText().trim());
        coordinatorDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        coordinatorDTO.setPersonalNumber(txtPersonalNumber.getText().trim());
        coordinatorDTO.setPassword(txtPassword.getText().trim());

    }

    @FXML
    private void saveCoordinator(ActionEvent event) {

        clearStatus();
        if (validateEmptyFields()) {
            return;
        }

        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        setAllCoordinatorDTO(event, coordinatorDTO);

        try {
            if (coordinatorDAO.addCoordinator(coordinatorDTO)) {
                showSuccess("Coordinador registrado correctamente.");
                clearAddFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    public void setToggleMode(ToggleMode mode) {
        this.toggleMode = mode;
        if (lblTitle != null) {
            applyToggleModeUI();
        }
    }

    @FXML
    private void changeStatus(ActionEvent event) {
        clearStatus();
        String personalNumber = txtPersonalNumberToggle.getText().trim();

        if (personalNumber.isBlank()) {
            showError("Ingresa el número de personal.");
            return;
        }

        try {
            boolean success = executeChangeStatus(personalNumber);
            if (success) {
                String message = (toggleMode == ToggleMode.ACTIVATE)
                        ? "Coordinador activado correctamente."
                        : "Coordinador inactivado correctamente.";
                showSuccess(message);
                txtPersonalNumberToggle.clear();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    private boolean executeChangeStatus(String personalNumber) throws DAOException {
        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        coordinatorDTO.setPersonalNumber(personalNumber);

        return (toggleMode == ToggleMode.ACTIVATE)
                ? coordinatorDAO.activateCoordinator(coordinatorDTO)
                : coordinatorDAO.inactivateCoordinator(coordinatorDTO);
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                "Menu Administrador", event);
    }

    private boolean validateEmptyFields() {
        boolean emptyFields = false;
        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtEmail.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtPersonalNumber.getText().isBlank() ||
                txtPassword.getText().isBlank()) {
            showError("Completa todos los campos obligatorios.");
            emptyFields = true;
        }
        return emptyFields;
    }

    private void clearAddFields() {
        txtFirstName.clear();
        txtSecondName.clear();
        txtFirstLastName.clear();
        txtSecondLastName.clear();
        txtEmail.clear();
        txtPhoneNumber.clear();
        txtPersonalNumber.clear();
        txtPassword.clear();
    }

    private void applyToggleModeUI() {
        if (toggleMode == ToggleMode.ACTIVATE) {
            lblTitle.setText("ACTIVAR COORDINADOR");
            lblSubtitle.setText("Reactivar cuenta de coordinador");
            btnConfirm.setText("Activar");
            btnConfirm.getStyleClass().removeAll("btn-danger");
            btnConfirm.getStyleClass().add("btn-primary");
        } else {
            lblTitle.setText("INACTIVAR COORDINADOR");
            lblSubtitle.setText("Desactivar cuenta de coordinador");
            btnConfirm.setText("Inactivar");
            btnConfirm.getStyleClass().removeAll("btn-primary");
            btnConfirm.getStyleClass().add("btn-danger");
        }
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

    private void clearStatus() {
        if (lblStatus != null) {
            lblStatus.setText("");
            lblStatus.getStyleClass().removeAll("error", "success");
        }
    }

    private void loadViewWithMode(String fxmlPath, String title, ActionEvent event, ToggleMode mode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            AdminMenuController controller = loader.getController();
            controller.setToggleMode(mode);
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 420, 380));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            AppLogger.logError(e);
        }
    }
}