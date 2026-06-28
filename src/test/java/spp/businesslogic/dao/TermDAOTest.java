package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TermDAOTest {

    private TermDAO termDAO;
    private String dynamicTermName;
    private static int yearCounter;

    @BeforeAll
    void setupAll() {
        termDAO = new TermDAO();
        yearCounter = 10;
    }

    @BeforeEach
    void setUp() throws DAOException {
        termDAO.deactivateCurrentTerm();

        dynamicTermName = "FEB-JUL-" + yearCounter;
        yearCounter++;
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar un nuevo periodo escolar exitosamente")
    void testInsertTermSuccess() throws DAOException {
        boolean result = termDAO.insertTerm(dynamicTermName);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Normal: Debe obtener el nombre del periodo escolar activo")
    void testFindActiveTermNameSuccess() throws DAOException {
        termDAO.insertTerm(dynamicTermName);

        String activeTerm = termDAO.findActiveTermName();

        assertNotNull(activeTerm);
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Debe obtener un ID válido para el periodo escolar activo")
    void testFindActiveTermIdSuccess() throws DAOException {
        termDAO.insertTerm(dynamicTermName);

        int activeTermId = termDAO.findActiveTermId();

        assertTrue(activeTermId > 0);
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Debe recuperar una lista con los nombres de todos los periodos")
    void testFindTermNamesSuccess() throws DAOException {
        termDAO.insertTerm(dynamicTermName);

        List<String> terms = termDAO.findTermNames();

        assertFalse(terms.isEmpty());
        assertTrue(terms.contains(dynamicTermName));
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Normal: Debe desactivar el periodo actual exitosamente")
    void testDeactivateCurrentTermSuccess() throws DAOException {
        termDAO.insertTerm(dynamicTermName);

        boolean result = termDAO.deactivateCurrentTerm();

        assertTrue(result);
    }

    @Test
    @Order(6)
    @DisplayName("Excepción: Debe lanzar DAOException al insertar un periodo duplicado")
    void testInsertTermDuplicate() throws DAOException {
        termDAO.insertTerm(dynamicTermName);

        DAOException exception = assertThrows(DAOException.class, () -> {
            termDAO.insertTerm(dynamicTermName);
        });

        assertTrue(exception.getMessage().contains("Operación denegada: Ya existe un periodo activo."));
    }
}