package spp.businesslogic.dao;

import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.enums.MesaggeStatus;
import spp.businesslogic.exceptions.DataAccessException;
import spp.businesslogic.interfaces.IMessageDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class MessageDAO implements IMessageDAO {

    public MessageDAO() {

    }

    @Override
    public void sendMessage(MessageDTO messageDTO) {
        final String INSERT_MESSAGE = "INSERT INTO Mensaje " +
                "(contenido, estado, id_usuario_remitente, id_usuario_destinatario) VALUES " +
                "(?, ?, ?, ?)";

        MySQLConnection database = new MySQLConnection();
        Connection connection = null;

        try {
            connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE);
            preparedStatement.setString(1, messageDTO.getContent());
            preparedStatement.setString(2, String.valueOf(messageDTO.getMessageStatus()));
            preparedStatement.setInt(3, 1);
            preparedStatement.setInt(4, 6);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Fallo al enviar el mensaje. No se afectaron filas.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error de integridad al enviar mensaje", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error al enviar mensaje", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    AppLogger.logError(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        MessageDAO messageDAO = new MessageDAO();

        System.out.println("=== Caso 1: Envío de mensaje válido ===");
        try {
            MessageDTO message = new MessageDTO();
            message.setContent("Hola, ¿puedes revisar mi avance de prácticas?");
            message.setMessageStatus(MesaggeStatus.PENDIENTE);

            messageDAO.sendMessage(message);
            System.out.println("Mensaje enviado correctamente.");
        } catch (DataAccessException e) {
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

}