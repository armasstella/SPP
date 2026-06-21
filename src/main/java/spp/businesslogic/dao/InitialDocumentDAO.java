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
        final String INSERT_DOCUMENT = " INSERT INTO documentos_practicantes (nombre_original, " +
                "nombre_almacenado, ruta_archivo, tamaño_mb, extension, fecha_subida," +
                "tipo, id_usuario_practicante," +
                "matricula) SELECT ?, ?, ?, ?, ?, ?, ?, p.id_usuario, p.matricula " +
                "FROM practicantes p WHERE p.matricula = ?";
        boolean isSaveSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT)) {
                preparedStatement.setString(1, initialDocumentDTO.getOriginalName());
                preparedStatement.setString(2, initialDocumentDTO.getSavedName());
                preparedStatement.setString(3, initialDocumentDTO.getFilePath());
                preparedStatement.setDouble(4, initialDocumentDTO.getSizeMb());
                preparedStatement.setString(5, initialDocumentDTO.getExtension());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(initialDocumentDTO.getUploadDate()));
                preparedStatement.setString(7, initialDocumentDTO.getDocumentType());
                isSaveSuccessful = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al guardar documento", e);
        }

        return isSaveSuccessful;

    }

    @Override
    public boolean hasClassScheduleByInternEmail(String email) throws DAOException {
        final String CHECK_CLASS_SCHEDULE =
                "SELECT f_existe_horario_estudiante(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasClassSchedule = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_CLASS_SCHEDULE)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasClassSchedule = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar horario.", e);
        }

        return hasClassSchedule;

    }

    @Override
    public boolean hasActivitiesScheduleByInternEmail(String email) throws DAOException {
        final String CHECK_SCHEDULE =
                "SELECT f_existe_calendarizacion_actividades_estudiante(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasActivitiesSchedule = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_SCHEDULE)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasActivitiesSchedule = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar calendarización.", e);
        }

        return hasActivitiesSchedule;

    }

    @Override
    public boolean hasPSPByInternEmail(String email) throws DAOException {
        final String CHECK_PSP = "SELECT f_existe_psp(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasPSP = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_PSP)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasPSP = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar psp.", e);
        }

        return hasPSP;

    }

    @Override
    public boolean hasPartialReportByInternEmail(String email) throws DAOException {
        final String CHECK_PARTIAL_REPORT = "SELECT f_existe_reporte_parcial(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasPartialReport = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_PARTIAL_REPORT)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasPartialReport = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar reporte parcial.", e);
        }

        return hasPartialReport;

    }

    public boolean hasMonthlyReportByInternEmail(String email) throws DAOException {
        final String CHECK_MONTHLY_REPORT = "SELECT f_existe_reporte_mensual(?)";
        boolean hasMonthlyReport = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_MONTHLY_REPORT)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasMonthlyReport = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar reporte mensual.", e);
        }

        return hasMonthlyReport;

    }

    @Override
    public boolean hasSelfEvaluationByInternEmail(String email) throws DAOException {
        final String CHECK_SELF_EVALUATION = "SELECT f_existe_autoevaluacion(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasSelfEvaluation = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_SELF_EVALUATION)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasSelfEvaluation = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar autoevaluación.", e);
        }

        return hasSelfEvaluation;

    }

    @Override
    public boolean hasEvaluationLinkedOrganizationByInternEmail(String email) throws DAOException {
        final String CHECK_EVALUATION = "SELECT f_existe_evaluacion_ov(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasLinkedOrganizationEvaluation = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_EVALUATION)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasLinkedOrganizationEvaluation = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar evaluación de organización vinculada.", e);
        }

        return hasLinkedOrganizationEvaluation;

    }



}
