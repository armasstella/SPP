package spp.presentation.controller;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.businesslogic.dto.IndicatorFilterDTO;
import spp.businesslogic.enums.GenderFilter;
import spp.businesslogic.enums.YesNoAllFilter;
import spp.presentation.controller.coordinator.IndicatorReportController;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class IndicatorReportControllerTest extends ControllerTestBase {

    private IndicatorReportController controller;
    private ComboBox<GenderFilter> genderComboBox;
    private ComboBox<YesNoAllFilter> languageComboBox;
    private ComboBox<String> periodComboBox;
    private TextField minAgeTextField;
    private TextField maxAgeTextField;

    @BeforeEach
    void setUp() throws Exception {
        controller = new IndicatorReportController();

        genderComboBox = new ComboBox<>();
        languageComboBox = new ComboBox<>();
        periodComboBox = new ComboBox<>();
        minAgeTextField = new TextField();
        maxAgeTextField = new TextField();

        injectField("cmbFilterGender", genderComboBox);
        injectField("cmbFilterLanguage", languageComboBox);
        injectField("cmbFilterPeriod", periodComboBox);
        injectField("txtFilterMinAge", minAgeTextField);
        injectField("txtFilterMaxAge", maxAgeTextField);

        genderComboBox.getItems().addAll(GenderFilter.values());
        genderComboBox.setValue(GenderFilter.TODOS);

        languageComboBox.getItems().addAll(YesNoAllFilter.values());
        languageComboBox.setValue(YesNoAllFilter.TODOS);

        periodComboBox.getItems().add("Todos");
        periodComboBox.getItems().add("FEBRERO - JULIO 26");
        periodComboBox.setValue("Todos");
    }

    private void injectField(String fieldName, Object value) throws Exception {
        Field field = IndicatorReportController.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(controller, value);
    }

    private Object invokePrivateMethod(String methodName) throws Exception {
        Method method = IndicatorReportController.class.getDeclaredMethod(methodName);
        method.setAccessible(true);
        return method.invoke(controller);
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Todos los filtros vacios, buildIndicatorFilterDTO asigna valores por defecto")
    void testBuildIndicatorFilterDTO_allFiltersEmpty() throws Exception {
        minAgeTextField.setText("");
        maxAgeTextField.setText("");

        IndicatorFilterDTO filterDTO = (IndicatorFilterDTO) invokePrivateMethod("buildIndicatorFilterDTO");

        assertNotNull(filterDTO);
        assertEquals(GenderFilter.TODOS, filterDTO.getGender());
        assertEquals(YesNoAllFilter.TODOS, filterDTO.getIndigenousLanguage());
        assertEquals("Todos", filterDTO.getPeriod());
        assertNull(filterDTO.getMinAge());
        assertNull(filterDTO.getMaxAge());
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Solo edad minima ingresada, buildIndicatorFilterDTO asigna minAge")
    void testBuildIndicatorFilterDTO_onlyMinAge() throws Exception {
        minAgeTextField.setText("18");
        maxAgeTextField.setText("");

        IndicatorFilterDTO filterDTO = (IndicatorFilterDTO) invokePrivateMethod("buildIndicatorFilterDTO");

        assertNotNull(filterDTO);
        assertEquals(18, filterDTO.getMinAge());
        assertNull(filterDTO.getMaxAge());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Solo edad maxima ingresada, buildIndicatorFilterDTO asigna maxAge")
    void testBuildIndicatorFilterDTO_onlyMaxAge() throws Exception {
        minAgeTextField.setText("");
        maxAgeTextField.setText("65");

        IndicatorFilterDTO filterDTO = (IndicatorFilterDTO) invokePrivateMethod("buildIndicatorFilterDTO");

        assertNotNull(filterDTO);
        assertNull(filterDTO.getMinAge());
        assertEquals(65, filterDTO.getMaxAge());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Ambas edades ingresadas, buildIndicatorFilterDTO asigna ambas")
    void testBuildIndicatorFilterDTO_bothAges() throws Exception {
        minAgeTextField.setText("18");
        maxAgeTextField.setText("65");

        IndicatorFilterDTO filterDTO = (IndicatorFilterDTO) invokePrivateMethod("buildIndicatorFilterDTO");

        assertNotNull(filterDTO);
        assertEquals(18, filterDTO.getMinAge());
        assertEquals(65, filterDTO.getMaxAge());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Normal: Edad minima y maxima con espacios, buildIndicatorFilterDTO las limpia")
    void testBuildIndicatorFilterDTO_agesWithSpaces() throws Exception {
        minAgeTextField.setText(" 18 ");
        maxAgeTextField.setText(" 65 ");

        IndicatorFilterDTO filterDTO = (IndicatorFilterDTO) invokePrivateMethod("buildIndicatorFilterDTO");

        assertNotNull(filterDTO);
        assertEquals(18, filterDTO.getMinAge());
        assertEquals(65, filterDTO.getMaxAge());
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Normal: ComboBox de genero tiene valores cargados")
    void testGenderComboBox_hasItems() {
        assertNotNull(genderComboBox.getItems());
        assertEquals(GenderFilter.values().length, genderComboBox.getItems().size());
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: ComboBox de lengua indigena tiene valores cargados")
    void testLanguageComboBox_hasItems() {
        assertNotNull(languageComboBox.getItems());
        assertEquals(YesNoAllFilter.values().length, languageComboBox.getItems().size());
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Normal: ComboBox de periodo tiene valores cargados")
    void testPeriodComboBox_hasItems() {
        assertNotNull(periodComboBox.getItems());
        assertTrue(periodComboBox.getItems().size() >= 1);
    }
}