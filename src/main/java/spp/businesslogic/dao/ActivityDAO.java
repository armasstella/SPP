package spp.businesslogic.dao;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IActivityDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ActivityDAO implements IActivityDAO {

    public ActivityDAO() {
    }

    @Override
    public boolean saveActivityForIntern(String studentNumber, ActivityDTO activityDTO) throws DAOException {
        final String INSERT_ACTIVITY = "INSERT INTO actividades_practicante " +
                        "(titulo, descripcion, fecha_inicio, fecha_fin, tiempo_estimado, tiempo_efectivo, " +
                        "avance, observaciones, id_usuario_practicante, matricula) " +
                        "SELECT ?, ?, ?, ?, ?, ?, ?, ?, p.id_usuario, p.matricula " +
                        "FROM practicantes p WHERE p.matricula = ?";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACTIVITY)) {
                preparedStatement.setString(1, activityDTO.getTitle());
                preparedStatement.setString(2, activityDTO.getDescription());
                preparedStatement.setDate(3, Date.valueOf(activityDTO.getStartDate()));
                preparedStatement.setDate(4, Date.valueOf(activityDTO.getEndDate()));
                preparedStatement.setInt(5, activityDTO.getEstimatedTime());
                preparedStatement.setInt(6, activityDTO.getEffectiveTime());
                preparedStatement.setInt(7, activityDTO.getProgress());
                preparedStatement.setString(8, activityDTO.getObservations());
                preparedStatement.setString(9, studentNumber);

                isInsertSuccessful = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;

            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al registrar la actividad", e);
        }

        return isInsertSuccessful;

    }

    @Override
    public List<ActivityDTO> findActivitiesByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_ACTIVITIES =
                "SELECT id_actividad_practicante, titulo, descripcion, fecha_inicio, fecha_fin, " +
                        "tiempo_estimado, tiempo_efectivo, avance, observaciones " +
                        "FROM actividades_practicante WHERE matricula = ? ORDER BY fecha_inicio ASC";
        List<ActivityDTO> activityList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACTIVITIES)) {
                preparedStatement.setString(1, studentNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        activityList.add(buildActivityDTOFromResultSet(resultSet));
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al obtener actividades de practicante", e);
        }
        return activityList;

    }

    private ActivityDTO buildActivityDTOFromResultSet(ResultSet resultSet) throws SQLException {
        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setId(resultSet.getInt("id_actividad_practicante"));
        activityDTO.setTitle(resultSet.getString("titulo"));
        activityDTO.setDescription(resultSet.getString("descripcion"));
        activityDTO.setStartDate(resultSet.getDate("fecha_inicio").toLocalDate());
        activityDTO.setEndDate(resultSet.getDate("fecha_fin").toLocalDate());
        activityDTO.setEstimatedTime(resultSet.getInt("tiempo_estimado"));
        activityDTO.setEffectiveTime(resultSet.getInt("tiempo_efectivo"));
        activityDTO.setProgress(resultSet.getInt("avance"));
        activityDTO.setObservations(resultSet.getString("observaciones"));
        return activityDTO;

    }

    @Override
    public boolean updateActivity(ActivityDTO activity) throws DAOException {
        boolean isActivityUpdated = false;
        final String UPDATE_ACTIVITY =
                "UPDATE actividades_practicante SET titulo = ?, descripcion = ?, fecha_inicio = ?, " +
                        "fecha_fin = ?, tiempo_estimado = ?, tiempo_efectivo = ?, avance = ?, observaciones = ? " +
                        "WHERE id_actividad_practicante = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ACTIVITY)) {
                preparedStatement.setString(1, activity.getTitle());
                preparedStatement.setString(2, activity.getDescription());
                preparedStatement.setDate(3, Date.valueOf(activity.getStartDate()));
                preparedStatement.setDate(4, Date.valueOf(activity.getEndDate()));
                preparedStatement.setInt(5, activity.getEstimatedTime());
                preparedStatement.setInt(6, activity.getEffectiveTime());
                preparedStatement.setInt(7, activity.getProgress());
                preparedStatement.setString(8, activity.getObservations());
                preparedStatement.setInt(9, activity.getId());
                isActivityUpdated = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión actualizar la actividad", e);
        }

        return isActivityUpdated;

    }

    @Override
    public boolean deleteActivity(int idActivity) throws DAOException {
        boolean isActivityDeleted = false;
        final String DELETE_ACTIVITY =
                "DELETE FROM actividades_practicante WHERE id_actividad_practicante = ?";
        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ACTIVITY)) {
                preparedStatement.setInt(1, idActivity);
                isActivityDeleted = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión eliminar la actividad", e);
        }

        return isActivityDeleted;

    }

}