package spp.presentation.controller;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.presentation.controller.intern.ActivityRegistrationController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityRegistrationControllerTest extends ControllerTestBase {

    private ActivityRegistrationController controller;
    private TextField titleTextField;
    private TextArea descriptionTextArea;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TextField estimatedTimeTextField;
    private TextField effectiveTimeTextField;
    private TextField progressTextField;
    private TextArea observationsTextArea;

    @BeforeEach
    void setUp() throws Exception {
        controller = new ActivityRegistrationController();

        titleTextField = new TextField();
        descriptionTextArea = new TextArea();
        startDatePicker = new DatePicker();
        endDatePicker = new DatePicker();
        estimatedTimeTextField = new TextField();
        effectiveTimeTextField = new TextField();
        progressTextField = new TextField();
        observationsTextArea = new TextArea();

        injectField("txtTitle", titleTextField);
        injectField("taDescription", descriptionTextArea);
        injectField("dpStartDate", startDatePicker);
        injectField("dpEndDate", endDatePicker);
        injectField("txtEstimatedTime", estimatedTimeTextField);
        injectField("txtEffectiveTime", effectiveTimeTextField);
        injectField("txtProgress", progressTextField);
        injectField("taObservations", observationsTextArea);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = ActivityRegistrationController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = ActivityRegistrationController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("75");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Titulo vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_titleEmpty() throws Exception {
        titleTextField.setText("");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("75");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Descripcion vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_descriptionEmpty() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("75");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Fecha inicio nula, hasEmptyFields retorna true")
    void testHasEmptyFields_startDateNull() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(null);
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("75");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Fecha fin nula, hasEmptyFields retorna true")
    void testHasEmptyFields_endDateNull() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(null);
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("75");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Tiempo estimado vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_estimatedTimeEmpty() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("75");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Tiempo efectivo vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_effectiveTimeEmpty() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("");
        progressTextField.setText("75");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Progreso vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_progressEmpty() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("");
        observationsTextArea.setText("Se requiere coordinación con el líder comunitario");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Alterno: Observaciones vacias, hasEmptyFields retorna true")
    void testHasEmptyFields_observationsEmpty() throws Exception {
        titleTextField.setText("Investigación de campo");
        descriptionTextArea.setText("Realizar entrevistas a los habitantes de la comunidad");
        startDatePicker.setValue(LocalDate.of(2026, 7, 1));
        endDatePicker.setValue(LocalDate.of(2026, 7, 15));
        estimatedTimeTextField.setText("8");
        effectiveTimeTextField.setText("6");
        progressTextField.setText("75");
        observationsTextArea.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }
}