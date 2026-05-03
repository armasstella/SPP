package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.CoordinatorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.ResourceBundle;

public class CoordinatorDeactivationController implements Initializable {

    @FXML private TextField txtPersonalNumber;
    @FXML private Label lblStatus;
    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
    }

    @FXML
    private void deactivateCoordinator(ActionEvent event) {
        if (txtPersonalNumber.getText().trim().isBlank()) {
            showError("Ingresa el número de personal.");
            return;
        }
        try {
            CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
            coordinatorDTO.setPersonalNumber(txtPersonalNumber.getText().trim());
            if (coordinatorDAO.inactivateCoordinator(coordinatorDTO)) {
                showSuccess("Coordinador inactivado correctamente.");
                txtPersonalNumber.clear();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
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

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                "Menú Administrador", event);
    }

}
