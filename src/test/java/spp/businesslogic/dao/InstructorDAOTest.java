package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        testInstructor.setStatus("Activo");
        testInstructor.setLastConnection("2025-03-17 07:00:00");
        testInstructor.setFirstName("Eliel");
        testInstructor.setSecondName("Gustavo");
        testInstructor.setFirstLastName("Masin");
        testInstructor.setSecondLastName("Campechano");
        testInstructor.setEmail("eleliel@uv.mx");
        testInstructor.setPhoneNumber("2293962454");
        testInstructor.setPassword(".eliile.");
        testInstructor.setPersonalNumber("90982");
        testInstructor.setShift("Matutino");
    }

    @Test
    @DisplayName("Debe insertar un profesor exitosamente")
    void testAddInstructorSuccess() throws DAOException {
        boolean result = instructorDAO.addInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un número de personal duplicado")
    void testAddInstructorFailedDuplicatePersonalNumber() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        assertThrows(DAOException.class, () -> {
            instructorDAO.addInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe obtener el id del profesor recién insertado")
    void testObtainIdSuccess() throws DAOException {
        int result = instructorDAO.obtainId(testInstructor.getPersonalNumber());
        Assertions.assertTrue(result > 0);
    }

    @Test
    @DisplayName("Debe obtener el id de un profesor recién registrado")
    void testObtainIdAfterInsertSuccess() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        int idObtainedAfterInsertInstructor = instructorDAO.obtainId(testInstructor.getPersonalNumber());
        assertNotEquals(0, idObtainedAfterInsertInstructor);
    }

    @Test
    @DisplayName("Debe lanzar un error al insertar profesor sin número de personal")
    void testAddInstructorFailedNullPersonalNumber() throws DAOException {
        testInstructor.setPersonalNumber(null);
        assertThrows(DAOException.class, () -> {
            instructorDAO.addInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe lanzar un error al insertar profesor sin contraseña")
    void testAddInstructorFailedNullPassword() throws DAOException {
        testInstructor.setPassword(null);
        assertThrows(DAOException.class, () -> {
            instructorDAO.addInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al insertar profesor sin nombre")
    void testAddInstructorFailedNullFirstName() throws DAOException {
        testInstructor.setFirstName(null);
        assertThrows(DAOException.class, () -> {
            instructorDAO.addInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe obtener el mismo ID al consultar varias veces el mismo número de personal")
    void testObtainIdConsistency() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        int firstId = instructorDAO.obtainId(testInstructor.getPersonalNumber());
        int secondId = instructorDAO.obtainId(testInstructor.getPersonalNumber());
        assertEquals(firstId, secondId);
    }

    @Test
    @DisplayName("Debe registrar profesor con segundo apellido vacío")
    void testAddInstructorWithEmptySecondLastNameSuccess() throws DAOException {
        testInstructor.setSecondLastName(null);
        boolean result = instructorDAO.addInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al buscar un número de personal inexistente")
    void testObtainIdFailedNonExistentPersonalNumber() throws DAOException {
        assertThrows(DAOException.class, () -> {
            instructorDAO.obtainId("999999");
        });
    }
}
