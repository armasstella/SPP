package spp.businesslogic.dao;

import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DataAccessException;
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
    public void addActivity(ActivityDTO activityDTO) {
        final String INSERT_ACTIVITY = "INSERT INTO Actividad " +
                "(titulo, descripcion, fecha_limite, id_profesor_usuario, id_profesor_num_personal) VALUES " +
                "(?, ?, ?, ?, ?)";

        MySQLConnection database = new MySQLConnection();
        Connection connection = null;

        try {
            connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACTIVITY);
            preparedStatement.setString(1, activityDTO.getTitle());
            preparedStatement.setString(2, activityDTO.getDescription());
            preparedStatement.setTimestamp(3, Timestamp.valueOf(activityDTO.getDeadline()));
            preparedStatement.setInt(4, 1);
            preparedStatement.setInt(5, 1);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Fallo al insertar la actividad. No se afectaron filas.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error de integridad al insertar actividad", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error al insertar actividad", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    AppLogger.logError(e);
                }
            }
        }
    }

    public static void main(String[] args) {

    }
}