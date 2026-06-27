package spp.businesslogic.dao;


import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ICoordinatorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.dataaccess.connection.MySQLConnectionManager;
import spp.utils.exceptionmanager.ExceptionLevel;
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
    public boolean registerCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {
        final String INSERT_COORDINATOR = "INSERT INTO Coordinadores " +
                "(id_usuario, num_personal) VALUES (?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            MySQLConnectionManager.getInstance().disableAutoCommitConnection();
            int generatedId = userDAO.registerUser(coordinatorDTO);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COORDINATOR)) {
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, coordinatorDTO.getPersonalNumber());

                if (preparedStatement.executeUpdate() != NO_ROWS_AFFECTED) {
                    isInsertSuccessful = true;
                    connection.commit();
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("Verifique los datos ingresados", e);

        } catch (DAOException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(ExceptionLevel.ERROR, e);
            throw new DAOException("Error al insertar coordinador", e);

        } catch (SQLException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al insertar coordinador", e);

        } finally {
            MySQLConnectionManager.getInstance().enableAutoCommitConnection();
        }

        return isInsertSuccessful;

    }

    @Override
    public boolean deactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {
        final String INACTIVATE_COORDINATOR = "UPDATE Usuarios " +
                "INNER JOIN Coordinadores ON Usuarios.id_usuario = Coordinadores.id_usuario " +
                "SET Usuarios.estado = 'Inactivo' " +
                "WHERE Coordinadores.num_personal = ?;";
        boolean isDeactivationSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(INACTIVATE_COORDINATOR)) {
                preparedStatement.setString(1, coordinatorDTO.getPersonalNumber());
                isDeactivationSuccessful = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al inactivar coordinador", e);
        }

        return isDeactivationSuccessful;

    }

    @Override
    public boolean existsActiveCoordinatorByPersonalNumber(String personalNumber) throws DAOException {
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
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al buscar coordinadores", e);
        }

    }

    @Override
    public List<CoordinatorDTO> getActiveCoordinators() throws DAOException {
        final String SELECT_ALL_COORDINATORS = "SELECT nombre, apellidos, correo_electronico, num_personal " +
                "FROM Usuarios u INNER JOIN Coordinadores c ON u.id_usuario = c.id_usuario AND u.estado = 'Activo'";
        List<CoordinatorDTO> coordinatorsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COORDINATORS);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    CoordinatorDTO coordinatorDTO = new CoordinatorDTO();
                    coordinatorDTO.setFirstName(resultSet.getString("nombre"));
                    coordinatorDTO.setFirstLastName(resultSet.getString("apellidos"));
                    coordinatorDTO.setEmail(resultSet.getString("correo_electronico"));
                    coordinatorDTO.setPersonalNumber(resultSet.getString("num_personal"));
                    coordinatorsList.add(coordinatorDTO);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al buscar coordinadores", e);
        }

        return coordinatorsList;

    }

}