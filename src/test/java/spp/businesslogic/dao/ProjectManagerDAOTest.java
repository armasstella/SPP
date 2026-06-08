package spp.businesslogic.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProjectManagerDAOTest {

    private ProjectManagerDAO projectManagerDAO;
    private ProjectManagerDTO testProjectManager;

    @BeforeAll
    void setupAll() {
        projectManagerDAO = new ProjectManagerDAO();
        testProjectManager = new ProjectManagerDTO();
    }

    @BeforeEach
    void setup() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniquePhone = "924" + uniqueSuffix.substring(uniqueSuffix.length() - 7);

        testProjectManager.setFirstName("Jocelyn");
        testProjectManager.setSecondName("Nohemí");
        testProjectManager.setFirstLastName("Armas");
        testProjectManager.setSecondLastName("Mendoza");
        testProjectManager.setRole("Jefe de Calidad");
        testProjectManager.setResponsibility("Asegurar la calidad");
        testProjectManager.setPhoneNumber(uniquePhone);
    }

    @Test
    @DisplayName("Debe insertar un encargado de proyecto exitosamente")
    void testAddProjectManagerSuccess() throws DAOException {
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un encargado de proyecto duplicado")
    void testAddProjectManagerFailedDuplicatedProjectManager() throws DAOException {
        projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertThrows(DAOException.class, () ->
            projectManagerDAO.addProjectManagerDAO(testProjectManager));
    }

    @Test
    @DisplayName("Debe insertar un encargado con teléfono")
    void testAddProjectManagerWithPhoneSuccess() throws DAOException {
        testProjectManager.setPhoneNumber("2281234567");
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar encargado sin nombre")
    void testAddProjectManagerFailedNullFirstName() throws DAOException {
        testProjectManager.setFirstName(null);
        assertThrows(DAOException.class, () -> {
            projectManagerDAO.addProjectManagerDAO(testProjectManager);
        });
    }

    @Test
    @DisplayName("Debe insertar un encargado con nombre")
    void testAddProjectManagerWithNameSuccess() throws DAOException {
        testProjectManager.setFirstName("Luz");
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar encargado sin teléfono")
    void testAddProjectManagerFailedNullPhoneNumber() throws DAOException {
        testProjectManager.setPhoneNumber(null);
        assertThrows(DAOException.class, () -> {
            projectManagerDAO.addProjectManagerDAO(testProjectManager);
        });
    }

    @Test
    @DisplayName("Debe insertar encargado sin segundo nombre")
    void testAddProjectManagerNullSecondNameSuccess() throws DAOException {
        testProjectManager.setSecondName(null);
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar un encargado con segundo nombre")
    void testAddProjectManagerWithSecondNameSuccess() throws DAOException {
        testProjectManager.setSecondName("Fernanda");
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar encargado sin apellido paterno")
    void testAddProjectManagerFailedNullFirstLastName() throws DAOException {
        testProjectManager.setFirstLastName(null);
        assertThrows(DAOException.class, () -> {
            projectManagerDAO.addProjectManagerDAO(testProjectManager);
        });
    }

    @Test
    @DisplayName("Debe insertar un encargado con apellido paterno")
    void testAddProjectManagerWithFirstLastNameSuccess() throws DAOException {
        testProjectManager.setFirstLastName("Herrera");
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar encargado con segundo apellido vacío")
    void testAddProjectManagerNullSecondLastNameSuccess() throws DAOException {
        testProjectManager.setSecondLastName(null);
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar un encargado con segundo apellido")
    void testAddProjectManagerWithSecondLastNameSuccess() throws DAOException {
        testProjectManager.setSecondLastName("Juárez");
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar encargado con rol vacío")
    void testAddProjectManagerFailedNullRole() throws DAOException {
        testProjectManager.setRole(null);
        assertThrows(DAOException.class, () -> {
            projectManagerDAO.addProjectManagerDAO(testProjectManager);
        });
    }

    @Test
    @DisplayName("Debe insertar un encargado con rol")
    void testAddProjectManagerWithRoleSuccess() throws DAOException {
            testProjectManager.setRole("Product Manager");
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar encargado con responsabilidad vacía")
    void testAddProjectManagerFailedNullResponsibility() throws DAOException {
        testProjectManager.setResponsibility(null);
        assertThrows(DAOException.class, () -> {
            projectManagerDAO.addProjectManagerDAO(testProjectManager);
        });
    }

    @Test
    @DisplayName("Debe insertar un encargado con su responsabilidad")
    void testAddProjectManagerWithResponsibilitySuccess() throws DAOException {
        testProjectManager.setResponsibility("Dirigir el Proyecto de Calidad");
        boolean result = projectManagerDAO.addProjectManagerDAO(testProjectManager);
        assertTrue(result);
    }
}
