package spp.presentation.controller;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.presentation.controller.instructor.FinalReportEvaluationController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FinalReportEvaluationControllerTest extends ControllerTestBase {

    private FinalReportEvaluationController controller;
    private TextField gradeTextField;
    private ComboBox<InternDTO> internComboBox;
    private ComboBox<ReportDocumentFileDTO> documentComboBox;
    private Button assignGradeButton;
    private Button modifyGradeButton;

    @BeforeEach
    void setUp() throws Exception {
        controller = new FinalReportEvaluationController();

        gradeTextField = new TextField();
        internComboBox = new ComboBox<>();
        documentComboBox = new ComboBox<>();
        assignGradeButton = new Button();
        modifyGradeButton = new Button();

        injectField("txtGrade", gradeTextField);
        injectField("cmbInterns", internComboBox);
        injectField("cmbInternDocuments", documentComboBox);
        injectField("btnAssignGrade", assignGradeButton);
        injectField("btnModifyGrade", modifyGradeButton);

        ReportDocumentFileDTO dummyDocument = new ReportDocumentFileDTO();
        dummyDocument.setDocumentId(1);
        dummyDocument.setGraded(false);
        documentComboBox.getItems().add(dummyDocument);
        documentComboBox.setValue(dummyDocument);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = FinalReportEvaluationController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = FinalReportEvaluationController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    private Object invokePrivateMethodWithArgs(String methodName, Object... args) throws Exception {
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass();
        }
        Method method = FinalReportEvaluationController.class.getDeclaredMethod(methodName, paramTypes);
        method.setAccessible(true);
        return method.invoke(controller, args);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Calificacion ingresada, hasEmptyFields retorna false")
    void testHasEmptyFields_gradeFilled() throws Exception {
        gradeTextField.setText("8");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Calificacion vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_gradeEmpty() throws Exception {
        gradeTextField.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Calificacion dentro del rango 0-10, hasValidGradeRange retorna true")
    void testHasValidGradeRange_validGrade() throws Exception {
        gradeTextField.setText("7");

        boolean valid = (boolean) invokePrivateMethod("hasValidGradeRange");
        assertTrue(valid);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Calificacion menor a 0, hasValidGradeRange retorna false")
    void testHasValidGradeRange_gradeNegative() throws Exception {
        gradeTextField.setText("-1");

        boolean valid = (boolean) invokePrivateMethod("hasValidGradeRange");
        assertFalse(valid);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Calificacion mayor a 10, hasValidGradeRange retorna false")
    void testHasValidGradeRange_gradeTooHigh() throws Exception {
        gradeTextField.setText("11");

        boolean valid = (boolean) invokePrivateMethod("hasValidGradeRange");
        assertFalse(valid);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Normal: Calificacion valida y no vacia, isGradeInputValid retorna true")
    void testIsGradeInputValid_validGrade() throws Exception {
        gradeTextField.setText("9");

        boolean valid = (boolean) invokePrivateMethod("isGradeInputValid");
        assertTrue(valid);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Alterno: Calificacion vacia, isGradeInputValid retorna false")
    void testIsGradeInputValid_emptyGrade() throws Exception {
        gradeTextField.setText("");

        boolean valid = (boolean) invokePrivateMethod("isGradeInputValid");
        assertFalse(valid);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Calificacion fuera de rango, isGradeInputValid retorna false")
    void testIsGradeInputValid_outOfRange() throws Exception {
        gradeTextField.setText("15");

        boolean valid = (boolean) invokePrivateMethod("isGradeInputValid");
        assertFalse(valid);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Normal: Documento sin calificar, configureGradeButtons habilita asignar y deshabilita modificar")
    void testConfigureGradeButtons_documentNotGraded() throws Exception {
        ReportDocumentFileDTO document = new ReportDocumentFileDTO();
        document.setDocumentId(1);
        document.setGraded(false);
        documentComboBox.getItems().clear();
        documentComboBox.getItems().add(document);
        documentComboBox.setValue(document);

        invokePrivateMethodWithArgs("configureGradeButtons", document);

        assertFalse(gradeTextField.isDisable());
        assertTrue(gradeTextField.getText().isEmpty());
        assertFalse(assignGradeButton.isDisable());
        assertTrue(modifyGradeButton.isDisable());
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Normal: Documento ya calificado, configureGradeButtons deshabilita asignar y habilita modificar")
    void testConfigureGradeButtons_documentGraded() throws Exception {
        ReportDocumentFileDTO document = new ReportDocumentFileDTO();
        document.setDocumentId(1);
        document.setGraded(true);
        document.setGrade(8);
        documentComboBox.getItems().clear();
        documentComboBox.getItems().add(document);
        documentComboBox.setValue(document);

        invokePrivateMethodWithArgs("configureGradeButtons", document);

        assertFalse(gradeTextField.isDisable());
        assertEquals("8", gradeTextField.getText());
        assertTrue(assignGradeButton.isDisable());
        assertFalse(modifyGradeButton.isDisable());
    }
}