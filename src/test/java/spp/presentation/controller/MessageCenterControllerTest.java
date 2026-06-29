package spp.presentation.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.presentation.controller.user.MessageCenterController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageCenterControllerTest extends ControllerTestBase {

    private MessageCenterController controller;
    private TextField recipientTextField;
    private TextField subjectTextField;
    private TextArea bodyTextArea;

    @BeforeEach
    void setUp() throws Exception {
        controller = new MessageCenterController();

        recipientTextField = new TextField();
        subjectTextField = new TextField();
        bodyTextArea = new TextArea();

        injectField("txtRecipient", recipientTextField);
        injectField("txtSubject", subjectTextField);
        injectField("taBody", bodyTextArea);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = MessageCenterController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = MessageCenterController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        recipientTextField.setText("maria.gonzalez@uv.mx");
        subjectTextField.setText("Reunión de seguimiento");
        bodyTextArea.setText("Por favor confirme su asistencia a la reunión del viernes.");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Destinatario vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_recipientEmpty() throws Exception {
        recipientTextField.setText("");
        subjectTextField.setText("Reunión de seguimiento");
        bodyTextArea.setText("Por favor confirme su asistencia a la reunión del viernes.");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Asunto vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_subjectEmpty() throws Exception {
        recipientTextField.setText("maria.gonzalez@uv.mx");
        subjectTextField.setText("");
        bodyTextArea.setText("Por favor confirme su asistencia a la reunión del viernes.");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Cuerpo del mensaje vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_bodyEmpty() throws Exception {
        recipientTextField.setText("maria.gonzalez@uv.mx");
        subjectTextField.setText("Reunión de seguimiento");
        bodyTextArea.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }
}