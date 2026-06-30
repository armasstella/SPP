package spp.presentation.controller.intern;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import spp.businesslogic.dao.DeliverableProductDAO;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.InternDAO;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.window.ViewNavigator;


public class DeliverableProductRegistrationController {

    @FXML private Label lblStatus;
    @FXML private TextField txtName;
    @FXML private TextArea taDescription;
    @FXML private TextField txtProgress;
    @FXML private TextArea taObservations;

    @FXML
    private void saveDeliverableProduct(ActionEvent event) {
        if (!validateInputs()) {
            DeliverableProductDAO deliverableProductDAO = new DeliverableProductDAO();
            InternDAO internDAO = new InternDAO();

            try {
                String studentNumber = internDAO.findActiveStudentNumberByEmail(ActiveSessionDTO.get().getEmail());
                if (deliverableProductDAO.saveDeliverableProductForIntern(studentNumber, buildDeliverableProduct())) {
                    StatusLabel.showSuccess(lblStatus, "Producto entregable registrado correctamente.");
                    clearFields();
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, e.getMessage());
            }
        }
    }

    private boolean validateInputs() {
        if (txtName.getText().trim().isEmpty()
                || taDescription.getText().trim().isEmpty()
                || taObservations.getText().trim().isEmpty()
                || txtProgress.getText().trim().isEmpty()) {
            return true;
        }

        return false;

    }

    private Integer parseNonNegativeInt(String text) {
        try {
            int value = Integer.parseInt(text.trim());
            return (value >= 0) ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }

    }

    private DeliverableProductDTO buildDeliverableProduct() {
        DeliverableProductDTO deliverableProductDTO = new DeliverableProductDTO();
        deliverableProductDTO.setName(txtName.getText().trim());
        deliverableProductDTO.setDescription(taDescription.getText().trim());
        deliverableProductDTO.setObservations(taObservations.getText().trim());
        deliverableProductDTO.setProgress(parseNonNegativeInt(txtProgress.getText().trim()));

        return deliverableProductDTO;
    }

    private void clearFields() {
        txtName.clear();
        taDescription.clear();
        txtProgress.clear();
        taObservations.clear();

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/intern/FinalReportMenu.fxml",
                "Reporte Final", event);

    }


}
