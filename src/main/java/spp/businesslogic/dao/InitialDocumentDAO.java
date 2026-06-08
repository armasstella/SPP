package spp.businesslogic.dao;


import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInitialDocumentDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.ResultSet;


public class InitialDocumentDAO implements IInitialDocumentDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    @Override
    public boolean saveDocument(String studentNumber, InitialDocumentDTO initialDocumentDTO) throws DAOException {
        final String INSERT_DOCUMENT = " INSERT INTO documentos_iniciales (nombre_original, " +
                "nombre_almacenado, ruta_archivo, tamaño_mb, extension, fecha_subida," +
                "tipo, id_usuario_practicante," +
                "matricula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean isSaveSuccessful = false;
        InternDAO internDAO = new InternDAO();

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
                throw new DAOException("WARN: Fallo al guardar documento. No se afectaron filas.");
            }

            connection.commit();
            isSaveSuccessful = true;
            connection.setAutoCommit(true);

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al guardar documento", e);
        }

        return isSaveSuccessful;

    }

    @Override
    public boolean searchClassScheduleForIntern(String email) throws DAOException {
        final String SEARCH_SCHEDULE = "SELECT f_existe_horario_estudiante(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_SCHEDULE);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar horario.", e);
        }

        return isSearchSuccessful;

    }

    @Override
    public boolean searchActivitiesScheduleForIntern(String email) throws DAOException {
        final String SEARCH_SCHEDULE = "SELECT f_existe_calendarizacion_actividades_estudiante(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_SCHEDULE);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar calendarización.", e);
        }

        return isSearchSuccessful;

    }

    @Override
    public boolean searchPSPForIntern(String studentNumber) throws DAOException {
        return false;
    }

    @Override
    public boolean searchPartialReportForIntern(String studentNumber) throws DAOException {
        return false;
    }

    @Override
    public boolean searchSelfEvaluationForIntern(String studentNumber) throws DAOException {
        return false;
    }

    @Override
    public boolean searchEvaluationLinkedOrganizationForIntern(String studentNumber) throws DAOException {
        return false;
    }

    @Override
    public boolean searchFinalReportForIntern(String studentNumber) throws DAOException {
        return false;
    }

}
