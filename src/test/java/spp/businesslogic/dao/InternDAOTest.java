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
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniqueStudentNumber = "S" + uniqueSuffix.substring(uniqueSuffix.length() - 8);
        testIntern.setStatus("Activo");
        testIntern.setLastConnection("2025-11-22 19:15:13");
        testIntern.setFirstName("Uri");
        testIntern.setSecondName("Abdiel");
        testIntern.setFirstLastName("Masin");
        testIntern.setSecondLastName("Campechano");

        testIntern.setEmail("z" + uniqueSuffix + "@estudiantes.uv.mx");
        testIntern.setPhoneNumber("22" + uniqueSuffix);
        testIntern.setPassword("s0yUr14bd1");
        testIntern.setStudentNumber(uniqueStudentNumber);
        testIntern.setGender("M");
        testIntern.setSpeaksIndigenousLanguage(true);
        testIntern.setIndigenousLanguage("Náhuatl");
        testIntern.setBirthDate(LocalDateTime.parse("2006-07-07T00:00:00"));
    }

    @Test
    @DisplayName("Debe insertar un practicante exitosamente")
    void testInsertInternSuccess() throws DAOException {
        boolean result = internDAO.registerIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar practicante que  no habla lengua indígena")
    void testInsertInternWithoutIndigenousLanguageSuccess() throws DAOException {
        testIntern.setSpeaksIndigenousLanguage(false);
        testIntern.setIndigenousLanguage(null);
        boolean result = internDAO.registerIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar un Practicante por duplicación de matrícula")
    void testInsertInternFailedDuplicated() throws DAOException {
        internDAO.registerIntern(testIntern);
        assertThrows(DAOException.class, () -> {
            internDAO.registerIntern(testIntern);
        });
    }

    @Test
    @DisplayName("Debe obtener el id del practicante recién insertado")
    void testObtainIdSuccess() throws DAOException {
        internDAO.registerIntern(testIntern);
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
    @DisplayName("Debe devolver true si la función SQL determina que el estudiante existe")
    void testExistsStudentByStudentNumberSuccess() throws DAOException {
        internDAO.registerIntern(testIntern);
        boolean exists = internDAO.existsStudentByStudentNumber(testIntern.getStudentNumber());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debe lanzar DAOException si la función SQL determina que la matrícula es inválida")
    void testExistsStudentByStudentNumberFailed() {
        assertThrows(DAOException.class, () -> {
            internDAO.existsStudentByStudentNumber("S00000000");
        });
    }

    @Test
    @DisplayName("Debe inactivar un practicante exitosamente")
    void testDeactivateInternSuccess() throws DAOException {
        internDAO.registerIntern(testIntern);
        boolean isDeactivated = internDAO.deactivateIntern(testIntern);
        assertTrue(isDeactivated);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante sin matrícula")
    void testInsertInternFailedNullStudentNumber() throws DAOException {
        testIntern.setStudentNumber(null);
        assertThrows(DAOException.class, () -> {
            internDAO.registerIntern(testIntern);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante sin correo")
    void testInsertInternFailedNullEmail() throws DAOException {
        testIntern.setEmail(null);
        assertThrows(DAOException.class, () -> {
            internDAO.registerIntern(testIntern);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante sin fecha de nacimiento")
    void testInsertInternFailedNullBirthDate() throws DAOException {
        testIntern.setBirthDate(null);
        assertThrows(DAOException.class, () -> {
            internDAO.registerIntern(testIntern);
        });
    }
}
