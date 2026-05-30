package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InstructorDAOTest {

    private InstructorDAO instructorDAO;
    private InstructorDTO testInstructor;

    @BeforeAll
    void setupAll() {
        instructorDAO = new InstructorDAO();
    }

    @BeforeEach
    void setUp() {
        testInstructor = new InstructorDTO();
        testInstructor.setStatus("null");
        testInstructor.setLastConnection("2025-03-17 07:00:00");
        testInstructor.setFirstName("Eliel");
        testInstructor.setSecondName("");
        testInstructor.setFirstLastName("Masin");
        testInstructor.setSecondLastName("Campechano");
        testInstructor.setEmail("eleliel@uv.mx");
        testInstructor.setPhoneNumber("2293962454");
        testInstructor.setPassword(".eliile.");
        testInstructor.setPersonalNumber("00002");
        testInstructor.setShift("Matutino");
    }

    @Test
    @DisplayName("Debe insertar un profesor exitosamente")
    void testAddInstructorSuccess() throws DAOException {
        boolean result = instructorDAO.addInstructor(testInstructor);
        assertTrue(result, "El método debería retornar true al insertar exitosamente");
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
        Assertions.assertTrue(result > 0,
                "No se obtuvo un id válido para el número de personal dado");
    }

}
