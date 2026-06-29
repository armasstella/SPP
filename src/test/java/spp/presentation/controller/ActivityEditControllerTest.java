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
import spp.presentation.controller.intern.ActivityEditController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ActivityEditControllerTest extends ControllerTestBase {

    private ActivityEditController controller;
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
        controller = new ActivityEditController();

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
        Field field = ActivityEditController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = ActivityEditController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos validos, hasValidationErrors retorna false")
    void testHasValidationErrors_allFieldsValid() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertFalse(hasErrors);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Titulo vacio, hasValidationErrors retorna true")
    void testHasValidationErrors_titleEmpty() throws Exception {
        titleTextField.setText("");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Descripcion vacia, hasValidationErrors retorna true")
    void testHasValidationErrors_descriptionEmpty() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Fecha inicio nula, hasValidationErrors retorna true")
    void testHasValidationErrors_startDateNull() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(null);
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Fecha fin nula, hasValidationErrors retorna true")
    void testHasValidationErrors_endDateNull() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(null);
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Tiempo estimado vacio, hasValidationErrors retorna true")
    void testHasValidationErrors_estimatedTimeEmpty() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Tiempo efectivo vacio, hasValidationErrors retorna true")
    void testHasValidationErrors_effectiveTimeEmpty() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Progreso vacio, hasValidationErrors retorna true")
    void testHasValidationErrors_progressEmpty() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Alterno: Observaciones vacias, hasValidationErrors retorna true")
    void testHasValidationErrors_observationsEmpty() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Alterno: Fecha fin anterior a fecha inicio, hasValidationErrors retorna true")
    void testHasValidationErrors_endDateBeforeStartDate() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 20));
        endDatePicker.setValue(LocalDate.of(2026, 7, 16));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(11)
    @DisplayName("Flujo Alterno: Tiempo estimado no numerico, hasValidationErrors retorna true")
    void testHasValidationErrors_estimatedTimeNotNumeric() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("diez");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(12)
    @DisplayName("Flujo Alterno: Tiempo efectivo no numerico, hasValidationErrors retorna true")
    void testHasValidationErrors_effectiveTimeNotNumeric() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("ocho");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(13)
    @DisplayName("Flujo Alterno: Progreso no numerico, hasValidationErrors retorna true")
    void testHasValidationErrors_progressNotNumeric() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("ochenta");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(14)
    @DisplayName("Flujo Alterno: Progreso mayor a 100, hasValidationErrors retorna true")
    void testHasValidationErrors_progressGreaterThan100() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("120");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }

    @Test
    @Order(15)
    @DisplayName("Flujo Alterno: Tiempo estimado negativo, hasValidationErrors retorna true")
    void testHasValidationErrors_estimatedTimeNegative() throws Exception {
        titleTextField.setText("Análisis de resultados");
        descriptionTextArea.setText("Procesar datos obtenidos en las entrevistas");
        startDatePicker.setValue(LocalDate.of(2026, 7, 16));
        endDatePicker.setValue(LocalDate.of(2026, 7, 20));
        estimatedTimeTextField.setText("-10");
        effectiveTimeTextField.setText("8");
        progressTextField.setText("80");
        observationsTextArea.setText("Revisar la consistencia de los datos");

        boolean hasErrors = (boolean) invokePrivateMethod("hasValidationErrors");
        assertTrue(hasErrors);
    }
}