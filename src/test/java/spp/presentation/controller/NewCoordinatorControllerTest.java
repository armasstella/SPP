package spp.presentation.controller;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.businesslogic.dto.CoordinatorDTO;
import spp.presentation.controller.admin.NewCoordinatorController;
import spp.utils.businessconstants.BusinessConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewCoordinatorControllerTest extends ControllerTestBase{

    private NewCoordinatorController controller;
    private TextField firstNameTextField;
    private TextField secondNameTextField;
    private TextField firstLastNameTextField;
    private TextField secondLastNameTextField;
    private TextField emailTextField;
    private TextField phoneNumberTextField;
    private TextField personalNumberTextField;
    private TextField passwordTextField;

    @BeforeEach
    void setUp() throws Exception {
        controller = new NewCoordinatorController();

        firstNameTextField = new TextField();
        secondNameTextField = new TextField();
        firstLastNameTextField = new TextField();
        secondLastNameTextField = new TextField();
        emailTextField = new TextField();
        phoneNumberTextField = new TextField();
        personalNumberTextField = new TextField();
        passwordTextField = new TextField();

        injectField("txtFirstName", firstNameTextField);
        injectField("txtSecondName", secondNameTextField);
        injectField("txtFirstLastName", firstLastNameTextField);
        injectField("txtSecondLastName", secondLastNameTextField);
        injectField("txtEmail", emailTextField);
        injectField("txtPhoneNumber", phoneNumberTextField);
        injectField("txtPersonalNumber", personalNumberTextField);
        injectField("txtPassword", passwordTextField);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = NewCoordinatorController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = NewCoordinatorController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    private void invokeSetAllCoordinator(CoordinatorDTO coordinatorDTO) throws Exception {
        Method method = NewCoordinatorController.class.getDeclaredMethod("setAllCoordinator", CoordinatorDTO.class);
        method.setAccessible(true);
        method.invoke(controller, coordinatorDTO);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Primer nombre vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_firstNameEmpty() throws Exception {
        firstNameTextField.setText("");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Apellido paterno vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_firstLastNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Email vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_emailEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Telefono vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_phoneEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Numero personal vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_personalNumberEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("");
        passwordTextField.setText("Mar1a$2024");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Contrasena vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_passwordEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: Campos opcionales vacios no afectan hasEmptyFields")
    void testHasEmptyFields_optionalFieldsEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

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
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Alterno: Primer nombre corto (2 caracteres), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_firstNameTooShort() throws Exception {
        firstNameTextField.setText("Ma");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(11)
    @DisplayName("Flujo Normal: Segundo nombre vacio (opcional) se ignora, hasValidMinimumLengths true")
    void testHasValidMinimumLengths_secondNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(12)
    @DisplayName("Flujo Alterno: Segundo nombre corto (2 caracteres), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_secondNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Is");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(13)
    @DisplayName("Flujo Alterno: Apellido paterno corto (2 caracteres), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_firstLastNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("Go");
        secondLastNameTextField.setText("Martínez");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(14)
    @DisplayName("Flujo Normal: Segundo apellido vacio (opcional) se ignora, hasValidMinimumLengths true")
    void testHasValidMinimumLengths_secondLastNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(15)
    @DisplayName("Flujo Alterno: Segundo apellido corto (2 caracteres), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_secondLastNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Ma");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(16)
    @DisplayName("Flujo Alterno: Numero personal corto (4 digitos), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_personalNumberTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        personalNumberTextField.setText("1234");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(17)
    @DisplayName("Flujo Alterno: Contrasena corta (7 caracteres), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_passwordTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(18)
    @DisplayName("Flujo Normal: Campos llenos y longitudes validas, areValidFields retorna true")
    void testAreValidFields_allValid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertTrue(valid);
    }

    @Test
    @Order(19)
    @DisplayName("Flujo Alterno: Campo obligatorio vacio, areValidFields retorna false")
    void testAreValidFields_emptyField() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(20)
    @DisplayName("Flujo Alterno: Longitud minima no cumplida, areValidFields retorna false")
    void testAreValidFields_shortLength() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("Go");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(21)
    @DisplayName("Flujo Normal: CoordinatorDTO valido con email y contrasena correctos")
    void testDtoValid_emailAndPasswordOk() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        invokeSetAllCoordinator(coordinatorDTO);

        assertTrue(coordinatorDTO.isValid());
        assertTrue(coordinatorDTO.getErrors().isEmpty());
        assertEquals("maria.gonzalez@uv.mx", coordinatorDTO.getEmail());
        assertEquals("Mar1a$2024", coordinatorDTO.getPassword());
    }

    @Test
    @Order(22)
    @DisplayName("Flujo Alterno: Email sin dominio, CoordinatorDTO invalido")
    void testDtoInvalid_emailNoDomain() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a$2024");

        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        invokeSetAllCoordinator(coordinatorDTO);

        assertFalse(coordinatorDTO.isValid());
        assertEquals(1, coordinatorDTO.getErrors().size());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(coordinatorDTO.getEmail());
    }

    @Test
    @Order(23)
    @DisplayName("Flujo Alterno: Contrasena sin mayuscula, CoordinatorDTO invalido")
    void testDtoInvalid_passwordNoUpperCase() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("mar1a$2024");

        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        invokeSetAllCoordinator(coordinatorDTO);

        assertFalse(coordinatorDTO.isValid());
        assertEquals(1, coordinatorDTO.getErrors().size());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(coordinatorDTO.getPassword());
    }

    @Test
    @Order(24)
    @DisplayName("Flujo Alterno: Contrasena sin caracter especial, CoordinatorDTO invalido")
    void testDtoInvalid_passwordNoSpecialChar() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("Mar1a2024");

        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        invokeSetAllCoordinator(coordinatorDTO);

        assertFalse(coordinatorDTO.isValid());
        assertEquals(1, coordinatorDTO.getErrors().size());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(coordinatorDTO.getPassword());
    }

    @Test
    @Order(25)
    @DisplayName("Flujo de Acumulacion: Email y contrasena invalidos, CoordinatorDTO acumula ambos errores")
    void testDtoInvalid_accumulateEmailAndPasswordErrors() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@");
        phoneNumberTextField.setText("2281234567");
        personalNumberTextField.setText("12345");
        passwordTextField.setText("mar1a");

        CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
        invokeSetAllCoordinator(coordinatorDTO);

        assertFalse(coordinatorDTO.isValid());
        assertEquals(2, coordinatorDTO.getErrors().size());
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertTrue(coordinatorDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
    }
}