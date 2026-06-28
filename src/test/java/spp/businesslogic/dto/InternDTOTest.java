package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import spp.utils.businessconstants.BusinessConstant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InternDTOTest {

    private InternDTO internDTO;

    @BeforeEach
    void setUp() {
        internDTO = new InternDTO();
    }

    @Test
    @Order(1)
    @DisplayName("Flujo Normal: Debe aceptar una matrícula con formato válido (S seguido de 8 dígitos)")
    void testSetStudentNumberValidFormat() {
        internDTO.setStudentNumber("S12345678");
        assertTrue(internDTO.isValid());
        assertTrue(internDTO.getErrors().isEmpty());
        assertEquals("S12345678", internDTO.getStudentNumber());
    }

    @Test
    @Order(2)
    @DisplayName("Flujo Alterno: Debe rechazar una matrícula que no comience con 'S'")
    void testSetStudentNumberInvalidNoS() {
        internDTO.setStudentNumber("A12345678");
        assertFalse(internDTO.isValid());
        assertEquals(1, internDTO.getErrors().size());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER));
        assertNull(internDTO.getStudentNumber());
    }

    @Test
    @Order(3)
    @DisplayName("Flujo Alterno: Debe rechazar una matrícula con menos de 8 dígitos")
    void testSetStudentNumberInvalidTooShort() {
        internDTO.setStudentNumber("S1234567");
        assertFalse(internDTO.isValid());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER));
        assertNull(internDTO.getStudentNumber());
    }

    @Test
    @Order(4)
    @DisplayName("Flujo Alterno: Debe rechazar una matrícula con más de 8 dígitos")
    void testSetStudentNumberInvalidTooLong() {
        internDTO.setStudentNumber("S123456789");
        assertFalse(internDTO.isValid());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER));
        assertNull(internDTO.getStudentNumber());
    }

    @Test
    @Order(5)
    @DisplayName("Flujo Alterno: Debe rechazar una matrícula con caracteres no numéricos después de la S")
    void testSetStudentNumberInvalidWithLetters() {
        internDTO.setStudentNumber("S1234A678");
        assertFalse(internDTO.isValid());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER));
        assertNull(internDTO.getStudentNumber());
    }

    @Test
    @Order(6)
    @DisplayName("Flujo de Acumulación: Debe acumular errores de validación de campos inválidos (email, password, matrícula)")
    void testAccumulateMultipleErrors() {
        internDTO.setEmail("correo-invalido.com");
        internDTO.setPassword("pass");
        internDTO.setStudentNumber("A12345678");
        assertFalse(internDTO.isValid());
        assertEquals(3, internDTO.getErrors().size());
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_EMAIL));
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_PASSWORD));
        assertTrue(internDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER));
    }

    @Test
    @Order(7)
    @DisplayName("Flujo Normal: Debe ser válido cuando email, password y matrícula son correctos")
    void testAllValidFields() {
        internDTO.setEmail("juan.perez@uv.mx");
        internDTO.setPassword("Juan123!");
        internDTO.setStudentNumber("S12345678");
        assertTrue(internDTO.isValid());
        assertTrue(internDTO.getErrors().isEmpty());
        assertEquals("juan.perez@uv.mx", internDTO.getEmail());
        assertEquals("Juan123!", internDTO.getPassword());
        assertEquals("S12345678", internDTO.getStudentNumber());
    }
}