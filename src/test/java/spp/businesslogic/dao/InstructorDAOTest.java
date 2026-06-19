package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class InstructorDAOTest {

    private InstructorDAO instructorDAO;
    private InstructorDTO testInstructor;
    private String uniqueSuffix;

    @BeforeAll
    void setUpAll() {
        instructorDAO = new InstructorDAO();
        testInstructor = new InstructorDTO();
    }

    @BeforeEach
    void setUpEach() {
        uniqueSuffix = String.valueOf(System.currentTimeMillis());
        String uniquePersonalNumber = "P" + uniqueSuffix.substring(uniqueSuffix.length() - 4);
        String uniqueEmail = "memo" + uniqueSuffix.substring(uniqueSuffix.length() - 8) + "@gmail.com";
        String uniquePhone = "22" + uniqueSuffix.substring(uniqueSuffix.length() - 8);

        testInstructor.setStatus("Activo");
        testInstructor.setLastConnection("2025-11-22 19:15:13");
        testInstructor.setFirstName("Guillermo");
        testInstructor.setSecondName("");
        testInstructor.setFirstLastName("González");
        testInstructor.setSecondLastName("Hernández");
        testInstructor.setEmail(uniqueEmail);
        testInstructor.setPhoneNumber(uniquePhone);
        testInstructor.setPassword("Pass123!");
        testInstructor.setPersonalNumber(uniquePersonalNumber);
        testInstructor.setShift("Matutino");
    }

    @Test
    @Order(1)
    @DisplayName("Debe lanzar DAOException al insertar profesor nulo")
    void testAddInstructorNullDTO() {
        assertThrows(DAOException.class, () -> instructorDAO.addInstructor(null));
    }

    @Test
    @Order(2)
    @DisplayName("Debe insertar un profesor exitosamente")
    void testAddInstructorSuccess() throws DAOException {
        boolean result = instructorDAO.addInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @Order(3)
    @DisplayName("Debe lanzar DAOException al insertar profesor con número personal duplicado")
    void testAddInstructorDuplicatePersonalNumber() throws DAOException {
        InstructorDTO duplicate = new InstructorDTO();
        duplicate.setPersonalNumber(testInstructor.getPersonalNumber());
        duplicate.setEmail("jimenez" + uniqueSuffix + "@gmail.com");
        duplicate.setFirstName("José");
        duplicate.setFirstLastName("Jiménez");
        duplicate.setPhoneNumber("1234567890");
        duplicate.setPassword("Pass123!");
        duplicate.setShift("Vespertino");

        assertThrows(DAOException.class, () -> instructorDAO.addInstructor(duplicate));
    }

    @Test
    @Order(4)
    @DisplayName("Debe obtener el ID del profesor por número personal")
    void testObtainIdSuccess() throws DAOException {
        int id = instructorDAO.obtainId(testInstructor.getPersonalNumber());
        assertTrue(id > 0);
    }

    @Test
    @Order(5)
    @DisplayName("Debe lanzar DAOException al buscar número personal inexistente")
    void testObtainIdNotFound() {
        assertThrows(DAOException.class, () -> instructorDAO.obtainId("Z9999"));
    }

    @Test
    @Order(6)
    @DisplayName("Debe desactivar un profesor exitosamente")
    void testDeactivateInstructorSuccess() throws DAOException {
        boolean result = instructorDAO.deactivateInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @Order(7)
    @DisplayName("Debe lanzar DAOException al desactivar profesor inexistente")
    void testDeactivateInstructorNotFound() {
        InstructorDTO fake = new InstructorDTO();
        fake.setPersonalNumber("Z9999");
        assertThrows(DAOException.class, () -> instructorDAO.deactivateInstructor(fake));
    }

    @Test
    @Order(8)
    @DisplayName("Debe obtener lista de profesores activos")
    void testObtainAllActiveInstructors() throws DAOException {
        var list = instructorDAO.obtainAllActiveInstructors();
        assertNotNull(list);
    }

    @Test
    @Order(9)
    @DisplayName("Después de desactivar, no debe aparecer en la lista de activos")
    void testObtainAllActiveInstructorsExcludesInactive() throws DAOException {
        var list = instructorDAO.obtainAllActiveInstructors();
        boolean found = list.stream().anyMatch(i ->
                i.getPersonalNumber().equals(testInstructor.getPersonalNumber()));
        assertFalse(found);
    }

    @Test
    @Order(10)
    @DisplayName("Debe obtener lista resumida de profesores activos")
    void testObtainActiveInstructorForComboBox() throws DAOException {
        List<InstructorDTO> list = instructorDAO.obtainActiveInstructorForComboBox();
        assertNotNull(list);
    }

    @Test
    @Order(11)
    @DisplayName("Después de insertar un profesor activo, debe aparecer en la lista resumida")
    void testObtainActiveInstructorForComboBoxIncludesNew() throws DAOException {
        testInstructor.setPersonalNumber("P2222");
        testInstructor.setEmail("profe_nuevo@gmail.com");
        instructorDAO.addInstructor(testInstructor);
        List<InstructorDTO> list = instructorDAO.obtainActiveInstructorForComboBox();
        boolean found = list.stream().anyMatch(i ->
                i.getPersonalNumber().equals(testInstructor.getPersonalNumber()));
        assertTrue(found);
    }
}