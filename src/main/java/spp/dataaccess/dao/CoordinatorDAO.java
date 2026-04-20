package spp.dataaccess.dao;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ICoordinatorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CoordinatorDAO implements ICoordinatorDAO {

    private final UserDAO userDAO = new UserDAO();

    public CoordinatorDAO() {

    }

    @Override
    public boolean addCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {
        final String INSERT_COORDINATOR = "INSERT INTO Coordinadores " +
                "(id_usuario, num_personal) VALUES (?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.insertUser(coordinatorDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COORDINATOR);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, coordinatorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Error. No se afectaron filas al insertar coordinador.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el usuario", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error. Datos duplicados al insertar.", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el coordinador", e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }

        return true;
    }

    @Override
    public boolean inactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {

        final String INACTIVATE_COORDINATOR = "UPDATE Usuarios " +
                "INNER JOIN Coordinadores ON Usuarios.id_usuario = Coordinadores.id_usuario " +
                "SET Usuarios.estado = 'Inactivo' " +
                "WHERE Coordinadores.num_personal = ?;";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INACTIVATE_COORDINATOR);
                preparedStatement.setString(1, coordinatorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Error. No se afectaron filas al inactivar el coordinador.");
                }

                connection.commit();

            } catch (SQLException | DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al inactivar el coordinador", e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }

        return true;
    }

    @Override
    public boolean activateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {
        final String ACTIVATE_COORDINATOR = "UPDATE Usuarios " +
                "INNER JOIN Coordinadores ON Usuarios.id_usuario = Coordinadores.id_usuario " +
                "SET Usuarios.estado = 'Activo' " +
                "WHERE Coordinadores.num_personal = ?;";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(ACTIVATE_COORDINATOR);
                preparedStatement.setString(1, coordinatorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Error. No se afectaron filas al activar el coordinador.");
                }

                connection.commit();

            } catch (SQLException | DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al activar el coordinador", e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }

        return true;
    }

}