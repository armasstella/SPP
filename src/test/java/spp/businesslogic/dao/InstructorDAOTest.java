package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniquePersonalNumber = uniqueSuffix.substring(uniqueSuffix.length() - 5);

        testInstructor.setStatus("Activo");
        testInstructor.setLastConnection("2025-03-17 07:00:00");
        testInstructor.setFirstName("Eliel");
        testInstructor.setSecondName("Gustavo");
        testInstructor.setFirstLastName("Masin");
        testInstructor.setSecondLastName("Campechano");

        testInstructor.setEmail("eli" + uniqueSuffix + "@uv.mx");
        testInstructor.setPhoneNumber("22939" + uniqueSuffix);
        testInstructor.setPassword(".eliile.");
        testInstructor.setPersonalNumber(uniquePersonalNumber);
        testInstructor.setShift("Matutino");
    }

    @Test
    @DisplayName("Debe insertar un profesor exitosamente")
    void testRegisterInstructorSuccess() throws DAOException {
        boolean result = instructorDAO.registerInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un número de personal duplicado")
    void testRegisterInstructorFailedDuplicatePersonalNumber() throws DAOException {
        instructorDAO.registerInstructor(testInstructor);
        assertThrows(DAOException.class, () -> {
            instructorDAO.registerInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar profesor sin número de personal")
    void testRegisterInstructorFailedNullPersonalNumber() throws DAOException {
        testInstructor.setPersonalNumber(null);
        assertThrows(DAOException.class, () -> {
            instructorDAO.registerInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar profesor sin contraseña")
    void testRegisterInstructorFailedNullPassword() throws DAOException {
        testInstructor.setPassword(null);
        assertThrows(DAOException.class, () -> {
            instructorDAO.registerInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar profesor sin nombre")
    void testRegisterInstructorFailedNullFirstName() throws DAOException {
        testInstructor.setFirstName(null);
        assertThrows(DAOException.class, () -> {
            instructorDAO.registerInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe registrar profesor con segundo apellido vacío")
    void testRegisterInstructorWithEmptySecondLastNameSuccess() throws DAOException {
        testInstructor.setSecondLastName(null);
        boolean result = instructorDAO.registerInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe desactivar un profesor existente exitosamente")
    void testDeactivateInstructorSuccess() throws DAOException {
        instructorDAO.registerInstructor(testInstructor);
        boolean result = instructorDAO.deactivateInstructor(testInstructor);
        assertTrue(result);
    }
}
