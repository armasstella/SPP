package spp.dataaccess.dao;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.LogicLayerException;
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
        final String INSERT_COORDINATOR = "INSERT INTO coordinador " +
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
                    throw new LogicLayerException("Error. No se afectaron filas al insertar coordinador.");
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
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw DAOException.insertError(e);
        }

        return true;
    }

    @Override
    public boolean inactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException {

        final String INACTIVATE_COORDINATOR = "UPDATE coordinador " +
                "SET estado = 'inactivo' WHERE num_personal = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INACTIVATE_COORDINATOR);
                preparedStatement.setString(1, coordinatorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new LogicLayerException("Error. No se afectaron filas al insertar coordinador.");
                }

                connection.commit();

            } catch (SQLException | LogicLayerException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw DAOException.insertError(e);
        }

        return true;
    }

}