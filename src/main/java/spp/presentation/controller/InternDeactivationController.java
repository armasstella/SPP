package spp.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.InternDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InternDeactivationController implements Initializable {
    @FXML private Label lblStatus;
    @FXML private TableView<InternDTO> tblInterns;
    @FXML private TableColumn<InternDTO, String> clmnStudentNumber;
    @FXML private TableColumn<InternDTO, String> clmnNames;
    @FXML private TableColumn<InternDTO, String> clmnSurnames;
    @FXML private TableColumn<InternDTO, String> clmnEmail;

    private final InternDAO internDAO = new InternDAO();
    private ObservableList internsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainInterns();
    }

    private void obtainInterns() {
        try {
            List<InternDTO> internsList = internDAO.obtainAllActiveInterns();
            internsObservableList = FXCollections.observableArrayList(internsList);
            tblInterns.setItems(internsObservableList);
        } catch (DAOException e) {
            showError("Error al obtener la lista de practicantes.");
        }

    }

    private void setUpColumns() {
        clmnStudentNumber.setCellValueFactory(
                new PropertyValueFactory<>("studentNumber"));
        clmnNames.setCellValueFactory(
                new PropertyValueFactory<>("firstName"));
        clmnSurnames.setCellValueFactory(
                new PropertyValueFactory<>("firstLastName"));
        clmnEmail.setCellValueFactory(
                new PropertyValueFactory<>("email"));

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
    }

    @FXML
    private void deactivateIntern(ActionEvent event) {
        InternDTO internSelected = tblInterns.getSelectionModel().getSelectedItem();
        if (internSelected == null) {
            showError("Seleccione el coordinador a inactivar");
            return;
        }
        if (AlertHelper.showConfirmation("Confirmar acción",
                "¿Seguro que desea inactivar \"" + internSelected.getStudentNumber() + "\"?")) {
            try {
                if (internDAO.inactivateIntern(internSelected)) {
                    obtainInterns();
                    showSuccess("Practicante inactivado exitosamente.");
                }
            } catch (DAOException e) {
                AppLogger.logError(e);
                showError("Error al inactivar practicante.");
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


}
