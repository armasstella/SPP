package spp.businesslogic.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.dto.SessionDTO;
import spp.businesslogic.enums.MesaggeStatus;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MessageDAOTest {

    private MessageDAO messageDAO;
    private MessageDTO testMessage;
    private SessionDTO activeSession;

    @BeforeAll
    void setUp() {
        messageDAO = new MessageDAO();
        testMessage = new MessageDTO();
        //activeSession = new SessionDTO("zs24013315@estudiantes.uv.mx", );
    }

    @BeforeEach
    void setUpEach() {
        ActiveSessionDTO.initialize(activeSession);
        testMessage.setSubject("Saludo");
        testMessage.setContent("Hola, cambia tu contraseña");
        testMessage.setReceiver(28);
        testMessage.setSender(31);
        testMessage.setEmailReceiver("zs24013261@estudiantes.uv.mx");
    }

    @AfterEach
    void tearDownEach() {
        ActiveSessionDTO.close();
    }

    @Test
    @DisplayName("Prueba que debe Enviar un Mensaje")
    void testSendMessageSuccess() throws DAOException {
        boolean result = messageDAO.sendMessage(testMessage);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe permitir enviar mensaje al mismo usuario")
    void testSendMessageToSelfSuccess() throws DAOException {
        testMessage.setReceiver(testMessage.getSender());
        boolean result = messageDAO.sendMessage(testMessage);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe recuperar correctamente los mensajes de un usuario")
    void testGetMessagesByUserSuccess() throws DAOException {
        messageDAO.sendMessage(testMessage);
        //List<MessageDTO> messages = messageDAO.obtainMessagesForUser();
        //assertFalse(messages.isEmpty());
    }

    @Test
    @DisplayName("Debe permitir enviar mensaje con caracteres especiales")
    void testSendMessageWithSpecialCharactersSuccess() throws DAOException {
        testMessage.setContent("Mensaje áéíóú ñ ¿? ¡! #$%");
        boolean result = messageDAO.sendMessage(testMessage);
        assertTrue(result);
    }

    @Test
    @DisplayName("Debe enviar correctamente dos mensajes consecutivos")
    void testSendMultipleMessagesSuccess() throws DAOException {
        boolean firstResult = messageDAO.sendMessage(testMessage);
        testMessage.setSubject("Segundo mensaje");
        boolean secondResult = messageDAO.sendMessage(testMessage);
        assertTrue(firstResult);
        assertTrue(secondResult);
    }

    @Test
    @DisplayName("Debe lanzar DAOException al enviar mensaje sin contenido")
    void testSendMessageFailedEmptyContent() throws DAOException {
        testMessage.setContent("");
        assertThrows(DAOException.class, () -> {
            messageDAO.sendMessage(testMessage);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al enviar mensaje sin remitente")
    void testSendMessageFailedNullSender() throws DAOException {
        testMessage.setSender(0);
        assertThrows(DAOException.class, () -> {
            messageDAO.sendMessage(testMessage);
        });
    }

    @Test
    @DisplayName("Debe lanzar DAOException al enviar mensaje sin destinatario")
    void testSendMessageFailedNullRecipientEmail() throws DAOException {
        testMessage.setEmailReceiver(null);
        assertThrows(DAOException.class, () -> {
            messageDAO.sendMessage(testMessage);
        });
    }

    @Test
    @DisplayName("Debe lanzar error al intentar enviar un mensaje con asunto demasiado largo")
    void testSendMessageFailedLongSubject() {
        testMessage.setSubject("A".repeat(300));
        assertThrows(DAOException.class, () -> {
            messageDAO.sendMessage(testMessage);
        });
    }
}