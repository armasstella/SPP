package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.dataaccess.dao.LinkedOrganizationDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;

public class LinkedOrganizationController implements Initializable {

    @FXML private TextField txtName;
    @FXML private TextField txtRfc;
    @FXML private TextField txtAddress;
    @FXML private TextField txtFiscalAddress;
    @FXML private TextField txtBusiness;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;

    @FXML private Label lblStatus;

    private final LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clearStatus();
    }

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

        clearStatus();
        if (validateRegistrationInputs()) {
            return;
        }

        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        setAllLinkedOrganization(event, linkedOrganizationDTO);

        try {
            if (linkedOrganizationDAO.addLinkedOrganization(linkedOrganizationDTO)) {
                showSuccess("Organización registrada correctamente.");
                clearInputFields();
            }
        } catch (DAOException e) {
            AppLogger.logError(e);
            showError(e.getMessage());
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/CoordinatorMenuView.fxml", "Menú Coordinador", event);
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
            showError("Completa todos los campos obligatorios.");
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
}