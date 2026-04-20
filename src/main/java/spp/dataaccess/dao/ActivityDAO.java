package spp.dataaccess.dao;

import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.LogicLayerException;
import spp.businesslogic.interfaces.IActivityDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;

public class ActivityDAO implements IActivityDAO {

    public ActivityDAO() {

    }

    @Override
    public void addActivity(ActivityDTO activityDTO) throws DAOException {
        final String INSERT_ACTIVITY = "INSERT INTO Actividad " +
                "(titulo, descripcion, fecha_limite, id_profesor_usuario, id_profesor_num_personal) VALUES " +
                "(?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACTIVITY);
                preparedStatement.setString(1, activityDTO.getTitle());
                preparedStatement.setString(2, activityDTO.getDescription());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(activityDTO.getDeadline()));
                preparedStatement.setInt(4, 1);
                preparedStatement.setInt(5, 1);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new LogicLayerException("Fallo al insertar la actividad. No se afectaron filas.");
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
    }

    public static void main(String[] args) {

    }
}