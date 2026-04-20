package spp.dataaccess.dao;

import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.LogicLayerException;
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
    public boolean sendMessage(MessageDTO messageDTO) throws DAOException {
        final String INSERT_MESSAGE = "INSERT INTO Mensajes " +
                "(contenido, estado, id_usuario_remitente, id_usuario_destinatario) VALUES " +
                "(?, ?, ?, ?)";
        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE);
                preparedStatement.setString(1, messageDTO.getContent());
                preparedStatement.setString(2, String.valueOf(messageDTO.getMessageStatus()));
                preparedStatement.setInt(3, messageDTO.getSender());
                preparedStatement.setInt(4, messageDTO.getReceiver());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new LogicLayerException("Fallo al enviar el mensaje. No se afectaron filas.");
                }

                connection.commit();

            } catch (LogicLayerException | SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw DAOException.insertError(e);
        }
        return true;
    }
}