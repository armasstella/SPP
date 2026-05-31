package spp.businesslogic.dao;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IActivityDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;


public class ActivityDAO implements IActivityDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    public ActivityDAO() {

    }

    @Override
    public boolean addActivity(ActivityDTO activityDTO) throws DAOException {
        final String INSERT_ACTIVITY = "INSERT INTO Actividades " +
                "(titulo, descripcion, fecha_limite, id_profesor_usuario, id_profesor_num_personal) VALUES " +
                "(?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACTIVITY);
                preparedStatement.setString(1, activityDTO.getTitle());
                preparedStatement.setString(2, activityDTO.getDescription());
                preparedStatement.setTimestamp(3, Timestamp.valueOf(activityDTO.getSubmissionDate()));
                preparedStatement.setInt(4, activityDTO.getInstructorDTO().getId());
                preparedStatement.setString(5, activityDTO.getInstructorDTO().getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Fallo al insertar la actividad. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar una actividad", e);

            } catch (SQLIntegrityConstraintViolationException e) {
                throw new DAOException("Fallo al insertar la actividad: Restricción de integridad violada", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al insertar una actividad", e);

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

}