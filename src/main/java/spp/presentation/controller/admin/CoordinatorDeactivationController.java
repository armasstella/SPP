package spp.presentation.controller.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.CoordinatorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class CoordinatorDeactivationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<CoordinatorDTO> tblCoordinators;
    @FXML private TableColumn<CoordinatorDTO, String> colNames;
    @FXML private TableColumn<CoordinatorDTO, String> colSurnames;
    @FXML private TableColumn<CoordinatorDTO, String> colEmail;
    @FXML private TableColumn<CoordinatorDTO, String> colPersonalNumber;
    private final CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainCoordinators();

    }

    private void setUpColumns() {
        colNames.setCellValueFactory(
                new GenericNestedSelector<>("firstName", "Sin nombre"));
        colSurnames.setCellValueFactory(
                new GenericNestedSelector<>("firstLastName", "Sin apellidos"));
        colEmail.setCellValueFactory(
                new GenericNestedSelector<>("email", "Sin correo electrónico"));
        colPersonalNumber.setCellValueFactory(
                new GenericNestedSelector<>("personalNumber", "Sin número de personal"));

    }

    @FXML
    private void obtainCoordinators() {
        try {
            List<CoordinatorDTO> coordinatorsList = coordinatorDAO.getActiveCoordinators();
            ObservableList<CoordinatorDTO> coordinatorsObservableList =
                    FXCollections.observableArrayList(coordinatorsList);
            tblCoordinators.setItems(coordinatorsObservableList);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al obtener lista de coordinadores");
        }

    }

    @FXML
    private void deactivateCoordinator(ActionEvent event) {
        CoordinatorDTO coordinatorSelected = tblCoordinators.getSelectionModel().getSelectedItem();
        if (coordinatorSelected == null) {
            StatusLabel.showError(lblStatus, "Seleccione el coordinador a inactivar");
            return;
        }
        if (AlertHelper.showConfirmation("Confirmar acción",
                "¿Seguro que desea inactivar \"" + coordinatorSelected.getPersonalNumber() + "\"?")) {
            try {
                if (coordinatorDAO.deactivateCoordinator(coordinatorSelected)) {
                    obtainCoordinators();
                    StatusLabel.showSuccess(lblStatus, "Coordinador inactivado exitosamente.");
                }
            } catch (DAOException e) {
                AppLogger.logError(e);
                StatusLabel.showError(lblStatus, "Error al inactivar coordinador.");
            }
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/admin/AdminMenuView.fxml",
                "Menú Administrador", event);

    }

}
