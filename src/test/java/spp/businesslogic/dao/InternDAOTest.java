package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        String substring = uniqueSuffix.substring(uniqueSuffix.length() - 8);
        String uniqueStudentNumber = "S" + substring;
        String uniquePhone = "22" + substring;
        String uniqueEmail = "z" + uniqueStudentNumber.toLowerCase() + "@estudiantes.uv.mx";

        testIntern.setStatus("Activo");
        testIntern.setLastConnection("2025-11-22 19:15:13");
        testIntern.setFirstName("Uri");
        testIntern.setSecondName("Abdiel");
        testIntern.setFirstLastName("Masin");
        testIntern.setSecondLastName("Campechano");
        testIntern.setEmail(uniqueEmail);
        testIntern.setPhoneNumber(uniquePhone);
        testIntern.setPassword("Pass123!");
        testIntern.setStudentNumber(uniqueStudentNumber);
        testIntern.setSex("Masculino");
        testIntern.setSpeaksIndigenousLanguage(true);
        testIntern.setIndigenousLanguage("Náhuatl");
        testIntern.setBirthDate(LocalDateTime.parse("2006-07-07T00:00:00"));
    }

    @Test
    @DisplayName("Debe insertar un practicante exitosamente")
    void testAddInternSuccess() throws DAOException {
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar practicante que no habla lengua indígena")
    void testAddInternWithoutIndigenousLanguageSuccess() throws DAOException {
        testIntern.setSpeaksIndigenousLanguage(false);
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante con matrícula duplicada")
    void testAddInternDuplicateStudentNumber() throws DAOException {
        internDAO.addIntern(testIntern);
        InternDTO duplicate = new InternDTO();
        duplicate.setStudentNumber(testIntern.getStudentNumber());
        duplicate.setEmail("zs01234567@estudiantes.uv.mx");
        duplicate.setFirstName("Armando");
        duplicate.setFirstLastName("Hernández");
        duplicate.setPhoneNumber("1234567890");
        duplicate.setPassword("Password1!");
        duplicate.setSex("Masculino");
        duplicate.setSpeaksIndigenousLanguage(false);
        duplicate.setBirthDate(LocalDateTime.now().minusYears(20));
        assertThrows(DAOException.class, () -> internDAO.addIntern(duplicate));
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar practicante con correo duplicado")
    void testAddInternDuplicateEmail() throws DAOException {
        internDAO.addIntern(testIntern);
        String secondSuffix = String.valueOf(System.currentTimeMillis() + 1);
        String secondStudentNumber = "S" + secondSuffix.substring(secondSuffix.length() - 8);

        InternDTO duplicate = new InternDTO();
        duplicate.setStudentNumber(secondStudentNumber);
        duplicate.setEmail(testIntern.getEmail());
        duplicate.setFirstName("Sebastián");
        duplicate.setFirstLastName("Pérez");
        duplicate.setPhoneNumber("2987654321");
        duplicate.setPassword("Password1!");
        duplicate.setSex("Masculino");
        duplicate.setSpeaksIndigenousLanguage(false);
        duplicate.setBirthDate(LocalDateTime.now().minusYears(20));

        assertThrows(DAOException.class, () -> internDAO.addIntern(duplicate));
    }

    @Test
    @DisplayName("Debe insertar practicante con segundo nombre nulo (campo opcional)")
    void testAddInternWithSecondNameNull() throws DAOException {
        testIntern.setSecondName(null);
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar practicante con segundo apellido vacío (campo opcional)")
    void testAddInternWithEmptySecondLastName() throws DAOException {
        testIntern.setSecondLastName("");
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar practicante solo con campos obligatorios (mínimo de datos)")
    void testAddInternWithMinimalData() throws DAOException {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String substring = uniqueSuffix.substring(uniqueSuffix.length() - 8);
        String uniqueStudentNumber = "S" + substring;
        String uniqueEmail = "z" + uniqueStudentNumber.toLowerCase() + "@estudiantes.uv.mx";
        String uniquePhone = "22" + substring;

        InternDTO minimal = new InternDTO();
        minimal.setFirstName("Mariana");
        minimal.setFirstLastName("Antonio");
        minimal.setStudentNumber(uniqueStudentNumber);
        minimal.setEmail(uniqueEmail);
        minimal.setPhoneNumber(uniquePhone);
        minimal.setPassword("Pass123!");
        minimal.setSex("Femenino");
        minimal.setSpeaksIndigenousLanguage(false);
        minimal.setBirthDate(LocalDateTime.now().minusYears(18));

        boolean result = internDAO.addIntern(minimal);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar practicante con valores en el límite máximo de longitud")
    void testAddInternWithMaxLengthValues() throws DAOException {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String substring = uniqueSuffix.substring(uniqueSuffix.length() - 8);
        String uniqueStudentNumber = "S" + substring;
        String uniqueEmail = "zS" + substring + "@estudiantes.uv.mx";
        String uniquePhone = "22" + substring;

        InternDTO intern = new InternDTO();
        intern.setStatus("Activo");
        intern.setLastConnection("2025-11-22 19:15:13");
        intern.setFirstName("A".repeat(25));
        intern.setSecondName("");
        intern.setFirstLastName("A".repeat(25));
        intern.setSecondLastName("");
        intern.setEmail(uniqueEmail);
        intern.setPhoneNumber(uniquePhone);
        intern.setPassword("Pass123!");
        intern.setStudentNumber(uniqueStudentNumber);
        intern.setSex("Masculino");
        intern.setSpeaksIndigenousLanguage(true);
        intern.setIndigenousLanguage("Náhuatl");
        intern.setBirthDate(LocalDateTime.parse("2006-07-07T00:00:00"));

        boolean result = internDAO.addIntern(intern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar practicante con campos opcionales que contienen solo espacios (se tratan como nulos)")
    void testAddInternWithWhitespaceOnlyInOptionalFields() throws DAOException {
        testIntern.setSecondName("   ");
        testIntern.setSecondLastName("   ");
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe obtener el ID del practicante por matrícula")
    void testObtainIdSuccess() throws DAOException {
        internDAO.addIntern(testIntern);
        int id = internDAO.obtainId(testIntern.getStudentNumber());
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al buscar matrícula inexistente")
    void testObtainIdNotFound() {
        assertThrows(DAOException.class, () -> internDAO.obtainId("S99999999"));
    }

    @Test
    @DisplayName("Debe obtener el ID incluso después de inactivar al practicante")
    void testObtainIdAfterInactivation() throws DAOException {
        internDAO.addIntern(testIntern);
        internDAO.inactivateIntern(testIntern);
        int id = internDAO.obtainId(testIntern.getStudentNumber());
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe devolver true si la matrícula existe")
    void testSearchStudentNumberRegisterExists() throws DAOException {
        internDAO.addIntern(testIntern);
        boolean exists = internDAO.searchStudentNumberRegister(testIntern.getStudentNumber());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debe lanzar DAOException si la matrícula no existe")
    void testSearchStudentNumberRegisterNotFound() {
        assertThrows(DAOException.class, () -> internDAO.searchStudentNumberRegister("S00000000"));
    }

    @Test
    @DisplayName("Debe devolver true incluso si el practicante está inactivo")
    void testSearchStudentNumberRegisterAfterInactivation() throws DAOException {
        internDAO.addIntern(testIntern);
        internDAO.inactivateIntern(testIntern);
        boolean exists = internDAO.searchStudentNumberRegister(testIntern.getStudentNumber());
        assertTrue(exists);
    }

    @Test
    @DisplayName("Debe obtener lista de practicantes activos (puede estar vacía)")
    void testObtainAllActiveInternsSuccess() throws DAOException {
        List<InternDTO> interns = internDAO.obtainAllActiveInterns();
        assertNotNull(interns);
    }

    @Test
    @DisplayName("Después de insertar uno activo, debe aparecer en la lista")
    void testObtainAllActiveInternsIncludesNew() throws DAOException {
        internDAO.addIntern(testIntern);
        List<InternDTO> interns = internDAO.obtainAllActiveInterns();
        boolean found = interns.stream().anyMatch(i ->
                testIntern.getStudentNumber().equals(i.getStudentNumber()));
        assertTrue(found);
    }

    @Test
    @DisplayName("Debe inactivar un practicante exitosamente")
    void testInactivateInternSuccess() throws DAOException {
        internDAO.addIntern(testIntern);
        boolean result = internDAO.inactivateIntern(testIntern);
        assertTrue(result);
        List<InternDTO> active = internDAO.obtainAllActiveInterns();
        boolean stillActive = active.stream().anyMatch(i ->
                testIntern.getStudentNumber().equals(i.getStudentNumber()));
        assertFalse(stillActive);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al intentar inactivar una matrícula inexistente")
    void testInactivateInternNotFound() {
        InternDTO fake = new InternDTO();
        fake.setStudentNumber("S00000000");
        assertThrows(DAOException.class, () -> internDAO.inactivateIntern(fake));
    }
}