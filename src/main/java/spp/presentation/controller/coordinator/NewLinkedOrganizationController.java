package spp.presentation.controller.coordinator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.LinkedOrganizationDAO;
import spp.utils.view.inputdata.InputFilter;
import spp.utils.view.label.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.window.ViewNavigator;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtName,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_TITLE);
        InputFilter.applyFormatFilter(txtRfc,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_MORAL_RFC);
        InputFilter.applyFormatFilter(txtAddress,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_ADDRESS);
        InputFilter.applyFormatFilter(txtFiscalAddress,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_ADDRESS);
        InputFilter.applyFormatFilter(txtBusiness,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_BUSINESS);
        InputFilter.applyFormatFilter(txtEmail,
                ViewConstant.PATTERN_EMAIL_CHARS, ViewConstant.MAX_LENGTH_EMAIL);
        InputFilter.applyFormatFilter(txtPhoneNumber,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_PHONE);
    }

    private void setAllLinkedOrganization(LinkedOrganizationDTO linkedOrganizationDTO) {
        linkedOrganizationDTO.setName(txtName.getText().trim());
        linkedOrganizationDTO.setRfc(txtRfc.getText().trim());
        linkedOrganizationDTO.setAddress(txtAddress.getText().trim());
        linkedOrganizationDTO.setFiscalAddress(txtFiscalAddress.getText().trim());
        linkedOrganizationDTO.setBusiness(txtBusiness.getText().trim());
        linkedOrganizationDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        linkedOrganizationDTO.setEmail(txtEmail.getText().trim());
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtName.getText().isBlank() ||
                txtRfc.getText().isBlank() ||
                txtAddress.getText().isBlank() ||
                txtFiscalAddress.getText().isBlank() ||
                txtBusiness.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank() ||
                txtEmail.getText().isBlank()) {

            emptyFields = true;
        }

        return emptyFields;
    }

    private boolean hasValidMinimumLengths() {
        boolean validLengths = false;

        boolean validName = InputFilter.hasMinimumLength(txtName,
                ViewConstant.MIN_LENGTH_NAME);
        boolean validRfc = InputFilter.hasMinimumLength(txtRfc,
                ViewConstant.MIN_LENGTH_MORAL_RFC);
        boolean validAddress = InputFilter.hasMinimumLength(txtAddress,
                ViewConstant.MIN_LENGTH_ADDRESS);
        boolean validFiscalAddress = InputFilter.hasMinimumLength(txtFiscalAddress,
                ViewConstant.MIN_LENGTH_ADDRESS);
        boolean validBusiness = InputFilter.hasMinimumLength(txtBusiness,
                ViewConstant.MIN_LENGTH_CATEGORY);
        boolean validPhone = InputFilter.hasMinimumLength(txtPhoneNumber,
                ViewConstant.MIN_LENGTH_PHONE);

        if (validName && validRfc && validAddress && validFiscalAddress && validBusiness && validPhone) {
            validLengths = true;
        }

        return validLengths;
    }

    private boolean areValidFields() {
        boolean validFields = false;

        if (hasEmptyFields()) {
            StatusLabel.showError(lblStatus, "Completa todos los campos obligatorios.");
        } else {
            if (hasValidMinimumLengths()) {
                validFields = true;
            } else {
                StatusLabel.showError(lblStatus, "La longitud de los campos debe cumplir con el mínimo de caracteres.");
            }
        }

        return validFields;
    }


    @FXML
    private void saveLinkedOrganization(ActionEvent event) {
        if (areValidFields()) {
            LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
            setAllLinkedOrganization(linkedOrganizationDTO);

            if (linkedOrganizationDTO.isValid()) {
                LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();
                try {
                    if (linkedOrganizationDAO.registerLinkedOrganization(linkedOrganizationDTO)) {
                        StatusLabel.showSuccess(lblStatus, "Organización registrada correctamente.");
                        clearInputFields();
                    }
                } catch (DAOException e) {
                    StatusLabel.showError(lblStatus, e.getMessage());
                }
            } else {
                String errorMessages = String.join("\n• ", linkedOrganizationDTO.getErrors());
                StatusLabel.showError(lblStatus, "Corrige los siguientes formatos:\n• " + errorMessages);
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
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
