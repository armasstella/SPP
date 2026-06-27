package spp.businesslogic.dao;

import spp.businesslogic.dto.ActivityScheduleDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IActivityScheduleDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ActivityScheduleDAO implements IActivityScheduleDAO {

    private static final int NO_ROWS_AFFECTED = 0;


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
                isSaveSuccessful = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;

            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al guardar documento", e);
        }

        return isSaveSuccessful;
    }
}
