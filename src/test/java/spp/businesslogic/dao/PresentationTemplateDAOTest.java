package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.PresentationTemplateDTO;
import spp.businesslogic.exceptions.DAOException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PresentationTemplateDAOTest {

    private PresentationTemplateDAO presentationTemplateDAO;
    private PresentationTemplateDTO testTemplate;

    @BeforeAll
    void setupAll() {
        presentationTemplateDAO = new PresentationTemplateDAO();
        testTemplate = new PresentationTemplateDTO();

        String uniqueSuffix = String.valueOf(System.currentTimeMillis());

        testTemplate.setOriginalName("Plantilla_Oficial.pptx");
        testTemplate.setSavedName("TEMP_" + uniqueSuffix + ".pptx");
        testTemplate.setFilePath("/docs/templates/TEMP_" + uniqueSuffix + ".pptx");
        testTemplate.setSizeMb(5.0);
        testTemplate.setExtension(".pptx");
        testTemplate.setUploadDate(LocalDateTime.now().withNano(0));
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe guardar la plantilla exitosamente para un profesor válido")
    void testSaveDocumentSuccess() throws DAOException {
        String VALID_PERSONAL_NUMBER = "12345";
        boolean result = presentationTemplateDAO.saveDocument(VALID_PERSONAL_NUMBER, testTemplate);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Guardar con número de personal inexistente debe devolver false")
    void testSaveDocumentInvalidInstructor() throws DAOException {
        boolean result = presentationTemplateDAO.saveDocument("FAKE000", testTemplate);
        assertFalse(result);
    }
}