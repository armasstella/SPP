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
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.presentation.controller.coordinator.NewLinkedOrganizationController;
import spp.utils.businessconstants.BusinessConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewLinkedOrganizationControllerTest extends ControllerTestBase {

    private NewLinkedOrganizationController controller;
    private TextField nameField;
    private TextField rfcField;
    private TextField addressField;
    private TextField fiscalAddressField;
    private TextField businessField;
    private TextField phoneNumberField;
    private TextField emailField;

    @BeforeEach
    void setUp() throws Exception {
        controller = new NewLinkedOrganizationController();

        nameField = new TextField();
        rfcField = new TextField();
        addressField = new TextField();
        fiscalAddressField = new TextField();
        businessField = new TextField();
        phoneNumberField = new TextField();
        emailField = new TextField();

        injectField("txtName", nameField);
        injectField("txtRfc", rfcField);
        injectField("txtAddress", addressField);
        injectField("txtFiscalAddress", fiscalAddressField);
        injectField("txtBusiness", businessField);
        injectField("txtPhoneNumber", phoneNumberField);
        injectField("txtEmail", emailField);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = NewLinkedOrganizationController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = NewLinkedOrganizationController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    private void invokeSetAllLinkedOrganization(LinkedOrganizationDTO linkedOrganizationDTO) throws Exception {
        Method method = NewLinkedOrganizationController.class.getDeclaredMethod("setAllLinkedOrganization", LinkedOrganizationDTO.class);
        method.setAccessible(true);
        method.invoke(controller, linkedOrganizationDTO);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Nombre vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_nameEmpty() throws Exception {
        nameField.setText("");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: RFC vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_rfcEmpty() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Direccion vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_addressEmpty() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Direccion fiscal vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_fiscalAddressEmpty() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Giro vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_businessEmpty() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Telefono vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_phoneEmpty() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("");
        emailField.setText("contacto@constructorahernandez.com");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Email vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_emailEmpty() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Normal: Longitudes minimas cumplidas, hasValidMinimumLengths retorna true")
    void testHasValidMinimumLengths_allValid() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Alterno: Nombre corto (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_nameTooShort() throws Exception {
        nameField.setText("Co");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(11)
    @DisplayName("Flujo Alterno: RFC corto (menos de 12), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_rfcTooShort() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XY");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(12)
    @DisplayName("Flujo Alterno: Direccion corta (menos de 6), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_addressTooShort() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Av.");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(13)
    @DisplayName("Flujo Alterno: Direccion fiscal corta (menos de 6), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_fiscalAddressTooShort() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Cll.");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(14)
    @DisplayName("Flujo Alterno: Giro corto (menos de 3), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_businessTooShort() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Co");
        phoneNumberField.setText("2281234567");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(15)
    @DisplayName("Flujo Alterno: Telefono corto (menos de 10), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_phoneTooShort() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("228123456");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(16)
    @DisplayName("Flujo Normal: Campos llenos y longitudes validas, areValidFields retorna true")
    void testAreValidFields_allValid() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertTrue(valid);
    }

    @Test
    @Order(17)
    @DisplayName("Flujo Alterno: Campo obligatorio vacio, areValidFields retorna false")
    void testAreValidFields_emptyField() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(18)
    @DisplayName("Flujo Alterno: Longitud minima no cumplida, areValidFields retorna false")
    void testAreValidFields_shortLength() throws Exception {
        nameField.setText("Co");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(19)
    @DisplayName("Flujo Normal: LinkedOrganizationDTO valido con email y RFC correctos")
    void testDtoValid_emailAndRfcOk() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        invokeSetAllLinkedOrganization(linkedOrganizationDTO);

        assertTrue(linkedOrganizationDTO.isValid());
        assertTrue(linkedOrganizationDTO.getErrors().isEmpty());
        assertEquals("HPC830101XYZ", linkedOrganizationDTO.getRfc());
        assertEquals("contacto@constructorahernandez.com", linkedOrganizationDTO.getEmail());
    }

    @Test
    @Order(20)
    @DisplayName("Flujo Alterno: Email invalido (sin dominio), LinkedOrganizationDTO invalido")
    void testDtoInvalid_emailInvalid() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XYZ");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez");

        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        invokeSetAllLinkedOrganization(linkedOrganizationDTO);

        assertFalse(linkedOrganizationDTO.isValid());
        assertEquals(1, linkedOrganizationDTO.getErrors().size());
        assertTrue(linkedOrganizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(linkedOrganizationDTO.getEmail());
    }

    @Test
    @Order(21)
    @DisplayName("Flujo Alterno: RFC invalido (formato incorrecto), LinkedOrganizationDTO invalido")
    void testDtoInvalid_rfcInvalid() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XY");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez.com");

        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        invokeSetAllLinkedOrganization(linkedOrganizationDTO);

        assertFalse(linkedOrganizationDTO.isValid());
        assertEquals(1, linkedOrganizationDTO.getErrors().size());
        assertTrue(linkedOrganizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_RFC));
        assertNull(linkedOrganizationDTO.getRfc());
    }

    @Test
    @Order(22)
    @DisplayName("Flujo de Acumulacion: Email y RFC invalidos, LinkedOrganizationDTO acumula ambos errores")
    void testDtoInvalid_accumulateErrors() throws Exception {
        nameField.setText("Constructora Hernández Pérez");
        rfcField.setText("HPC830101XY");
        addressField.setText("Avenida Universidad 1500, Colonia Centro, Xalapa, Veracruz");
        fiscalAddressField.setText("Calle Revolución 700, Colonia Reforma, Xalapa, Veracruz");
        businessField.setText("Construcción");
        phoneNumberField.setText("2281234567");
        emailField.setText("contacto@constructorahernandez");

        LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
        invokeSetAllLinkedOrganization(linkedOrganizationDTO);

        assertFalse(linkedOrganizationDTO.isValid());
        assertEquals(2, linkedOrganizationDTO.getErrors().size());
        assertTrue(linkedOrganizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertTrue(linkedOrganizationDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_RFC));
    }
}