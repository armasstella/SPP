package spp.businesslogic.dao;


import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IMessageDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO implements IMessageDAO {

    public MessageDAO() {
    }

    @Override
    public boolean sendMessage(MessageDTO messageDTO) throws DAOException {
        final String INSERT_MESSAGE_WITH_SUBQUERIES = "INSERT INTO Mensajes " +
                "(asunto, contenido, id_usuario_remitente, id_usuario_destinatario, fecha) VALUES " +
                "(?, ?, (SELECT id_usuario FROM Usuarios WHERE correo_electronico = ?), " +
                "(SELECT id_usuario FROM Usuarios WHERE correo_electronico = ?), NOW())";

        boolean isMessageSent = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_MESSAGE_WITH_SUBQUERIES)) {
                preparedStatement.setString(1, messageDTO.getSubject());
                preparedStatement.setString(2, messageDTO.getContent());
                preparedStatement.setString(3, messageDTO.getEmailSender());
                preparedStatement.setString(4, messageDTO.getEmailReceiver());
                isMessageSent = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("Verifique los datos ingresados", e);
        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al guardar el mensaje", e);
        }

        return isMessageSent;

    }

    @Override
    public List<MessageDTO> findMessagesByReceiverEmail(String email) throws DAOException {
        final String SELECT_ALL_MESSAGES = "SELECT m.asunto, m.contenido, m.fecha, remitente.correo_electronico " +
                "FROM Mensajes m INNER JOIN Usuarios destinatario ON m.id_usuario_destinatario =  " +
                "destinatario.id_usuario INNER JOIN Usuarios remitente ON m.id_usuario_remitente = " +
                "remitente.id_usuario WHERE destinatario.correo_electronico = ?";
        List<MessageDTO> messagesList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_MESSAGES)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        MessageDTO messageDTO = new MessageDTO();
                        messageDTO.setSubject(resultSet.getString("asunto"));
                        messageDTO.setContent(resultSet.getString("contenido"));
                        messageDTO.setDate(resultSet.getString("fecha"));
                        messageDTO.setEmailSender(resultSet.getString("correo_electronico"));
                        messagesList.add(messageDTO);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al obtener mensajes");
        }

        return messagesList;

    }

}