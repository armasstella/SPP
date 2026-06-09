package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
    @DisplayName("Debe insertar un profesor exitosamente")
    void testAddInstructorSuccess() throws DAOException {
        boolean result = instructorDAO.addInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar profesor con segundo nombre nulo")
    void testAddInstructorWithNullSecondName() throws DAOException {
        testInstructor.setSecondName(null);
        boolean result = instructorDAO.addInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe insertar profesor con segundo apellido vacío")
    void testAddInstructorWithEmptySecondLastName() throws DAOException {
        testInstructor.setSecondLastName("");
        boolean result = instructorDAO.addInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al insertar profesor con número personal duplicado")
    void testAddInstructorDuplicatePersonalNumber() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
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
    @DisplayName("Debe lanzar IllegalArgumentException al insertar profesor con número personal inválido " +
            "(minúsculas)")
    void testAddInstructorInvalidPersonalNumberFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            testInstructor.setPersonalNumber("abc12");
            instructorDAO.addInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al insertar profesor con turno nulo")
    void testAddInstructorNullShift() {
        assertThrows(IllegalArgumentException.class, () -> {
            testInstructor.setShift(null);
            instructorDAO.addInstructor(testInstructor);
        });
    }

    @Test
    @DisplayName("Debe obtener el ID del profesor por número personal")
    void testObtainIdSuccess() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        int id = instructorDAO.obtainId(testInstructor.getPersonalNumber());
        assertTrue(id > 0);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al buscar número personal inexistente")
    void testObtainIdNotFound() {
        assertThrows(DAOException.class, () -> instructorDAO.obtainId("Z9999"));
    }

    @Test
    @DisplayName("Debe desactivar un profesor exitosamente")
    void testDeactivateInstructorSuccess() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        boolean result = instructorDAO.deactivateInstructor(testInstructor);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al desactivar profesor inexistente")
    void testDeactivateInstructorNotFound() {
        InstructorDTO fake = new InstructorDTO();
        fake.setPersonalNumber("Z9999");
        assertThrows(DAOException.class, () -> instructorDAO.deactivateInstructor(fake));
    }

    @Test
    @DisplayName("Debe obtener lista de profesores activos")
    void testObtainAllActiveInstructors() throws DAOException {
        var list = instructorDAO.obtainAllActiveInstructors();
        assertNotNull(list);
    }

    @Test
    @DisplayName("Después de insertar un profesor activo, debe aparecer en la lista")
    void testObtainAllActiveInstructorsIncludesNew() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        var list = instructorDAO.obtainAllActiveInstructors();
        boolean found = list.stream().anyMatch(i ->
                i.getPersonalNumber().equals(testInstructor.getPersonalNumber()));
        assertTrue(found);
    }

    @Test
    @DisplayName("Después de desactivar, no debe aparecer en la lista de activos")
    void testObtainAllActiveInstructorsExcludesInactive() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        instructorDAO.deactivateInstructor(testInstructor);
        var list = instructorDAO.obtainAllActiveInstructors();
        boolean found = list.stream().anyMatch(i ->
                i.getPersonalNumber().equals(testInstructor.getPersonalNumber()));
        assertFalse(found);
    }

    @Test
    @DisplayName("Debe obtener lista resumida de profesores activos")
    void testObtainActiveInstructorForComboBox() throws DAOException {
        var list = instructorDAO.obtainActiveInstructorForComboBox();
        assertNotNull(list);
    }

    @Test
    @DisplayName("Después de insertar un profesor, debe aparecer en la lista resumida")
    void testObtainActiveInstructorForComboBoxIncludesNew() throws DAOException {
        instructorDAO.addInstructor(testInstructor);
        var list = instructorDAO.obtainActiveInstructorForComboBox();
        boolean found = list.stream().anyMatch(i ->
                i.getPersonalNumber().equals(testInstructor.getPersonalNumber()));
        assertTrue(found);
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al exceder longitud máxima en número personal (>5)")
    void testSetPersonalNumberExceedsMaxLength() {
        assertThrows(IllegalArgumentException.class, () -> testInstructor.setPersonalNumber("ABCDEF"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al exceder longitud máxima en turno (>45)")
    void testSetShiftExceedsMaxLength() {
        assertThrows(IllegalArgumentException.class, () -> testInstructor.setShift("A".repeat(46)));
    }

    @Test
    @DisplayName("Debe aceptar turno con longitud máxima de 45 caracteres")
    void testSetShiftMaxLength() {
        assertDoesNotThrow(() -> testInstructor.setShift("A".repeat(45)));
    }
}