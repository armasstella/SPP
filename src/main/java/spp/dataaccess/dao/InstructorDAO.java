package spp.dataaccess.dao;

import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInstructorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;

public class InstructorDAO implements IInstructorDAO {

    private final UserDAO userDAO = new UserDAO();

    public InstructorDAO() {

    }

    @Override
    public boolean addInstructor(InstructorDTO instructorDTO) throws DAOException {
        final String INSERT_INSTRUCTOR = "INSERT INTO Profesores " +
                "(id_usuario, num_personal, turno) VALUES (?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.addUser(instructorDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSTRUCTOR);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, instructorDTO.getPersonalNumber());
                preparedStatement.setString(3, instructorDTO.getShift());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Fallo al insertar al profesor. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el usuario", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new SQLIntegrityConstraintViolationException("Error al insertar el profesor: Datos duplicados", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el profesor", e);
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
    public boolean login(String personalNumber, String password) throws DAOException {
        final String SELECT_LOGIN =
                "SELECT P.id_usuario " +
                        "FROM Profesores P " +
                        "INNER JOIN Usuarios U ON P.id_usuario = U.id_usuario " +
                        "WHERE P.num_personal = ? AND U.contraseña = ? AND U.estado = 'Activo'";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LOGIN);
            preparedStatement.setString(1, personalNumber);
            preparedStatement.setString(2, password);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al verificar credenciales de instructor", e);
        }
    }

}