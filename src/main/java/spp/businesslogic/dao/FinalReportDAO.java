package spp.businesslogic.dao;


import spp.businesslogic.dto.FinalReportDTO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.ReportDocumentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IFinalReportDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class FinalReportDAO implements IFinalReportDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    @Override
    public FinalReportDTO getFinalReportDetailByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_REPORT_DATA = "SELECT * FROM view_detalle_reporte_final WHERE matricula = ? LIMIT 1";
        FinalReportDTO finalReportDTO = new FinalReportDTO();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REPORT_DATA)) {
                preparedStatement.setString(1, studentNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        finalReportDTO.setNrc(resultSet.getString("nrc"));
                        finalReportDTO.setProfessorName(resultSet.getString("profesor"));
                        finalReportDTO.setSchoolPeriod(resultSet.getString("periodo"));
                        finalReportDTO.setStudentName(resultSet.getString("alumno"));
                        finalReportDTO.setLinkedOrganization(resultSet.getString("organizacion"));
                        finalReportDTO.setProjectName(resultSet.getString("proyecto"));
                        finalReportDTO.setTotalHours(String.valueOf(resultSet.getInt("horas_cubiertas")));
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener los datos del reporte", e);
        }

        return finalReportDTO;
    }

    @Override
    public List<InternDTO> getAssignedInternsByProfessorEmail(String email) throws DAOException {
        final String SELECT_INTERNS_BY_PROFESSOR_EMAIL = "SELECT DISTINCT p.matricula, " +
                "CONCAT(ua.nombre, ' ', ua.apellidos) AS nombre_completo " +
                "FROM practicantes p " +
                "INNER JOIN usuarios ua ON p.id_usuario = ua.id_usuario " +
                "INNER JOIN inscripciones_practicas_profesionales i ON p.matricula = i.matricula " +
                "INNER JOIN experiencias_educativas ee ON i.id_experiencia_educativa = ee.id_experiencia_educativa " +
                "INNER JOIN usuarios u_profesor ON ee.id_usuario_profesor = u_profesor.id_usuario " +
                "WHERE u_profesor.correo_electronico = ?";
        List<InternDTO> internsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INTERNS_BY_PROFESSOR_EMAIL)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        InternDTO intern = new InternDTO();
                        intern.setStudentNumber(resultSet.getString("matricula"));
                        intern.setFullName(resultSet.getString("nombre_completo"));
                        internsList.add(intern);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar los practicantes asignados al profesor", e);
        }

        return internsList;

    }

    @Override
    public List<ReportDocumentDTO> getFinalReportsByIntern(String studentNumber) throws DAOException {
        List<ReportDocumentDTO> reportsList = new ArrayList<>();
        String SELECT_REPORT_DOCUMENT = "SELECT d.id_documentos_iniciales, d.nombre_almacenado, d.ruta_archivo, " +
                "f_tiene_documento_final_calificacion(d.id_documentos_iniciales) AS 'tiene_calificacion', " +
                "er.calificacion " +
                "FROM documentos_practicantes d " +
                "LEFT JOIN evaluaciones_reportes er ON d.id_documentos_iniciales = er.id_documento " +
                "WHERE d.matricula = ? AND d.tipo = 'FINAL_REPORT'";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REPORT_DOCUMENT)) {
                preparedStatement.setString(1, studentNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ReportDocumentDTO document = new ReportDocumentDTO();
                        document.setDocumentId(resultSet.getInt("id_documentos_iniciales"));
                        document.setStoredName(resultSet.getString("nombre_almacenado"));
                        document.setFilePath(resultSet.getString("ruta_archivo"));
                        boolean hasGrade = resultSet.getBoolean("tiene_calificacion");
                        document.setGraded(hasGrade);
                        if (hasGrade) {
                            document.setGrade(resultSet.getInt("calificacion"));
                        }
                        reportsList.add(document);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener los reportes finales del alumno.");
        }

        return reportsList;

    }

    @Override
    public boolean assignGrade(int documentId, String email, int grade) throws DAOException {
        final String INSERT_GRADE =
                "INSERT INTO evaluaciones_reportes (id_documento, id_usuario_profesor, calificacion) " +
                        "SELECT ?, u.id_usuario, ? " +
                        "FROM usuarios u WHERE u.correo_electronico = ?";
        boolean isGradeAssigned = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_GRADE)) {
                preparedStatement.setInt(1, documentId);
                preparedStatement.setInt(2, grade);
                preparedStatement.setString(3, email);
                isGradeAssigned = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al asignar la calificación del reporte", e);
        }

        return isGradeAssigned;
    }

    @Override
    public boolean updateGrade(int documentId, int grade) throws DAOException {
        final String UPDATE_GRADE = "UPDATE evaluaciones_reportes SET calificacion = ? WHERE id_documento = ?";
        boolean isGradeUpdated = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_GRADE)) {
                preparedStatement.setInt(1, grade);
                preparedStatement.setInt(2, documentId);
                isGradeUpdated = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al actualizar la calificación del reporte", e);
        }

        return isGradeUpdated;
    }

    @Override
    public boolean hasFinalReportByInternEmail(String email) throws DAOException {
        final String CHECK_FINAL_REPORT =
                "SELECT f_existe_reporte_final(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasReport = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_FINAL_REPORT)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasReport = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al verificar la existencia del reporte final.", e);
        }

        return hasReport;
    }


}
