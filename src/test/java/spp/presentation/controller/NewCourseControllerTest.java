package spp.presentation.controller;

import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.presentation.controller.coordinator.NewCourseController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewCourseControllerTest extends ControllerTestBase {

    private NewCourseController controller;
    private TextField courseCodeTextField;
    private TextField termTextField;
    private ComboBox<String> schoolBlockComboBox;
    private ComboBox<String> sectionComboBox;
    private ComboBox<InstructorDTO> instructorComboBox;
    private TextField capacityTextField;
    private TextArea courseDetailsTextArea;

    @BeforeEach
    void setUp() throws Exception {
        controller = new NewCourseController();

        courseCodeTextField = new TextField();
        termTextField = new TextField();
        schoolBlockComboBox = new ComboBox<>();
        sectionComboBox = new ComboBox<>();
        instructorComboBox = new ComboBox<>();
        capacityTextField = new TextField();
        courseDetailsTextArea = new TextArea();

        injectField("txtCourseCode", courseCodeTextField);
        injectField("txtTerm", termTextField);
        injectField("cmbSchoolBlock", schoolBlockComboBox);
        injectField("cmbSection", sectionComboBox);
        injectField("cmbInstructor", instructorComboBox);
        injectField("txtCapacity", capacityTextField);
        injectField("taCourseDetails", courseDetailsTextArea);

        schoolBlockComboBox.getItems().addAll("1", "2", "3");
        sectionComboBox.getItems().addAll("1", "2", "3", "4", "5");
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = NewCourseController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = NewCourseController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    private void invokeSetAllCourse(CourseDTO courseDTO) throws Exception {
        Method method = NewCourseController.class.getDeclaredMethod("setAllCourse", CourseDTO.class);
        method.setAccessible(true);
        method.invoke(controller, courseDTO);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: CourseCode vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_courseCodeEmpty() throws Exception {
        courseCodeTextField.setText("");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Term vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_termEmpty() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: SchoolBlock no seleccionado, hasEmptyFields retorna true")
    void testHasEmptyFields_schoolBlockNull() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue(null);
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Section no seleccionado, hasEmptyFields retorna true")
    void testHasEmptyFields_sectionNull() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue(null);
        capacityTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Capacity vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_capacityEmpty() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: CourseDetails vacio no afecta hasEmptyFields")
    void testHasEmptyFields_courseDetailsEmpty() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");
        courseDetailsTextArea.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: Instructor no seleccionado no afecta hasEmptyFields")
    void testHasEmptyFields_instructorNull() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");
        instructorComboBox.setValue(null);

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Normal: Longitudes minimas cumplidas, hasValidMinimumLengths retorna true")
    void testHasValidMinimumLengths_allValid() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Alterno: CourseCode menor a 5 digitos, hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_courseCodeTooShort() throws Exception {
        courseCodeTextField.setText("1234");
        termTextField.setText("FEBRERO - JULIO 26");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(11)
    @DisplayName("Flujo Alterno: Term menor a 10 caracteres, hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_termTooShort() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEB-26");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(12)
    @DisplayName("Flujo Normal: Campos llenos y longitudes validas, areValidFields retorna true")
    void testAreValidFields_allValid() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertTrue(valid);
    }

    @Test
    @Order(13)
    @DisplayName("Flujo Alterno: Campo obligatorio vacio, areValidFields retorna false")
    void testAreValidFields_emptyField() throws Exception {
        courseCodeTextField.setText("12345");
        termTextField.setText("");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(14)
    @DisplayName("Flujo Alterno: Longitud minima no cumplida, areValidFields retorna false")
    void testAreValidFields_shortLength() throws Exception {
        courseCodeTextField.setText("1234");
        termTextField.setText("FEBRERO - JULIO 26");
        schoolBlockComboBox.setValue("1");
        sectionComboBox.setValue("1");
        capacityTextField.setText("8");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }
}