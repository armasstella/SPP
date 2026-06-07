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
        testIntern = new InternDTO();
    }

    @BeforeEach
    void setUpEach() {
        testIntern.setStatus("Activo");
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
    @DisplayName("Debe insertar practicante que habla lengua indígena")
    void testInsertInternWithIndigenousLanguageSuccess() throws DAOException {
        testIntern.setSpeaksIndigenousLanguage(true);
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result);
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
        Assertions.assertTrue(result > 0);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al buscar una matrícula inexistente")
    void testObtainIdFailedNonExistentStudentNumber() throws DAOException {
        assertThrows(DAOException.class, () -> {
            internDAO.obtainId("S99999999");
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante sin matrícula")
    void testInsertInternFailedNullStudentNumber() throws DAOException {
        testIntern.setStudentNumber(null);
        assertThrows(DAOException.class, () -> {
            internDAO.addIntern(testIntern);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante sin correo")
    void testInsertInternFailedNullEmail() throws DAOException {
        testIntern.setEmail(null);
        assertThrows(DAOException.class, () -> {
            internDAO.addIntern(testIntern);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante sin fecha de nacimiento")
    void testInsertInternFailedNullBirthDate() throws DAOException {
        testIntern.setBirthDate(null);
        assertThrows(DAOException.class, () -> {
            internDAO.addIntern(testIntern);
        });
    }
}
