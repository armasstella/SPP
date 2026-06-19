package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class InternDAOTest {

    private InternDAO internDAO;
    private InternDTO testIntern;

    @BeforeAll
    void setUpAll() {
        internDAO = new InternDAO();
        testIntern = new InternDTO();

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
    @Order(1)
    @DisplayName("Debe lanzar DAOException al insertar un practicante nulo")
    void testAddInternNullDTO() {
        assertThrows(DAOException.class, () -> internDAO.addIntern(null));
    }

    @Test
    @Order(2)
    @DisplayName("Debe lanzar DAOException al buscar matrícula inexistente")
    void testObtainIdNotFound() {
        assertThrows(DAOException.class, () -> internDAO.obtainId("S99999999"));
    }

    @Test
    @Order(3)
    @DisplayName("Debe lanzar DAOException si la matrícula no existe al verificar registro")
    void testSearchStudentNumberRegisterNotFound() {
        assertThrows(DAOException.class, () -> internDAO.searchStudentNumberRegister("S00000000"));
    }

    @Test
    @Order(4)
    @DisplayName("Debe lanzar DAOException al intentar inactivar una matrícula inexistente")
    void testInactivateInternNotFound() {
        InternDTO fakeIntern = new InternDTO();
        fakeIntern.setStudentNumber("S00000000");
        assertThrows(DAOException.class, () -> internDAO.inactivateIntern(fakeIntern));
    }

    @Test
    @Order(5)
    @DisplayName("Debe insertar un practicante exitosamente")
    void testAddInternSuccess() throws DAOException {
        boolean result = internDAO.addIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @Order(6)
    @DisplayName("Debe lanzar DAOException al insertar practicante con matrícula duplicada")
    void testAddInternDuplicateStudentNumber() throws DAOException {
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
    @Order(7)
    @DisplayName("Debe lanzar DAOException al insertar practicante con correo duplicado")
    void testAddInternDuplicateEmail() throws DAOException {
        InternDTO duplicate = new InternDTO();
        duplicate.setStudentNumber("S11111111");
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
    @Order(8)
    @DisplayName("Debe obtener el ID del practicante por matrícula")
    void testObtainIdSuccess() throws DAOException {
        int id = internDAO.obtainId(testIntern.getStudentNumber());
        assertTrue(id > 0);
    }

    @Test
    @Order(9)
    @DisplayName("Debe devolver true si la matrícula existe")
    void testSearchStudentNumberRegisterExists() throws DAOException {
        boolean exists = internDAO.searchStudentNumberRegister(testIntern.getStudentNumber());
        assertTrue(exists);
    }

    @Test
    @Order(10)
    @DisplayName("Debe obtener lista de practicantes activos (no nula)")
    void testObtainAllActiveInternsSuccess() throws DAOException {
        List<InternDTO> interns = internDAO.obtainAllActiveInterns();
        assertNotNull(interns);
    }

    @Test
    @Order(11)
    @DisplayName("Debe inactivar un practicante exitosamente")
    void testInactivateInternSuccess() throws DAOException {
        boolean result = internDAO.inactivateIntern(testIntern);
        assertTrue(result);
    }

    @Test
    @Order(12)
    @DisplayName("Debe obtener el ID incluso después de inactivar al practicante")
    void testObtainIdAfterInactivation() throws DAOException {
        int id = internDAO.obtainId(testIntern.getStudentNumber());
        assertTrue(id > 0);
    }
}