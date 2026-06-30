package spp.presentation.controller;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InternDTO;
import spp.presentation.controller.coordinator.NewInternController;
import spp.utils.businessconstants.BusinessConstant;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewInternControllerTest extends ControllerTestBase{

    private NewInternController controller;
    private TextField firstNameTextField;
    private TextField secondNameTextField;
    private TextField firstLastNameTextField;
    private TextField secondLastNameTextField;
    private TextField emailTextField;
    private TextField phoneNumberTextField;
    private TextField studentNumberTextField;
    private TextField passwordTextField;
    private ComboBox<String> sexComboBox;
    private RadioButton indigenousYesRadioButton;
    private RadioButton indigenousNoRadioButton;
    private VBox languageDetailVBox;
    private TextField indigenousLanguageTextField;
    private DatePicker birthDatePicker;
    private ComboBox<CourseDTO> courseCodeComboBox;

    @BeforeEach
    void setUp() throws Exception {
        controller = new NewInternController();

        firstNameTextField = new TextField();
        secondNameTextField = new TextField();
        firstLastNameTextField = new TextField();
        secondLastNameTextField = new TextField();
        emailTextField = new TextField();
        phoneNumberTextField = new TextField();
        studentNumberTextField = new TextField();
        passwordTextField = new TextField();
        sexComboBox = new ComboBox<>();
        indigenousYesRadioButton = new RadioButton();
        indigenousNoRadioButton = new RadioButton();
        languageDetailVBox = new VBox();
        indigenousLanguageTextField = new TextField();
        birthDatePicker = new DatePicker();
        courseCodeComboBox = new ComboBox<>();

        injectField("txtFirstName", firstNameTextField);
        injectField("txtSecondName", secondNameTextField);
        injectField("txtFirstLastName", firstLastNameTextField);
        injectField("txtSecondLastName", secondLastNameTextField);
        injectField("txtEmail", emailTextField);
        injectField("txtPhoneNumber", phoneNumberTextField);
        injectField("txtStudentNumber", studentNumberTextField);
        injectField("txtPassword", passwordTextField);
        injectField("cmbSex", sexComboBox);
        injectField("rbYes", indigenousYesRadioButton);
        injectField("rbNo", indigenousNoRadioButton);
        injectField("vbLanguageDetail", languageDetailVBox);
        injectField("txtIndigenousLanguage", indigenousLanguageTextField);
        injectField("dpBirthDate", birthDatePicker);
        injectField("cmbCourseCode", courseCodeComboBox);

        sexComboBox.getItems().addAll("MASCULINO", "FEMENINO");
        indigenousNoRadioButton.setSelected(true);
        CourseDTO dummyCourse = new CourseDTO();
        dummyCourse.setIdCourse(1);
        dummyCourse.setCourseCode(12345);
        courseCodeComboBox.getItems().add(dummyCourse);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = NewInternController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = NewInternController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    private void invokeSetAllIntern(InternDTO internDTO) throws Exception {
        Method method = NewInternController.class.getDeclaredMethod("setAllIntern", InternDTO.class);
        method.setAccessible(true);
        method.invoke(controller, internDTO);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

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
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

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
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

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
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

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
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Numero de estudiante vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_studentNumberEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

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
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Sexo no seleccionado, hasEmptyFields retorna true")
    void testHasEmptyFields_sexNull() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue(null);
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Alterno: Fecha de nacimiento nula, hasEmptyFields retorna true")
    void testHasEmptyFields_birthDateNull() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(null);
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Alterno: Radio Si seleccionado pero lengua indigena vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_indigenousYesButLanguageEmpty() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousYesRadioButton.setSelected(true);
        indigenousNoRadioButton.setSelected(false);
        indigenousLanguageTextField.setText("");
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(11)
    @DisplayName("Flujo Normal: Radio Si seleccionado y lengua indigena llena, hasEmptyFields false")
    void testHasEmptyFields_indigenousYesAndLanguageFilled() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousYesRadioButton.setSelected(true);
        indigenousNoRadioButton.setSelected(false);
        indigenousLanguageTextField.setText("Náhuatl");
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(12)
    @DisplayName("Flujo Alterno: Curso no seleccionado, hasEmptyFields retorna true")
    void testHasEmptyFields_courseNull() throws Exception {
        firstNameTextField.setText("María");
        firstLastNameTextField.setText("González");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(null);

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(13)
    @DisplayName("Flujo Normal: Campos opcionales vacios no afectan hasEmptyFields")
    void testHasEmptyFields_optionalFieldsEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(14)
    @DisplayName("Flujo Normal: Longitudes minimas cumplidas, hasValidMinimumLengths retorna true")
    void testHasValidMinimumLengths_allValid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousYesRadioButton.setSelected(false);
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(15)
    @DisplayName("Flujo Alterno: Primer nombre corto, hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_firstNameTooShort() throws Exception {
        firstNameTextField.setText("Ma");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(16)
    @DisplayName("Flujo Normal: Segundo nombre vacio se ignora, hasValidMinimumLengths true")
    void testHasValidMinimumLengths_secondNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(17)
    @DisplayName("Flujo Alterno: Segundo nombre corto, hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_secondNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Is");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(18)
    @DisplayName("Flujo Alterno: Apellido paterno corto, hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_firstLastNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("Go");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(19)
    @DisplayName("Flujo Normal: Segundo apellido vacio se ignora, hasValidMinimumLengths true")
    void testHasValidMinimumLengths_secondLastNameEmpty() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(20)
    @DisplayName("Flujo Alterno: Segundo apellido corto, hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_secondLastNameTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Ma");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(21)
    @DisplayName("Flujo Alterno: Numero de estudiante corto, hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_studentNumberTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S1234567");
        passwordTextField.setText("Mar1a$2024");
        indigenousNoRadioButton.setSelected(true);

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(22)
    @DisplayName("Flujo Alterno: Si habla lengua indigena y la lengua tiene menos de 4 caracteres, hasValidMinimumLengths false")
    void testHasValidMinimumLengths_indigenousLanguageTooShort() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousYesRadioButton.setSelected(true);
        indigenousNoRadioButton.setSelected(false);
        indigenousLanguageTextField.setText("Nah");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(23)
    @DisplayName("Flujo Normal: Si habla lengua indigena y la lengua tiene longitud suficiente, hasValidMinimumLengths true")
    void testHasValidMinimumLengths_indigenousLanguageValid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        indigenousYesRadioButton.setSelected(true);
        indigenousNoRadioButton.setSelected(false);
        indigenousLanguageTextField.setText("Náhuatl");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(24)
    @DisplayName("Flujo Normal: Campos llenos y longitudes validas, areValidFields retorna true")
    void testAreValidFields_allValid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertTrue(valid);
    }

    @Test
    @Order(25)
    @DisplayName("Flujo Alterno: Campo obligatorio vacio, areValidFields retorna false")
    void testAreValidFields_emptyField() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(26)
    @DisplayName("Flujo Alterno: Longitud minima no cumplida, areValidFields retorna false")
    void testAreValidFields_shortLength() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("Go");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(27)
    @DisplayName("Flujo Normal: InternDTO valido con email, password y student number correctos")
    void testDtoValid_allFieldsOk() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        InternDTO internDTO = new InternDTO();
        invokeSetAllIntern(internDTO);

        assertTrue(internDTO.isValid());
        assertTrue(internDTO.getErrors().isEmpty());
        assertEquals("maria.gonzalez@uv.mx", internDTO.getEmail());
        assertEquals("Mar1a$2024", internDTO.getPassword());
        assertEquals("S12345678", internDTO.getStudentNumber());
    }

    @Test
    @Order(28)
    @DisplayName("Flujo Alterno: Email invalido, InternDTO invalido")
    void testDtoInvalid_emailInvalid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        InternDTO internDTO = new InternDTO();
        invokeSetAllIntern(internDTO);

        assertFalse(internDTO.isValid());
        assertEquals(1, internDTO.getErrors().size());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertNull(internDTO.getEmail());
    }

    @Test
    @Order(29)
    @DisplayName("Flujo Alterno: Contrasena invalida (sin mayuscula), InternDTO invalido")
    void testDtoInvalid_passwordNoUpperCase() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        InternDTO internDTO = new InternDTO();
        invokeSetAllIntern(internDTO);

        assertFalse(internDTO.isValid());
        assertEquals(1, internDTO.getErrors().size());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(internDTO.getPassword());
    }

    @Test
    @Order(30)
    @DisplayName("Flujo Alterno: Contrasena invalida (sin caracter especial), InternDTO invalido")
    void testDtoInvalid_passwordNoSpecialChar() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S12345678");
        passwordTextField.setText("Mar1a2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        InternDTO internDTO = new InternDTO();
        invokeSetAllIntern(internDTO);

        assertFalse(internDTO.isValid());
        assertEquals(1, internDTO.getErrors().size());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertNull(internDTO.getPassword());
    }

    @Test
    @Order(31)
    @DisplayName("Flujo Alterno: Numero de estudiante invalido, InternDTO invalido")
    void testDtoInvalid_studentNumberInvalid() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@uv.mx");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S1234567");
        passwordTextField.setText("Mar1a$2024");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        InternDTO internDTO = new InternDTO();
        invokeSetAllIntern(internDTO);

        assertFalse(internDTO.isValid());
        assertEquals(1, internDTO.getErrors().size());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER));
        assertNull(internDTO.getStudentNumber());
    }

    @Test
    @Order(32)
    @DisplayName("Flujo de Acumulacion: Email, password y student number invalidos, InternDTO acumula tres errores")
    void testDtoInvalid_accumulateErrors() throws Exception {
        firstNameTextField.setText("María");
        secondNameTextField.setText("Isabel");
        firstLastNameTextField.setText("González");
        secondLastNameTextField.setText("Martínez");
        emailTextField.setText("maria.gonzalez@");
        phoneNumberTextField.setText("2281234567");
        studentNumberTextField.setText("S1234");
        passwordTextField.setText("mar1a");
        sexComboBox.setValue("FEMENINO");
        birthDatePicker.setValue(LocalDate.of(2000, 1, 15));
        indigenousNoRadioButton.setSelected(true);
        courseCodeComboBox.setValue(courseCodeComboBox.getItems().get(0));

        InternDTO internDTO = new InternDTO();
        invokeSetAllIntern(internDTO);

        assertFalse(internDTO.isValid());
        assertEquals(3, internDTO.getErrors().size());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER));
    }
}