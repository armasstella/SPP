package spp.presentation.controller.coordinator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.dao.ProjectManagerDAO;
import spp.utils.view.InputFilter;
import spp.utils.view.StatusLabel;
import spp.utils.view.ViewConstant;
import spp.utils.view.ViewNavigator;

import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectManagerController implements Initializable {

    @FXML private Label lblStatus;
    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtResponsibility;
    @FXML private TextField txtRole;
    @FXML private TextField txtPhoneNumber;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpFields();
    }

    private void setUpFields() {
        InputFilter.applyFormatFilter(txtFirstName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtSecondName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtFirstLastName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtSecondLastName,
                ViewConstant.PATTERN_ALPHABETIC, ViewConstant.MAX_LENGTH_NAME_PART);
        InputFilter.applyFormatFilter(txtResponsibility,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_CATEGORY);
        InputFilter.applyFormatFilter(txtRole,
                ViewConstant.PATTERN_ALPHANUMERIC, ViewConstant.MAX_LENGTH_CATEGORY);
        InputFilter.applyFormatFilter(txtPhoneNumber,
                ViewConstant.PATTERN_NUMERIC, ViewConstant.MAX_LENGTH_PHONE);
    }

    private void setAllProjectManager(ProjectManagerDTO projectManagerDTO) {
        projectManagerDTO.setFirstName(txtFirstName.getText());
        projectManagerDTO.setSecondName(txtSecondName.getText());
        projectManagerDTO.setFirstLastName(txtFirstLastName.getText());
        projectManagerDTO.setSecondLastName(txtSecondLastName.getText());
        projectManagerDTO.setRole(txtRole.getText());
        projectManagerDTO.setResponsibility(txtResponsibility.getText());
        projectManagerDTO.setPhoneNumber(txtPhoneNumber.getText());
    }

    private boolean hasEmptyFields() {
        boolean emptyFields = false;

        if (txtFirstName.getText().isBlank() ||
                txtFirstLastName.getText().isBlank() ||
                txtResponsibility.getText().isBlank() ||
                txtRole.getText().isBlank() ||
                txtPhoneNumber.getText().isBlank()) {

            emptyFields = true;
        }

        return emptyFields;
    }

    private boolean hasValidMinimumLengths() {
        boolean validLengths = false;

        boolean validFirstName = InputFilter.hasMinimumLength(txtFirstName,
                ViewConstant.MIN_LENGTH_NAME);
        boolean validFirstLastName = InputFilter.hasMinimumLength(txtFirstLastName,
                ViewConstant.MIN_LENGTH_NAME);
        boolean validResponsibility = InputFilter.hasMinimumLength(txtResponsibility,
                ViewConstant.MIN_LENGTH_CATEGORY);
        boolean validRole = InputFilter.hasMinimumLength(txtRole,
                ViewConstant.MIN_LENGTH_CATEGORY);
        boolean validPhone = InputFilter.hasMinimumLength(txtPhoneNumber,
                ViewConstant.MIN_LENGTH_PHONE);

        boolean validSecondName = true;
        if (!txtSecondName.getText().isBlank()) {
            validSecondName = InputFilter.hasMinimumLength(txtSecondName,
                    ViewConstant.MIN_LENGTH_NAME);
        }

        boolean validSecondLastName = true;
        if (!txtSecondLastName.getText().isBlank()) {
            validSecondLastName = InputFilter.hasMinimumLength(txtSecondLastName,
                    ViewConstant.MIN_LENGTH_NAME);
        }

        if (validFirstName && validFirstLastName && validResponsibility && validRole &&
                validPhone && validSecondName && validSecondLastName) {
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
    private void saveProjectManager(ActionEvent event) {
        if (areValidFields()) {
            ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
            setAllProjectManager(projectManagerDTO);

            ProjectManagerDAO projectManagerDAO  = new ProjectManagerDAO();
            try {
                if (projectManagerDAO.registerProjectManager(projectManagerDTO)) {
                    StatusLabel.showSuccess(lblStatus, "Encargado de proyecto registrado correctamente.");
                    clearInputFields();
                }
            } catch (DAOException e) {
                StatusLabel.showError(lblStatus, "No se pudo registrar el encargado.");
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/coordinator/CoordinatorMenuView.fxml",
                "Menú Coordinador", event);
    }

    private void clearInputFields() {
        txtFirstName.clear();
        txtSecondName.clear();
        txtFirstLastName.clear();
        txtSecondLastName.clear();
        txtResponsibility.clear();
        txtRole.clear();
        txtPhoneNumber.clear();
    }
}
