package spp.dataaccess.dao;

import spp.businesslogic.dto.ActiveSession;
import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IMessageDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO implements IMessageDAO {
    private static final int NO_ROWS_AFFECTED = 0;
    private final UserDAO userDAO = new UserDAO();

    public MessageDAO() {

    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO) throws DAOException {
        final String INSERT_MESSAGE = "INSERT INTO Mensajes " +
                "(asunto, contenido, id_usuario_remitente, id_usuario_destinatario, fecha) VALUES " +
                "(?, ?, ?, ?, NOW())";

        String email = ActiveSession.get().getEmail();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE);
                preparedStatement.setString(1, messageDTO.getSubject());
                preparedStatement.setString(2, messageDTO.getContent());
                preparedStatement.setInt(3, userDAO.obtainId(email));
                preparedStatement.setInt(4, userDAO.obtainId(messageDTO.getEmailReceiver()));

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Fallo al enviar el mensaje. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al enviar mensaje", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Integridad de datos violada al enviar mensaje", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al enviar mensaje", e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }
        return true;
    }

    public List<MessageDTO> obtainMessagesForUser() throws DAOException {
        List<MessageDTO> messagesList = new ArrayList<>();
        final String SELECT_ALL_MESSAGES = "SELECT m.asunto, m.contenido, m.fecha, remitente.correo_electronico " +
                "FROM Mensajes m INNER JOIN Usuarios destinatario ON m.id_usuario_destinatario = destinatario.id_usuario " +
                "INNER JOIN Usuarios remitente ON m.id_usuario_remitente = remitente.id_usuario " +
                "WHERE destinatario.correo_electronico = ?";
        String email = ActiveSession.get().getEmail();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_MESSAGES);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                MessageDTO messageDTO = new MessageDTO();
                messageDTO.setSubject(resultSet.getString("asunto"));
                messageDTO.setContent(resultSet.getString("contenido"));
                messageDTO.setDate(resultSet.getString("fecha"));
                messageDTO.setEmailSender(resultSet.getString("correo_electronico"));
                messagesList.add(messageDTO);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos");
        }
        return messagesList;
    }

}