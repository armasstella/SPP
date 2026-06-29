package spp.presentation.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.presentation.controller.coordinator.ProjectUpdateController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProjectUpdateControllerTest extends ControllerTestBase {

    private ProjectUpdateController controller;
    private TextField nameTextField;
    private TextArea descriptionTextArea;
    private TextField placesAvailableTextField;

    @BeforeEach
    void setUp() throws Exception {
        controller = new ProjectUpdateController();

        nameTextField = new TextField();
        descriptionTextArea = new TextArea();
        placesAvailableTextField = new TextField();

        injectField("txtName", nameTextField);
        injectField("txtDescription", descriptionTextArea);
        injectField("txtPlacesAvailable", placesAvailableTextField);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = ProjectUpdateController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = ProjectUpdateController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        nameTextField.setText("Proyecto de Energías Renovables");
        descriptionTextArea.setText("Investigación sobre paneles solares");
        placesAvailableTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Nombre vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_nameEmpty() throws Exception {
        nameTextField.setText("");
        descriptionTextArea.setText("Investigación sobre paneles solares");
        placesAvailableTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Descripcion vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_descriptionEmpty() throws Exception {
        nameTextField.setText("Proyecto de Energías Renovables");
        descriptionTextArea.setText("");
        placesAvailableTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Lugares disponibles vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_placesAvailableEmpty() throws Exception {
        nameTextField.setText("Proyecto de Energías Renovables");
        descriptionTextArea.setText("Investigación sobre paneles solares");
        placesAvailableTextField.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Normal: Nombre con longitud minima cumplida, hasValidMinimumLengths retorna true")
    void testHasValidMinimumLengths_nameValid() throws Exception {
        nameTextField.setText("Proyecto");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Nombre corto (menos de 3 caracteres), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_nameTooShort() throws Exception {
        nameTextField.setText("Pr");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: Campos llenos y nombre con longitud valida, areValidFields retorna true")
    void testAreValidFields_allValid() throws Exception {
        nameTextField.setText("Proyecto de Energías Renovables");
        descriptionTextArea.setText("Investigación sobre paneles solares");
        placesAvailableTextField.setText("8");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertTrue(valid);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Campo obligatorio vacio, areValidFields retorna false")
    void testAreValidFields_emptyField() throws Exception {
        nameTextField.setText("Proyecto de Energías Renovables");
        descriptionTextArea.setText("");
        placesAvailableTextField.setText("8");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Alterno: Longitud minima no cumplida, areValidFields retorna false")
    void testAreValidFields_shortLength() throws Exception {
        nameTextField.setText("Pr");
        descriptionTextArea.setText("Investigación sobre paneles solares");
        placesAvailableTextField.setText("8");

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }
}