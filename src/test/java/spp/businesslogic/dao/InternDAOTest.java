package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
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
        testIntern.setFirstName("Jane");
        testIntern.setSecondName("Doe");
        testIntern.setFirstLastName("Smith");
        testIntern.setSecondLastName("Juárez");
        testIntern.setEmail("doedoe.dot@gmail.com");
        testIntern.setPhoneNumber("2223331234");
        testIntern.setPassword("cdevfrbgt");
        testIntern.setStudentNumber("S23061267");
        testIntern.setGender("M");
        testIntern.setSpeaksIndigenousLanguage(true);
        testIntern.setBirthDate(LocalDateTime.parse("2026-12-06T12:41:20"));
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



}
