package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InternDAOTest {

    private InternDAO internDAO;
    private InternDTO testIntern;

    @BeforeAll
    void setupAll() {
        internDAO = new InternDAO();
        testIntern = new InternDTO();
    }

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "practicante" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@uv.mx";
        String uniqueStudentNumber = "S" + uniqueSuffix.substring(uniqueSuffix.length() - 8);
        String uniquePhone = "228" + uniqueSuffix.substring(uniqueSuffix.length() - 7);

        testIntern.setFirstName("Juan");
        testIntern.setSecondName("");
        testIntern.setFirstLastName("Perez");
        testIntern.setSecondLastName("");
        testIntern.setEmail(uniqueEmail);
        testIntern.setPhoneNumber(uniquePhone);
        testIntern.setPassword("Password123!");
        testIntern.setStudentNumber(uniqueStudentNumber);
        testIntern.setSex("Masculino");
        testIntern.setSpeaksIndigenousLanguage(false);
        testIntern.setBirthDate(LocalDateTime.of(2000, 1, 1, 0, 0));
    }

    @Test
    @Order(1)
    @DisplayName("Debe registrar un practicante exitosamente")
    void testRegisterInternSuccess() throws DAOException {
        boolean result = internDAO.registerIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException al intentar registrar un practicante duplicado")
    void testRegisterInternDuplicate() throws DAOException {
        internDAO.registerIntern(testIntern);

        DAOException exception = assertThrows(DAOException.class, () -> {
            internDAO.registerIntern(testIntern);
        });

        assertTrue(exception.getMessage().contains("El Usuario que usted está intentando registrar ya existe"));
    }

    @Test
    @Order(3)
    @DisplayName("Debe obtener la lista de practicantes activos y verificar que el registrado esté presente")
    void testGetActiveInterns() throws DAOException {
        internDAO.registerIntern(testIntern);

        InternDTO expectedIntern = new InternDTO();
        expectedIntern.setStudentNumber(testIntern.getStudentNumber());
        expectedIntern.setFullName("Juan  Perez");
        expectedIntern.setEmail(testIntern.getEmail());

        List<InternDTO> actualInterns = internDAO.getActiveInterns();
        assertFalse(actualInterns.isEmpty());

        InternDTO internFound = actualInterns.stream()
                .filter(intern -> intern.getStudentNumber().equals(expectedIntern.getStudentNumber()))
                .findFirst()
                .orElse(null);

        assertNotNull(internFound);
        assertEquals(expectedIntern.getFullName().trim(), internFound.getFullName().trim());
        assertEquals(expectedIntern.getEmail(), internFound.getEmail());
    }

    @Test
    @Order(4)
    @DisplayName("Debe verificar que la matrícula existe en la BD")
    void testExistsStudentByStudentNumber() throws DAOException {
        internDAO.registerIntern(testIntern);
        boolean exists = internDAO.existsStudentByStudentNumber(testIntern.getStudentNumber());
        assertTrue(exists);
    }

    @Test
    @Order(5)
    @DisplayName("Debe obtener la matrícula a partir del correo del practicante activo")
    void testFindActiveStudentNumberByEmail() throws DAOException {
        internDAO.registerIntern(testIntern);
        String studentNumber = internDAO.findActiveStudentNumberByEmail(testIntern.getEmail());
        assertEquals(testIntern.getStudentNumber(), studentNumber);
    }

    @Test
    @Order(6)
    @DisplayName("Debe desactivar un practicante correctamente")
    void testDeactivateIntern() throws DAOException {
        internDAO.registerIntern(testIntern);
        boolean result = internDAO.deactivateIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @Order(7)
    @DisplayName("Debe devolver lista de practicantes sin proyecto asignado y debe contener al recién insertado")
    void testFindUnassignedInternsIdentifiers() throws DAOException {
        internDAO.registerIntern(testIntern);

        List<InternDTO> unassignedList = internDAO.findUnassignedInternsIdentifiers();
        assertNotNull(unassignedList);

        boolean found = unassignedList.stream()
                .anyMatch(intern -> intern.getStudentNumber().equals(testIntern.getStudentNumber()));
        assertTrue(found);
    }
}
