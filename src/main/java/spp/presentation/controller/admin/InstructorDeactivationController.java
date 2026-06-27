package spp.presentation.controller.admin;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InstructorDAO;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import spp.utils.view.AlertHelper;
import spp.utils.view.GenericNestedSelector;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class InstructorDeactivationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<InstructorDTO> tblInstructors;
    @FXML private TableColumn<InstructorDTO, String> colNames;
    @FXML private TableColumn<InstructorDTO, String> colSurnames;
    @FXML private TableColumn<InstructorDTO, String> colEmail;
    @FXML private TableColumn<InstructorDTO, String> colPersonalNumber;
    @FXML private TableColumn<InstructorDTO, String> colShift;
    private final InstructorDAO instructorDAO = new InstructorDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainInstructors();

    }

    @FXML
    public void deactivateInstructor() {
        InstructorDTO instructorSelected = tblInstructors.getSelectionModel().getSelectedItem();
        if(instructorSelected == null) {
            StatusLabel.showError(lblStatus, "Seleccione el profesor a inactivar");
        } else {
            if (AlertHelper.showConfirmation("Confirmar acción",
                    "¿Seguro que desea inactivar \"" + instructorSelected.getPersonalNumber() + "\"?")) {
                try {
                    if (instructorDAO.deactivateInstructor(instructorSelected)) {
                        obtainInstructors();
                        StatusLabel.showSuccess(lblStatus, "Profesor inactivado exitosamente.");
                    }
                } catch (DAOException e) {
                    AppLogger.log(ExceptionLevel.FATAL, e);
                    StatusLabel.showError(lblStatus, "Error al inactivar profesor.");
                }
            }
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/admin/AdminMenuView.fxml",
                "Menú Administrador", event);

    }

    private void setUpColumns() {
        colNames.setCellValueFactory(
                new GenericNestedSelector<>("firstName", "Sin nombres"));
        colSurnames.setCellValueFactory(
                new GenericNestedSelector<>("firstLastName", "Sin apellidos"));
        colEmail.setCellValueFactory(
                new GenericNestedSelector<>("email", "Sin correo electrónico"));
        colPersonalNumber.setCellValueFactory(
                new GenericNestedSelector<>("personalNumber", "Sin número de personal"));
        colShift.setCellValueFactory(
                new GenericNestedSelector<>("shift", "Sin turno"));

    }

    @FXML
    private void obtainInstructors() {
        try {
            List<InstructorDTO> instructorsList = instructorDAO.getActiveInstructors();
            ObservableList<InstructorDTO> instructorsObservableList =
                    FXCollections.observableArrayList(instructorsList);
            tblInstructors.setItems(instructorsObservableList);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, "Error al obtener lista de profesores");
        }

    }

}
