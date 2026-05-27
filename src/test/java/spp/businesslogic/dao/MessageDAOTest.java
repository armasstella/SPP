package spp.businesslogic.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.enums.MesaggeStatus;
import spp.businesslogic.exceptions.DAOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

public class MessageDAOTest {
    private MessageDAO messageDAO;
    private MessageDTO testMessage;

    @BeforeAll
    void setUp() {
        messageDAO = new MessageDAO();
    }

    @BeforeEach
    void setUpEach() {
        testMessage = new MessageDTO();
        testMessage.setContent("Hola");
        testMessage.setMessageStatus(MesaggeStatus.ENVIADO);
        testMessage.setReceiver(1);
        testMessage.setSender(7);
    }

    @Test
    @DisplayName("Prueba que debe Enviar un Mensaje")
    void testSendMessageSuccess() throws DAOException {
        boolean result = messageDAO.sendMessage(testMessage);
        assertTrue(result, "El Mensaje se ha enviado");
    }
}