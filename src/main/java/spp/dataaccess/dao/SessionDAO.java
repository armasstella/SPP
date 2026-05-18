package spp.dataaccess.dao;

import spp.businesslogic.dto.SessionDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ISessionDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import spp.utils.session.Token;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionDAO implements ISessionDAO {
    private static final int NO_ROWS_AFFECTED = 0;

    @Override
    public String createSession(int idUser) throws DAOException {
        String token = Token.generate();
        final String GENERATE_SESSION = "INSERT INTO sesiones(token, id_usuario, expiracion)" +
                "VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 1 HOUR))";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(GENERATE_SESSION);
                preparedStatement.setString(1, token);
                preparedStatement.setInt(2, idUser);
                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Error. No se afectaron filas al generar la sesión.");
                }
                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al crear sesión de usuario");
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos");
        }

        return token;
    }

    @Override
    public SessionDTO searchSession(String token) throws DAOException {
        final String SEARCH_SESSION = "SELECT u.correo_electronico FROM sesiones s" +
                "INNER JOIN usuarios u ON u.id_usuario = s.id_usuario" +
                "WHERE s.token = ? AND s.expiracion > NOW()";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_SESSION);
                preparedStatement.setString(1, token);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    return null;
                }
                return new SessionDTO(resultSet.getString("correo_electronico"));

            } catch (SQLException e) {
                AppLogger.logError(e);
                throw new DAOException("Error al buscar sesión");
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos");
        }
    }

    @Override
    public void deleteSession(String token) throws DAOException {
        final String DELETE_SESSION = "DELETE FROM sesiones WHERE token = ?";
        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SESSION);
                preparedStatement.setString(1, token);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                AppLogger.logError(e);
                throw new DAOException("Error al eliminar sesión");
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos.");
        }
    }
}
