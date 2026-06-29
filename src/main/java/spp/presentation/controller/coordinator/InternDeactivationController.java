package spp.presentation.controller.coordinator;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InternDAO;
import spp.utils.view.alert.AlertHelper;
import spp.utils.view.table.GenericNestedSelector;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class InternDeactivationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TableView<InternDTO> tblInterns;
    @FXML private TableColumn<InternDTO, String> colStudentNumber;
    @FXML private TableColumn<InternDTO, String> colFullNames;
    @FXML private TableColumn<InternDTO, String> colEmail;
    private final InternDAO internDAO = new InternDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpColumns();
        obtainInterns();

    }

    private void obtainInterns() {
        try {
            List<InternDTO> internsList = internDAO.getActiveInterns();
            ObservableList<InternDTO> internsObservableList = FXCollections.observableArrayList(internsList);
            tblInterns.setItems(internsObservableList);
        } catch (DAOException e) {
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    private void setUpColumns() {
        colStudentNumber.setCellValueFactory(
                new GenericNestedSelector<>("studentNumber", "Sin matricula"));
        colFullNames.setCellValueFactory(
                new GenericNestedSelector<>("fullName", "Sin nombres"));
        colEmail.setCellValueFactory(
                new GenericNestedSelector<>("email", "Sin correo electrónico"));

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
    }

    @FXML
    private void deactivateIntern(ActionEvent event) {
        InternDTO internSelected = tblInterns.getSelectionModel().getSelectedItem();

        if (internSelected == null) {
            StatusLabel.showError(lblStatus, "Seleccione el coordinador a inactivar");
        } else {
            if (AlertHelper.showConfirmation("Confirmar acción",
                    "¿Seguro que desea inactivar \"" + internSelected.getStudentNumber() + "\"?")) {
                try {
                    if (internDAO.deactivateIntern(internSelected)) {
                        obtainInterns();
                        StatusLabel.showSuccess(lblStatus, "Practicante inactivado exitosamente.");
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, e.getMessage());
                }
            }
        }
    }

}
