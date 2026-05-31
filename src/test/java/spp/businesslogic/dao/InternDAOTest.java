package spp.businesslogic.dao;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Assertions;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class InternDAOTest {
    private InternDAO internDAO;
    private InternDTO testIntern;

    @BeforeAll
    void setUpAll() {
        internDAO = new InternDAO();

    }

    @BeforeEach
    void setUpEach() {
        testIntern = new InternDTO();
        testIntern.setStatus("null");
        testIntern.setLastConnection("2025-11-22 19:15:13");
        testIntern.setFirstName("Uri");
        testIntern.setSecondName("Abdiel");
        testIntern.setFirstLastName("Masin");
        testIntern.setSecondLastName("Campechano");
        testIntern.setEmail("zS24013314@estudiantes.uv.mx");
        testIntern.setPhoneNumber("2299192196");
        testIntern.setPassword("s0yUr14bd1");
        testIntern.setStudentNumber("S24013314");
        testIntern.setGender("M");
        testIntern.setSpeaksIndigenousLanguage(true);
        testIntern.setBirthDate(LocalDateTime.parse("2006-07-07T00:00:00"));

    }

    @Test
    @DisplayName("Debe insertar un practicante exitosamente")
    void testInsertInternSuccess() throws DAOException {
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result, "El practicante se ha insertado correctamente");

    }

    @Test
    @DisplayName("Debe lanzar DAOException  al insertar un Practicante por duplicación de número de estudiante")
    void testInsertInternFailedDuplicated() throws DAOException {
        internDAO.addIntern(testIntern);
        assertThrows(DAOException.class,() -> internDAO.addIntern(testIntern));

    }

    @Test
    @DisplayName("Debe obtener el id del practicante recién insertado")
    void testObtainIdSuccess() throws DAOException {
        int result = internDAO.obtainId(testIntern.getStudentNumber());
        Assertions.assertTrue(result > 0,
                "No se obtuvo un id válido para la matricula dada");

    }

}
