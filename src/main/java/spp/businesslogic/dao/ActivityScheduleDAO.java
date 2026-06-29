package spp.businesslogic.dao;

import spp.businesslogic.dto.ActivityScheduleDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IActivityScheduleDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;

public class ActivityScheduleDAO implements IActivityScheduleDAO {

    public ActivityScheduleDAO() {
    }

    @Override
    public boolean saveActivitySchedule(ActivityScheduleDTO activityScheduleDTO, int projectId) throws DAOException {
        final String INSERT_DOCUMENT = "INSERT INTO calendarizaciones_actividades (nombre_original, " +
                "nombre_almacenado, ruta_archivo, tamaño_mb, extension, fecha_subida," +
                "id_proyecto) VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean isSaveSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT)) {
                preparedStatement.setString(1, activityScheduleDTO.getOriginalName());
                preparedStatement.setString(2, activityScheduleDTO.getSavedName());
                preparedStatement.setString(3, activityScheduleDTO.getFilePath());
                preparedStatement.setDouble(4, activityScheduleDTO.getSizeMb());
                preparedStatement.setString(5, activityScheduleDTO.getExtension());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(activityScheduleDTO.getUploadDate()));
                preparedStatement.setInt(7, projectId);

                isSaveSuccessful = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo guardar la calendarización. Es posible que el archivo ya exista o el proyecto asociado no sea válido.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al intentar guardar la calendarización de actividades.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al guardar el documento.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al guardar el documento.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error interno al intentar guardar el documento.");
            }
        }

        return isSaveSuccessful;
    }
}