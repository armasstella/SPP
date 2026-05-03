package spp.presentation.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import spp.businesslogic.dto.InstructorDTO;
import spp.utils.view.ViewNavigator;

public class NewInstructorController {

    @FXML private TextField txtFirstName;
    @FXML private TextField txtSecondName;
    @FXML private TextField txtFirstLastName;
    @FXML private TextField txtSecondLastName;
    @FXML private TextField txtEmail;
    @FXML private TextField txtPhoneNumber;
    @FXML private TextField txtPersonalNumber;
    @FXML private TextField txtPassword;
    @FXML private Label lblStatus;

    private InstructorDTO buildInstructorDTO() {
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setFirstName(txtFirstName.getText().trim());
        instructorDTO.setSecondName(txtSecondName.getText().trim());
        instructorDTO.setFirstLastName(txtFirstLastName.getText().trim());
        instructorDTO.setSecondLastName(txtSecondLastName.getText().trim());
        instructorDTO.setEmail(txtEmail.getText().trim());
        instructorDTO.setPhoneNumber(txtPhoneNumber.getText().trim());
        instructorDTO.setPersonalNumber(txtPersonalNumber.getText().trim());
        instructorDTO.setPassword(txtPassword.getText().trim());
        return instructorDTO;
    }

    @FXML
    private void saveInstructor() {

    }

    @FXML
    private void cancel(ActionEvent event) {
        ViewNavigator.loadView("/spp/presentation/view/AdminMenuView.fxml",
                "Menú Administrador", event);
    }

}
