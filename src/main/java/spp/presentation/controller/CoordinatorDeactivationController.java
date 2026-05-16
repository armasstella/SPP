package spp.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.CoordinatorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CoordinatorDeactivationController implements Initializable {
    @FXML private Label lblStatus;
    @FXML private TableView<CoordinatorDTO> tblCoordinators;
    @FXML private TableColumn<CoordinatorDTO, String> clmnNames;
    @FXML private TableColumn<CoordinatorDTO, String> clmnSurnames;
    @FXML private TableColumn<CoordinatorDTO, String> clmnEmail;
    @FXML private TableColumn<CoordinatorDTO, String> clmnPersonalNumber;

    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();
    private ObservableList<CoordinatorDTO> coordinatorsObservableList;
    private CoordinatorDTO coordinatorInEdition;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
        setUpColumns();
        obtainCoordinators();
    }

    private void setUpColumns() {
        clmnNames.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        clmnSurnames.setCellValueFactory(
                new PropertyValueFactory<>("firstLastName"));
        clmnEmail.setCellValueFactory(
                new PropertyValueFactory<>("email"));
        clmnPersonalNumber.setCellValueFactory(
                new PropertyValueFactory<>("personalNumber"));

    }

    @FXML
    private void obtainCoordinators() {
        try {
            List<CoordinatorDTO> coordinatorsList = coordinatorDAO.obtainAllActiveCoordinators();
            coordinatorsObservableList = FXCollections.observableArrayList(coordinatorsList);
            tblCoordinators.setItems(coordinatorsObservableList);
        } catch (DAOException e) {
            showError("Error al obtener la lista de coordinadores.");
        }

    }

    @FXML
    private void deactivateCoordinator(ActionEvent event) {
        CoordinatorDTO coordinatorSelected = tblCoordinators.getSelectionModel().getSelectedItem();
        if (coordinatorSelected == null) {
            showError("Seleccione el coordinador a inactivar");
            return;
        }
        if (AlertHelper.showConfirmation("Confirmar acción",
                "¿Seguro que desea inactivar \"" + coordinatorSelected.getPersonalNumber() + "\"?")) {
            try {
                if (coordinatorDAO.inactivateCoordinator(coordinatorSelected)) {
                    obtainCoordinators();
                    showSuccess("Coordinador inactivado exitosamente.");
                }
            } catch (DAOException e) {
                AppLogger.logError(e);
                showError("Error al inactivar coordinador.");
            }

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
