package spp.dataaccess.dao;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInternDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.ResultSet;

public class InternDAO implements IInternDAO {

    private final UserDAO userDAO = new UserDAO();

    public InternDAO() {

    }

    @Override
    public boolean addIntern(InternDTO internDTO) throws DAOException {
        final String INSERT_INTERN = "INSERT INTO Practicantes " +
                "(id_usuario, matricula, sexo, habla_lengua_indigena, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.addUser(internDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTERN);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, internDTO.getStudentNumber());
                preparedStatement.setString(3, internDTO.getGender());
                preparedStatement.setBoolean(4, internDTO.getSpeaksIndigenousLanguage());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(internDTO.getBirthDate()));

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Fallo al insertar al practicante. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el usuario", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new SQLIntegrityConstraintViolationException("Error al insertar el usuario: Datos duplicados", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el practicante", e);
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

    @Override
    public boolean login(String matricula, String password) throws DAOException {
        final String SELECT_LOGIN =
                "SELECT P.id_usuario " +
                        "FROM Practicantes P " +
                        "INNER JOIN Usuarios U ON P.id_usuario = U.id_usuario " +
                        "WHERE P.matricula = ? AND U.contraseña = ? AND U.estado = 'Activo'";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LOGIN);
            preparedStatement.setString(1, matricula);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al verificar credenciales de practicante", e);
        }
    }

}