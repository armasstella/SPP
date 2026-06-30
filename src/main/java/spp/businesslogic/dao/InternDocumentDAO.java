package spp.businesslogic.dao;

import spp.businesslogic.dto.InternDocumentDTO;
import spp.businesslogic.dto.InternDocumentReviewDTO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInitialDocumentDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLTimeoutException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class InternDocumentDAO implements IInitialDocumentDAO {

    public InternDocumentDAO() {
    }

    @Override
    public boolean saveDocument(String studentNumber, InternDocumentDTO internDocumentDTO) throws DAOException {
        final String INSERT_DOCUMENT = " INSERT INTO documentos_practicantes (nombre_original, " +
                "nombre_almacenado, ruta_archivo, tamaño_mb, extension, fecha_subida," +
                "tipo, id_usuario_practicante, matricula) SELECT ?, ?, ?, ?, ?, ?, ?, p.id_usuario, p.matricula " +
                "FROM practicantes p WHERE p.matricula = ?";
        boolean isSaveSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_DOCUMENT)) {
                preparedStatement.setString(1, internDocumentDTO.getOriginalName());
                preparedStatement.setString(2, internDocumentDTO.getSavedName());
                preparedStatement.setString(3, internDocumentDTO.getFilePath());
                preparedStatement.setDouble(4, internDocumentDTO.getSizeMb());
                preparedStatement.setString(5, internDocumentDTO.getExtension());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(internDocumentDTO.getUploadDate()));
                preparedStatement.setString(7, String.valueOf(internDocumentDTO.getDocumentType()));
                preparedStatement.setString(8, studentNumber);

                isSaveSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo guardar el documento. Verifique que los datos ingresados sean válidos y que el practicante exista en el sistema.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación al intentar guardar el documento.");

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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar el horario del estudiante.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar el horario.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar horario.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar el horario del estudiante.");
            }
        }

        return hasClassSchedule;
    }

    @Override
    public boolean hasActivitiesPlanByInternEmail(String email) throws DAOException {
        final String CHECK_SCHEDULE =
                "SELECT f_existe_plan_actividades_estudiante(u.id_usuario) FROM usuarios u WHERE " +
                        "u.correo_electronico = ?";
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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar el plan de actividades.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar la calendarización.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar calendarización.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar el plan de actividades.");
            }
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
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasPSP = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar el PSP.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar el PSP.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar psp.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar la existencia del PSP.");
            }
        }

        return hasPSP;
    }

    @Override
    public boolean hasPartialReportByInternEmail(String email) throws DAOException {
        final String CHECK_PARTIAL_REPORT = "SELECT f_existe_reporte_parcial(u.id_usuario) FROM usuarios u WHERE " +
                "u.correo_electronico = ?";
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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar el reporte parcial.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar el reporte parcial.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar reporte parcial.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar el reporte parcial.");
            }
        }

        return hasPartialReport;
    }

    public boolean hasMonthlyReportByInternEmail(String email) throws DAOException {
        final String CHECK_MONTHLY_REPORT = "SELECT f_tiene_subido_limite_reportes_mensuales(u.id_usuario) " +
                "FROM usuarios u WHERE u.correo_electronico = ?";
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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar el reporte mensual.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar el reporte mensual.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar reporte mensual.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar el reporte mensual.");
            }
        }

        return hasMonthlyReport;
    }

    @Override
    public boolean hasSelfEvaluationByInternEmail(String email) throws DAOException {
        final String CHECK_SELF_EVALUATION = "SELECT f_existe_autoevaluacion(u.id_usuario) FROM usuarios u WHERE " +
                "u.correo_electronico = ?";
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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar la autoevaluación.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar la autoevaluación.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar autoevaluación.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar la autoevaluación.");
            }
        }

        return hasSelfEvaluation;
    }

    @Override
    public boolean hasEvaluationLinkedOrganizationByInternEmail(String email) throws DAOException {
        final String CHECK_EVALUATION = "SELECT f_existe_evaluacion_ov(u.id_usuario) FROM usuarios u WHERE " +
                "u.correo_electronico = ?";
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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar la evaluación de la organización vinculada.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar la evaluación de la organización vinculada.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar evaluación de organización vinculada.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar la evaluación de la organización vinculada.");
            }
        }

        return hasLinkedOrganizationEvaluation;
    }

    public List<InternDocumentReviewDTO> findDocumentsWithEvaluationStatusByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_DOCUMENTS_WITH_STATUS = "SELECT dp.id_documentos_iniciales, dp.nombre_original, dp.tipo, dp.ruta_archivo, rd.calificacion " +
                        "FROM documentos_practicantes dp " +
                        "LEFT JOIN revision_documentos rd ON dp.id_documentos_iniciales = rd.id_evaluacion " +
                        "WHERE dp.matricula = ?";
        List<InternDocumentReviewDTO> documentsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_DOCUMENTS_WITH_STATUS)) {
                preparedStatement.setString(1, studentNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        InternDocumentReviewDTO documentDTO = new InternDocumentReviewDTO();

                        int idDocument = resultSet.getInt("id_documentos_iniciales");
                        String originalName = resultSet.getString("nombre_original");
                        DocumentType documentType = DocumentType.valueOf(resultSet.getString("tipo"));
                        String filePath = resultSet.getString("ruta_archivo");
                        int grade = resultSet.getInt("calificacion");
                        boolean isEvaluated = grade > 0;

                        documentDTO.setInternDocumentId(idDocument);
                        documentDTO.setOriginalName(originalName);
                        documentDTO.setDocumentType(documentType);
                        documentDTO.setFilePath(filePath);
                        documentDTO.setGraded(isEvaluated);
                        documentsList.add(documentDTO);
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al obtener los documentos del practicante para su revisión.", e);
        }

        return documentsList;
    }

    @Override
    public boolean assignGrade(int documentId, int grade, String comments) throws DAOException {
        final String UPDATE_GRADE = "UPDATE revision_documentos SET calificacion = ?, " +
                "estado = 'CALIFICADO', comentarios = ? WHERE id_documento = ?";
        boolean isGradeUpdated = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GRADE)) {
                preparedStatement.setInt(1, grade);
                preparedStatement.setString(2, comments);
                preparedStatement.setInt(3, documentId);
                isGradeUpdated = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo actualizar la calificación. Verifique los datos ingresados.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al actualizar la calificación del reporte.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al procesar la actualización de la calificación.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al actualizar la calificación del reporte.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error interno al intentar actualizar la calificación del reporte.");
            }
        }

        return isGradeUpdated;
    }

    public boolean hasReleaseLetterByInternEmail(String email) throws DAOException {
        final String CHECK_RELEASE_LETTER = "SELECT f_existe_carta_liberacion(u.id_usuario) FROM usuarios u WHERE " +
                "u.correo_electronico = ?";
        boolean hasReleaseLetter = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_RELEASE_LETTER)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasReleaseLetter = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar la carta de liberación.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar la carta de liberacion.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar la carta de liberación.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar la carta de liberación.");
            }
        }

        return hasReleaseLetter;
    }

}