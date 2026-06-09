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
    public boolean saveDocument(String email, InitialDocumentDTO initialDocumentDTO) throws DAOException {
        final String INSERT_DOCUMENT = " INSERT INTO documentos_iniciales (nombre_original, " +
                "nombre_almacenado, ruta_archivo, tamaño_mb, extension, fecha_subida," +
                "tipo, id_usuario_practicante," +
                "matricula) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean isSaveSuccessful = false;
        InternDAO internDAO = new InternDAO();
        UserDAO userDAO = new UserDAO();

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
            preparedStatement.setInt(8, userDAO.obtainId(email));
            preparedStatement.setString(9, internDAO.obtainStudentNumber(email));

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
    public boolean searchPSPForIntern(String email) throws DAOException {
        final String SEARCH_PSP = "SELECT f_existe_psp(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PSP);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar psp.", e);
        }

        return isSearchSuccessful;

    }

    @Override
    public boolean searchPartialReportForIntern(String email) throws DAOException {
        final String SEARCH_PARTIAL_REPORT = "SELECT f_existe_reporte_parcial(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_PARTIAL_REPORT);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar reporte parcial.", e);
        }

        return isSearchSuccessful;

    }

    public boolean searchMonthlyReportForIntern(String email) throws DAOException {
        final String SEARCH_MONTHLY_REPORT = "SELECT f_existe_reporte_mensual(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_MONTHLY_REPORT);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar reporte mensual.", e);
        }

        return isSearchSuccessful;

    }

    @Override
    public boolean searchSelfEvaluationForIntern(String email) throws DAOException {
        final String SEARCH_SELF_EVALUATION = "SELECT f_existe_autoevaluacion(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_SELF_EVALUATION);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar autoevaluación.", e);
        }

        return isSearchSuccessful;

    }

    @Override
    public boolean searchEvaluationLinkedOrganizationForIntern(String email) throws DAOException {
        final String SEARCH_LINKED_ORGANIZATION_EVALUATION = "SELECT f_existe_evaluacion_ov(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_LINKED_ORGANIZATION_EVALUATION);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar evaluación de organización vinculada.", e);
        }

        return isSearchSuccessful;

    }

    @Override
    public boolean searchFinalReportForIntern(String email) throws DAOException {
        final String SEARCH_FINAL_REPORT = "SELECT f_existe_reporte_final(?)";
        boolean isSearchSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_FINAL_REPORT);
            UserDAO userDAO = new UserDAO();
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar reporte final.", e);
        }

        return isSearchSuccessful;

    }

}
