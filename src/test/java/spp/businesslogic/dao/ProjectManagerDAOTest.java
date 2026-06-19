package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ProjectManagerDAOTest {

    private ProjectManagerDAO projectManagerDAO;
    private ProjectManagerDTO testManager;

    @BeforeAll
    void setUpAll() {
        projectManagerDAO = new ProjectManagerDAO();
        testManager = new ProjectManagerDTO();
    }

    @BeforeEach
    void setUpEach() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniquePhone = "22" + uniqueSuffix.substring(uniqueSuffix.length() - 8);

        testManager.setFirstName("Carlos");
        testManager.setSecondName("Eduardo");
        testManager.setFirstLastName("Ramírez");
        testManager.setSecondLastName("Soto");
        testManager.setRole("Líder Técnico");
        testManager.setResponsibility("Gestión de técnica");
        testManager.setPhoneNumber(uniquePhone);
    }

    @Test
    @DisplayName("Debe lanzar DAOException si se recibe un DTO nulo (Fail-Fast)")
    void testAddProjectManagerNullDTO() {
        assertThrows(DAOException.class, ()
                -> projectManagerDAO.addProjectManagerDAO(null));
    }

    @Test
    @DisplayName("Debe insertar un encargado de proyecto exitosamente")
    void testAddProjectManagerSuccess() throws DAOException {
        boolean result = projectManagerDAO.addProjectManagerDAO(testManager);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un encargado con teléfono duplicado")
    void testAddProjectManagerDuplicatePhone() throws DAOException {
        projectManagerDAO.addProjectManagerDAO(testManager);

        ProjectManagerDTO duplicate = new ProjectManagerDTO();
        duplicate.setFirstName("Ana");
        duplicate.setFirstLastName("Martínez");
        duplicate.setRole("Scrum Master");
        duplicate.setResponsibility("Facilitación");
        duplicate.setPhoneNumber(testManager.getPhoneNumber());

        assertThrows(DAOException.class, () -> projectManagerDAO.addProjectManagerDAO(duplicate));
    }

    @Test
    @DisplayName("Debe obtener lista de encargados (no nula)")
    void testObtainActiveProjectManagers() throws DAOException {
        List<ProjectManagerDTO> list = projectManagerDAO.obtainActiveProjectManagers();
        assertNotNull(list);
    }

    @Test
    @DisplayName("Debe devolver true si existen encargados (después de insertar uno)")
    void testSearchProjectManagerRegistersTrue() throws DAOException {
        boolean exists = projectManagerDAO.searchProjectManagerRegisters();
        assertTrue(exists);
    }
}