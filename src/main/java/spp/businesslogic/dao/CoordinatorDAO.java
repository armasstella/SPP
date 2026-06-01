package spp.businesslogic.dao;


import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ICoordinatorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class CoordinatorDAO implements ICoordinatorDAO {

    private static final int NO_ROWS_AFFECTED = 0;
    private final UserDAO userDAO = new UserDAO();

    public CoordinatorDAO() {

    }

    @Override
    public boolean addCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {
        final String INSERT_COORDINATOR = "INSERT INTO Coordinadores " +
                "(id_usuario, num_personal) VALUES (?, ?)";
        boolean isAddSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.addUser(coordinatorDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COORDINATOR);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, coordinatorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("WARN: Fallo al insertar coordinador. No se afectaron filas");
                }

                connection.commit();
                isAddSuccesful = true;

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error al insertar coordinador", e);

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("WARN: Violación de integridad de datos al insertar", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error general al insertar coordinador", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al insertar coordinador", e);
        }

        return isAddSuccesful;

    }

    @Override
    public boolean inactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {
        final String INACTIVATE_COORDINATOR = "UPDATE Usuarios " +
                "INNER JOIN Coordinadores ON Usuarios.id_usuario = Coordinadores.id_usuario " +
                "SET Usuarios.estado = 'Inactivo' " +
                "WHERE Coordinadores.num_personal = ?;";
        boolean isDeactivationSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INACTIVATE_COORDINATOR);
                preparedStatement.setString(1, coordinatorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("WARN: Fallo al inactivar coordinador. No se afectaron filas");
                }

                connection.commit();
                isDeactivationSuccesful = true;

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error general al inactivar coordinador", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al inactivar coordinador", e);
        }

        return isDeactivationSuccesful;

    }

    @Override
    public boolean activateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {
        final String ACTIVATE_COORDINATOR = "UPDATE Usuarios " +
                "INNER JOIN Coordinadores ON Usuarios.id_usuario = Coordinadores.id_usuario " +
                "SET Usuarios.estado = 'Activo' " +
                "WHERE Coordinadores.num_personal = ?;";
        boolean isActivationSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(ACTIVATE_COORDINATOR);
                preparedStatement.setString(1, coordinatorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("WARN: Fallo al activar coordinador. No se afectaron filas");
                }

                connection.commit();
                isActivationSuccesful = true;

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error general al activar coordinador", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al activar coordinador", e);
        }

        return isActivationSuccesful;

    }

    @Override
    public boolean existCoordinator(String personalNumber) throws DAOException {
        final String SELECT_EXISTS = "SELECT C.id_usuario " +
                        "FROM Coordinadores C " +
                        "INNER JOIN Usuarios U ON C.id_usuario = U.id_usuario " +
                        "WHERE C.num_personal = ? AND U.estado = 'Activo'";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EXISTS);
            preparedStatement.setString(1, personalNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar coordinadores", e);
        }

    }

    @Override
    public List<CoordinatorDTO> obtainAllActiveCoordinators() throws DAOException {
        List<CoordinatorDTO> coordinatorsList = new ArrayList<>();
        final String SELECT_ALL_COORDINATORS = "SELECT nombre, apellidos, correo_electronico, num_personal " +
                "FROM Usuarios u INNER JOIN Coordinadores c ON u.id_usuario = c.id_usuario AND u.estado = 'Activo'";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COORDINATORS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
                coordinatorDTO.setFirstName(resultSet.getString("nombre"));
                coordinatorDTO.setFirstLastName(resultSet.getString("apellidos"));
                coordinatorDTO.setEmail(resultSet.getString("correo_electronico"));
                coordinatorDTO.setPersonalNumber(resultSet.getString("num_personal"));
                coordinatorsList.add(coordinatorDTO);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar coordinadores", e);
        }

        return coordinatorsList;

    }

}