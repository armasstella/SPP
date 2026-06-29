package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import spp.utils.businessconstants.BusinessConstant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TermDTOTest {

    private TermDTO termDTO;

    @BeforeEach
    void setUp() {
        termDTO = new TermDTO();
    }

    @Test
    @DisplayName("Formato Válido: FEBRERO-JULIO-2026")
    void testSetNameValidFebJul() {
        termDTO.setName("FEBRERO - JULIO 26");
        assertTrue(termDTO.isValid(), "El formato FEBRERO - JULIO - 2026 debería ser válido");
    }

    @Test
    @DisplayName("Formato Válido: AGOSTO - ENERO 26")
    void testSetNameValidAgoEne() {
        termDTO.setName("AGOSTO - ENERO 26");
        assertTrue(termDTO.isValid(), "El formato AGOSTO - ENERO - 2026 debería ser válido");
    }

    @Test
    @DisplayName("Inválido: Prefijo incorrecto (ej. MARZO-ABRIL)")
    void testSetNameInvalidPrefix() {
        termDTO.setName("MARZO-ABRIL-26");
        assertFalse(termDTO.isValid(), "El prefijo MARZO-ABRIL no debería ser aceptado");
        assertTrue(termDTO.getErrors().contains(BusinessConstant.MESSAGE_INVALID_TERM));
    }

    @Test
    @DisplayName("Inválido: Formato de año corto (YY)")
    void testSetNameInvalidYearFormat() {
        termDTO.setName("FEBRERO-JULIO-26");
        assertFalse(termDTO.isValid(), "El año en formato YY no debería ser aceptado");
    }

    @Test
    @DisplayName("Inválido: Falta de guiones")
    void testSetNameMissingHyphens() {
        termDTO.setName("FEBREROJULIO2026");
        assertFalse(termDTO.isValid(), "Un formato sin guiones debería ser rechazado");
    }

    @Test
    @DisplayName("Acumulación: Debe registrar múltiples errores")
    void testAccumulateErrors() {
        termDTO.setName("INVALIDO-01");
        termDTO.setName("INVALIDO-02");

        assertFalse(termDTO.isValid());
        assertEquals(2, termDTO.getErrors().size(), "Debería haber registrado 2 errores acumulados");
    }
}