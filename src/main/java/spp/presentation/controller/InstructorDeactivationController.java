package spp.presentation.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InstructorDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class InstructorDeactivationController implements Initializable {
    @FXML private Label lblStatus;
    @FXML private TableView<InstructorDTO> tblInstructors;
    @FXML private TableColumn<InstructorDTO, String> clmnNames;
    @FXML private TableColumn<InstructorDTO, String> clmnSurnames;
    @FXML private TableColumn<InstructorDTO, String> clmnEmail;
    @FXML private TableColumn<InstructorDTO, String> clmnPersonalNumber;
    @FXML private TableColumn<InstructorDTO, String> clmnShift;

    private final InstructorDAO instructorDAO = new InstructorDAO();
    private ObservableList<InstructorDTO> instructorsObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainInstructors();
    }

    @FXML
    public void deactivateInstructor() {
        InstructorDTO instructorSelected = tblInstructors.getSelectionModel().getSelectedItem();
        if(instructorSelected == null) {
            showError("Seleccione el profesor a inactivar");
            return;
        }

        if (AlertHelper.showConfirmation("Confirmar acción",
                "¿Seguro que desea inactivar \"" + instructorSelected.getPersonalNumber() + "\"?")) {
            try {
                if (instructorDAO.deactivateInstructor(instructorSelected)) {
                    obtainInstructors();
                    showSuccess("Profesor inactivado exitosamente.");
                }
            } catch (DAOException e) {
                AppLogger.logError(e);
                showError("Error al inactivar profesor.");
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                "Menú Administrador", event);
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
        clmnShift.setCellValueFactory(
                new PropertyValueFactory<>("shift"));

    }

    @FXML
    private void obtainInstructors() {
        try {
            List<InstructorDTO> instructorsList = instructorDAO.obtainAllActiveInstructors();
            instructorsObservableList = FXCollections.observableArrayList(instructorsList);
            tblInstructors.setItems(instructorsObservableList);
        } catch (DAOException e) {
            showError("Error al obtener la lista de coordinadores.");
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
