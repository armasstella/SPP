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
import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.presentation.controller.coordinator.NewProjectController;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NewProjectControllerTest extends ControllerTestBase {

    private NewProjectController controller;
    private TextField nameTextField;
    private TextArea descriptionTextArea;
    private TextField placesAvailableTextField;
    private TextField activitiesScheduleFileTextField;
    private ComboBox<ProjectManagerDTO> projectManagerComboBox;
    private ComboBox<LinkedOrganizationDTO> linkedOrganizationComboBox;

    @BeforeEach
    void setUp() throws Exception {
        controller = new NewProjectController();

        nameTextField = new TextField();
        descriptionTextArea = new TextArea();
        placesAvailableTextField = new TextField();
        activitiesScheduleFileTextField = new TextField();
        projectManagerComboBox = new ComboBox<>();
        linkedOrganizationComboBox = new ComboBox<>();

        injectField("txtName", nameTextField);
        injectField("taDescription", descriptionTextArea);
        injectField("txtPlacesAvailable", placesAvailableTextField);
        injectField("txtActivitiesScheduleFile", activitiesScheduleFileTextField);
        injectField("cmbProjectManager", projectManagerComboBox);
        injectField("cmbLinkedOrganization", linkedOrganizationComboBox);

        ProjectManagerDTO dummyManager = new ProjectManagerDTO();
        dummyManager.setId(1);
        dummyManager.setFirstName("Juan");
        dummyManager.setFirstLastName("Pérez");
        projectManagerComboBox.getItems().add(dummyManager);

        LinkedOrganizationDTO dummyOrganization = new LinkedOrganizationDTO();
        dummyOrganization.setId(1);
        dummyOrganization.setName("Organización Ejemplo");
        linkedOrganizationComboBox.getItems().add(dummyOrganization);
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = NewProjectController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = NewProjectController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los campos obligatorios llenos, hasEmptyFields retorna false")
    void testHasEmptyFields_allRequiredFilled() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Nombre vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_nameEmpty() throws Exception {
        nameTextField.setText("");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Descripcion vacia, hasEmptyFields retorna true")
    void testHasEmptyFields_descriptionEmpty() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Lugares disponibles vacio, hasEmptyFields retorna true")
    void testHasEmptyFields_placesAvailableEmpty() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: ProjectManager no seleccionado, hasEmptyFields retorna true")
    void testHasEmptyFields_projectManagerNull() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(null);
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: LinkedOrganization no seleccionado, hasEmptyFields retorna true")
    void testHasEmptyFields_linkedOrganizationNull() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(null);

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertTrue(empty);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: Archivo de horario no seleccionado no afecta hasEmptyFields")
    void testHasEmptyFields_activitiesScheduleFileEmpty() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));
        activitiesScheduleFileTextField.setText("");

        boolean empty = (boolean) invokePrivateMethod("hasEmptyFields");
        assertFalse(empty);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: Longitud minima del nombre cumplida, hasValidMinimumLengths retorna true")
    void testHasValidMinimumLengths_nameValid() throws Exception {
        nameTextField.setText("Proyecto");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertTrue(valid);
    }

    @Test
    @Order(9)
    @DisplayName("Flujo Alterno: Nombre corto (menos de 3 caracteres), hasValidMinimumLengths retorna false")
    void testHasValidMinimumLengths_nameTooShort() throws Exception {
        nameTextField.setText("Pr");

        boolean valid = (boolean) invokePrivateMethod("hasValidMinimumLengths");
        assertFalse(valid);
    }

    @Test
    @Order(10)
    @DisplayName("Flujo Normal: Campos llenos y nombre con longitud valida, areValidFields retorna true")
    void testAreValidFields_allValid() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertTrue(valid);
    }

    @Test
    @Order(11)
    @DisplayName("Flujo Alterno: Campo obligatorio vacio, areValidFields retorna false")
    void testAreValidFields_emptyField() throws Exception {
        nameTextField.setText("Proyecto de Investigación");
        descriptionTextArea.setText("");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }

    @Test
    @Order(12)
    @DisplayName("Flujo Alterno: Longitud minima no cumplida, areValidFields retorna false")
    void testAreValidFields_shortLength() throws Exception {
        nameTextField.setText("Pr");
        descriptionTextArea.setText("Investigación sobre energías renovables");
        placesAvailableTextField.setText("5");
        projectManagerComboBox.setValue(projectManagerComboBox.getItems().get(0));
        linkedOrganizationComboBox.setValue(linkedOrganizationComboBox.getItems().get(0));

        boolean valid = (boolean) invokePrivateMethod("areValidFields");
        assertFalse(valid);
    }
}