package spp.presentation.controller;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.presentation.controller.coordinator.NewProjectManagerController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewProjectManagerControllerTest extends ControllerTestBase {

    private NewProjectManagerController controller;
    private TextField firstNameTextField;
    private TextField secondNameTextField;
    private TextField firstLastNameTextField;
    private TextField secondLastNameTextField;
    private TextField responsibilityTextField;
    private TextField roleTextField;
    private TextField phoneNumberTextField;
    private ComboBox<LinkedOrganizationDTO> linkedOrganizationsComboBox;

    @BeforeEach
    void setUp() throws Exception {
        controller = new NewProjectManagerController();

        firstNameTextField = new TextField();
        secondNameTextField = new TextField();
        firstLastNameTextField = new TextField();
        secondLastNameTextField = new TextField();
        responsibilityTextField = new TextField();
        roleTextField = new TextField();
        phoneNumberTextField = new TextField();
        linkedOrganizationsComboBox = new ComboBox<>();

        injectField("txtFirstName", firstNameTextField);
        injectField("txtSecondName", secondNameTextField);
        injectField("txtFirstLastName", firstLastNameTextField);
        injectField("txtSecondLastName", secondLastNameTextField);
        injectField("txtResponsibility", responsibilityTextField);
        injectField("txtRole", roleTextField);
        injectField("txtPhoneNumber", phoneNumberTextField);
        injectField("cmbLinkedOrganizations", linkedOrganizationsComboBox);

        LinkedOrganizationDTO dummyOrganization = new LinkedOrganizationDTO();
        dummyOrganization.setId(1);
        dummyOrganization.setName("Organización Ejemplo");
        linkedOrganizationsComboBox.getItems().add(dummyOrganization);
        linkedOrganizationsComboBox.setValue(dummyOrganization);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = NewProjectManagerController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = NewProjectManagerController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    private void invokeSetAllProjectManager(ProjectManagerDTO projectManagerDTO) throws Exception {
        Method method = NewProjectManagerController.class.getDeclaredMethod("setAllProjectManager", ProjectManagerDTO.class);
        method.setAccessible(true);
        method.invoke(controller, projectManagerDTO);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Primer nombre vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_firstNameEmpty() throws Exception {
        firstNameTextField.setText("");
        firstLastNameTextField.setText("González");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Apellido paterno vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_firstLastNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Responsabilidad vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_responsibilityEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        responsibilityTextField.setText("");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Rol vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_roleEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("");
        phoneNumberTextField.setText("2281234567");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Telefono vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_phoneEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: Campos opcionales (segundo nombre y segundo apellido) vacios no afectan hasEmptyFields")
    void testHasEmptyFields_optionalFieldsEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: ComboBox de organizaciones no afecta hasEmptyFields")
    void testHasEmptyFields_linkedOrganizationNull() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");
        linkedOrganizationsComboBox.setValue(null);

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Normal: Longitudes minimas cumplidas, hasValidMinimumLengths retorna true")
    void testHasValidMinimumLengths_allValid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Alterno: Primer nombre corto (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_firstNameTooShort() throws Exception {
        firstNameTextField.setText("Ma");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(11)
    @DisplayName("Flujo Normal: Segundo nombre vacio se ignora, hasValidMinimumLengths true")
    void testHasValidMinimumLengths_secondNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(12)
    @DisplayName("Flujo Alterno: Segundo nombre corto (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_secondNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Is");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(13)
    @DisplayName("Flujo Alterno: Apellido paterno corto (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_firstLastNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("Go");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(14)
    @DisplayName("Flujo Normal: Segundo apellido vacio se ignora, hasValidMinimumLengths true")
    void testHasValidMinimumLengths_secondLastNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(15)
    @DisplayName("Flujo Alterno: Segundo apellido corto (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_secondLastNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Ma");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(16)
    @DisplayName("Flujo Alterno: Responsabilidad corta (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_responsibilityTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Co");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(17)
    @DisplayName("Flujo Alterno: Rol corto (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_roleTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Ge");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(18)
    @DisplayName("Flujo Alterno: Telefono corto (menos de 10 digitos), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_phoneTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("228123456");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(19)
    @DisplayName("Flujo Normal: Campos llenos y longitudes validas, areValidFields retorna true")
    void testAreValidFields_allValid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertTrue(valid);
    }

    @Test
    @Order(20)
    @DisplayName("Flujo Alterno: Campo obligatorio vacio, areValidFields retorna false")
    void testAreValidFields_emptyField() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(21)
    @DisplayName("Flujo Alterno: Longitud minima no cumplida, areValidFields retorna false")
    void testAreValidFields_shortLength() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("Go");
        secondLastNameTextField.setText("Martínez");
        responsibilityTextField.setText("Coordinación de proyectos");
        roleTextField.setText("Gerente");
        phoneNumberTextField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }
}