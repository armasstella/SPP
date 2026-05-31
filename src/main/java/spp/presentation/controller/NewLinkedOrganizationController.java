package spp.presentation.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.LinkedOrganizationDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;


public class NewLinkedOrganizationController {

    @FXML private Label lblStatus;
    @FXML private TextField txtName;
    @FXML private TextField txtRfc;
    @FXML private TextField txtAddress;
    @FXML private TextField txtFiscalAddress;
    @FXML private TextField txtBusiness;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    private final LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();;

    @FXML
    private void setAllLinkedOrganization(ActionEvent event, LinkedOrganizationDTO linkedOrganizationDTO) {
        linkedOrganizationDTO.setName(txtName.getText().trim());
        linkedOrganizationDTO.setRfc(txtRfc.getText().trim());
        linkedOrganizationDTO.setAddress(txtAddress.getText().trim());
        linkedOrganizationDTO.setFiscalAddress(txtFiscalAddress.getText().trim());
        linkedOrganizationDTO.setBusiness(txtBusiness.getText().trim());
        linkedOrganizationDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        linkedOrganizationDTO.setEmail(txtEmail.getText().trim());

    }

    @FXML
    private void saveLinkedOrganization(ActionEvent event) {
        if (validateRegistrationInputs()) {
            return;
        }

        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        setAllLinkedOrganization(event, linkedOrganizationDTO);

        try {
            if (linkedOrganizationDAO.addLinkedOrganization(linkedOrganizationDTO)) {
                StatusLabel.showSuccess(lblStatus, "Organización registrada correctamente.");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            StatusLabel.showError(lblStatus, e.getMessage());
        }

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);

    }

    private boolean validateRegistrationInputs() {
        boolean validFields = false;

        if (txtName.getText().isBlank() ||
                txtName.getText().isBlank() ||
                txtRfc.getText().isBlank() ||
                txtAddress.getText().isBlank() ||
                txtFiscalAddress.getText().isBlank() ||
                txtBusiness.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtEmail.getText().isBlank()){
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
            validFields = true;
        }

        return validFields;

    }

    private void clearInputFields() {
        txtName.clear();
        txtRfc.clear();
        txtAddress.clear();
        txtFiscalAddress.clear();
        txtBusiness.clear();
        txtPhoneNumber.clear();
        txtEmail.clear();

    }

}