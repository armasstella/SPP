package spp.dataaccess.dao;

import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInitialDocumentDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;

import java.sql.*;

public class InitialDocumentDAO implements IInitialDocumentDAO {
    private static final int NO_ROWS_AFFECTED = 0;

    InternDAO internDAO = new InternDAO();

    @Override
    public boolean saveDocument(String studentNumber, InitialDocumentDTO initialDocumentDTO) throws DAOException {
        boolean isSaveSuccessful = false;
        final String INSERT_DOCUMENT = " INSERT INTO documentos_iniciales (nombre_original, " +
                "nombre_almacenado, ruta_archivo, tamaño_mb, extension, fecha_subida," +
                "tipo, id_usuario_practicante," +
                "matricula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT);

            preparedStatement.setString(1, initialDocumentDTO.getOriginalName());
            preparedStatement.setString(2, initialDocumentDTO.getSavedName());
            preparedStatement.setString(3, initialDocumentDTO.getFilePath());
            preparedStatement.setDouble(4, initialDocumentDTO.getSizeMb());
            preparedStatement.setString(5, initialDocumentDTO.getExtension());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(initialDocumentDTO.getUploadDate()));
            preparedStatement.setString(7, initialDocumentDTO.getDocumentType());
            preparedStatement.setInt(8, internDAO.obtainId(studentNumber));
            preparedStatement.setString(9, studentNumber);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == NO_ROWS_AFFECTED) {
                throw new DAOException("Fallo al guardar documento. No se afectaron filas.");
            }

            connection.commit();
            isSaveSuccessful = true;

        } catch (SQLException | DAOException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al guardar documento", e);
        }
        return isSaveSuccessful;
    }

    @Override
    public boolean searchClassScheduleForIntern(String studentNumber) throws DAOException {
        boolean isSearchSuccessful = false;
        final String SEARCH_SCHEDULE = "SELECT f_existe_horario_estudiante(?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_SCHEDULE);
            preparedStatement.setString(1, studentNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al buscar horario del practicante.", e);
        }

        return isSearchSuccessful;
    }

    @Override
    public boolean searchActivitiesScheduleForIntern(String studentNumber) throws DAOException {
        boolean isSearchSuccessful = false;
        final String SEARCH_SCHEDULE = "SELECT f_existe_calendarizacion_actividades_estudiante(?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_SCHEDULE);
            preparedStatement.setString(1, studentNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al buscar calendarización de actividades.", e);
        }

        return isSearchSuccessful;
    }



}
