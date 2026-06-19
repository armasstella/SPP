package spp.businesslogic.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageDTOTest {

    private MessageDTO testMessage;

    @BeforeEach
    void setUpEach() {
        testMessage = new MessageDTO();
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar asunto nulo")
    void testSetSubjectNull() {
        assertThrows(IllegalArgumentException.class, () -> testMessage.setSubject(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con asunto demasiado largo")
    void testSetSubjectTooLong() {
        String longSubject = "A".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> testMessage.setSubject(longSubject));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException al asignar contenido nulo")
    void testSetContentNull() {
        assertThrows(IllegalArgumentException.class, () -> testMessage.setContent(null));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con correo de remitente inválido")
    void testSetEmailSenderInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> testMessage.setEmailSender("correosinarroba"));
    }

    @Test
    @DisplayName("Debe lanzar IllegalArgumentException con correo de destinatario inválido")
    void testSetEmailReceiverInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> testMessage.setEmailReceiver("correosinarroba"));
    }

    @Test
    @DisplayName("Debe aceptar correos con formato válido")
    void testSetEmailsValid() {
        assertDoesNotThrow(() -> {
            testMessage.setEmailSender("remitente@uv.mx");
            testMessage.setEmailReceiver("destinatario@uv.mx");
        });
    }

    @Test
    @DisplayName("Debe aceptar un mensaje con todos sus datos válidos")
    void testSetValidDataSuccess() {
        assertDoesNotThrow(() -> {
            testMessage.setSubject("Duda sobre reporte");
            testMessage.setContent("Hola profesor, tengo una duda sobre la sección 2.");
            testMessage.setEmailSender("zs24013315@estudiantes.uv.mx");
            testMessage.setEmailReceiver("profesor@uv.mx");
        });
    }
}