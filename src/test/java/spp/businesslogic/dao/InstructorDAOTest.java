package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InstructorDAOTest {

    private InstructorDAO instructorDAO;
    private InstructorDTO testInstructor;

    @BeforeAll
    void setupAll() {
        instructorDAO = new InstructorDAO();
        testInstructor = new InstructorDTO();
    }

    @BeforeEach
    void setUp() {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniqueEmail = "instructor" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@uv.mx";
        String uniquePersonalNumber = "P" + uniqueSuffix.substring(uniqueSuffix.length() - 8);
        String uniquePhone = "228" + uniqueSuffix.substring(uniqueSuffix.length() - 7);

        testInstructor.setFirstName("Carlos");
        testInstructor.setSecondName("");
        testInstructor.setFirstLastName("Gomez");
        testInstructor.setSecondLastName("");
        testInstructor.setEmail(uniqueEmail);
        testInstructor.setPhoneNumber(uniquePhone);
        testInstructor.setPassword("Instructor123!");
        testInstructor.setPersonalNumber(uniquePersonalNumber);
        testInstructor.setShift("Matutino");
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe registrar un instructor correctamente")
    void testRegisterInstructorSuccess() throws DAOException {
        boolean result = instructorDAO.registerInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @Order(2)
    @DisplayName("Excepción: Debe lanzar DAOException al intentar registrar un instructor duplicado")
    void testRegisterInstructorDuplicate() throws DAOException {
        instructorDAO.registerInstructor(testInstructor);

        DAOException exception = assertThrows(DAOException.class, () -> {
            instructorDAO.registerInstructor(testInstructor);
        });

        assertTrue(exception.getMessage().contains("El Usuario que usted está intentando registrar ya existe"));
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Normal: Debe obtener la lista de instructores activos y verificar que el registrado esté presente")
    void testGetActiveInstructors() throws DAOException {
        instructorDAO.registerInstructor(testInstructor);

        InstructorDTO expected = new InstructorDTO();
        expected.setFirstName(testInstructor.getFirstName());
        expected.setFirstLastName(testInstructor.getFirstLastName());
        expected.setEmail(testInstructor.getEmail());
        expected.setPersonalNumber(testInstructor.getPersonalNumber());
        expected.setShift(testInstructor.getShift());

        List<InstructorDTO> actualList = instructorDAO.getActiveInstructors();
        assertFalse(actualList.isEmpty());

        InstructorDTO found = actualList.stream()
                .filter(i -> i.getPersonalNumber().equals(expected.getPersonalNumber()))
                .findFirst()
                .orElse(null);

        assertNotNull(found);

        assertEquals(expected.getFirstName().trim(), found.getFirstName().trim());
        assertEquals(expected.getFirstLastName().trim(), found.getFirstLastName().trim());
        assertEquals(expected.getEmail(), found.getEmail());
        assertEquals(expected.getShift(), found.getShift());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Normal: Debe obtener la lista de identificadores de instructores activos")
    void testGetActiveInstructorsIdentifiers() throws DAOException {
        instructorDAO.registerInstructor(testInstructor);

        List<InstructorDTO> identifiers = instructorDAO.getActiveInstructorsIdentifiers();
        assertFalse(identifiers.isEmpty());

        InstructorDTO found = identifiers.stream()
                .filter(instructor -> instructor.getPersonalNumber().equals(testInstructor.getPersonalNumber()))
                .findFirst()
                .orElse(null);

        assertNotNull(found);
        assertTrue(found.getId() > 0);
        assertNotNull(found.getFullName());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Normal: Debe obtener el número personal a partir del correo del instructor activo")
    void testFindActivePersonalNumberByEmail() throws DAOException {
        instructorDAO.registerInstructor(testInstructor);

        String personalNumber = instructorDAO.findActivePersonalNumberByEmail(testInstructor.getEmail());
        assertEquals(testInstructor.getPersonalNumber(), personalNumber);
    }

    @Test
    @Order(6)
    @DisplayName("Flujo Alterno: Debe retornar null al buscar número personal con correo inexistente")
    void testFindActivePersonalNumberByEmailNotFound() throws DAOException {
        String personalNumber = instructorDAO.findActivePersonalNumberByEmail("noexiste@uv.mx");
        assertNull(personalNumber);
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: Debe desactivar un instructor correctamente")
    void testDeactivateInstructor() throws DAOException {
        instructorDAO.registerInstructor(testInstructor);

        boolean result = instructorDAO.deactivateInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @Order(8)
    @DisplayName("Flujo Alterno: Desactivar un instructor que no existe debe devolver false")
    void testDeactivateInstructorNotFound() throws DAOException {
        InstructorDTO fake = new InstructorDTO();
        fake.setPersonalNumber("P99999999");

        boolean result = instructorDAO.deactivateInstructor(fake);
        assertFalse(result);
    }
}