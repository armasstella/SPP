package spp.businesslogic.dao;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.enums.ActivityType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IActivityDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.List;


public class ActivityDAO implements IActivityDAO {

    public ActivityDAO() {
    }

    @Override
    public boolean saveActivityForIntern(String studentNumber, ActivityDTO activityDTO, ActivityType activityType) throws DAOException {
        final String INSERT_ACTIVITY = "INSERT INTO actividades_practicante " +
                "(titulo, descripcion, fecha_inicio, fecha_fin, tiempo_estimado, tiempo_efectivo, " +
                "avance, observaciones, id_usuario_practicante, matricula, tipo) " +
                "SELECT ?, ?, ?, ?, ?, ?, ?, ?, p.id_usuario, p.matricula, ?" +
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
                preparedStatement.setString(9, activityType.getValue());
                preparedStatement.setString(10, studentNumber);


                isInsertSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;

            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo registrar la actividad. Verifique que los datos ingresados sean válidos y que el practicante exista en el sistema.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al registrar la actividad.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al registrar la actividad", e);
        }

        return isInsertSuccessful;

    }

    @Override
    public List<ActivityDTO> findMonthlyActivitiesByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_ACTIVITIES =
                "SELECT id_actividad_practicante, titulo, descripcion, fecha_inicio, fecha_fin, " +
                        "tiempo_estimado, tiempo_efectivo, avance, observaciones " +
                        "FROM actividades_practicante ap WHERE matricula = ? AND ap.tipo = 'MENSUAL' " +
                        "ORDER BY fecha_inicio ASC";
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

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar las actividades.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener las actividades del practicante.");
            } else {
                throw new DAOException("Ocurrió un error al consultar la lista de actividades.");
            }
        }

        return activityList;
    }

    @Override
    public List<ActivityDTO> findFinalActivitiesByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_ACTIVITIES =
                "SELECT id_actividad_practicante, titulo, descripcion, fecha_inicio, fecha_fin, " +
                        "tiempo_estimado, tiempo_efectivo, avance, observaciones " +
                        "FROM actividades_practicante ap WHERE matricula = ? AND ap.tipo = 'FINAL' " +
                        "ORDER BY fecha_inicio ASC";
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

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar las actividades.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener las actividades del practicante.");
            } else {
                throw new DAOException("Ocurrió un error al consultar la lista de actividades.");
            }
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

                isActivityUpdated = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo actualizar la actividad. Verifique que los datos ingresados sean correctos.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al procesar la actualización de la actividad.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al actualizar la actividad.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error al intentar actualizar la actividad.");
            }
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
                isActivityDeleted = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al eliminar la actividad.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al eliminar la actividad.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error al intentar eliminar la actividad.");
            }
        }

        return isActivityDeleted;

    }

}