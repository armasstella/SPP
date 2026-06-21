package spp.presentation.controller.coordinator;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.LinkedOrganizationDAO;
import spp.utils.logger.AppLogger;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewNavigator;
import java.net.URL;
import java.util.ResourceBundle;


public class NewLinkedOrganizationController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtName;
    @FXML private TextField txtRfc;
    @FXML private TextField txtAddress;
    @FXML private TextField txtFiscalAddress;
    @FXML private TextField txtBusiness;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    private final LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFilter(txtName, InputFilter.NAME_PATTERN, 40);
        InputFilter.applyFilter(txtRfc, InputFilter.NAME_PATTERN, 13);
        InputFilter.applyFilter(txtAddress, InputFilter.ALPHANUMERIC_PATTERN, 40);
        InputFilter.applyFilter(txtFiscalAddress, InputFilter.ALPHANUMERIC_PATTERN, 40);
        InputFilter.applyFilter(txtBusiness, InputFilter.ALPHANUMERIC_PATTERN, 100);
        InputFilter.applyFilter(txtEmail, InputFilter.EMAIL_CHARS_PATTERN, 100);
        InputFilter.applyFilter(txtPhoneNumber, InputFilter.NUMERIC_PATTERN, 10);

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
        if (validateRegistrationInputs()) {
            return;
        }

        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        setAllLinkedOrganization(event, linkedOrganizationDTO);

        try {
            if (linkedOrganizationDAO.registerLinkedOrganization(linkedOrganizationDTO)) {
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
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
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