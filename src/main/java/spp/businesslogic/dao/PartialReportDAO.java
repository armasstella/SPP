package spp.businesslogic.dao;

import spp.businesslogic.dto.PartialReportDTO;
import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IPartialReportDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class PartialReportDAO implements IPartialReportDAO {

    public PartialReportDAO() {
    }

    @Override
    public PartialReportDTO findReportHeaderByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_REPORT_HEADER = "SELECT nrc, profesor, periodo, alumno, organizacion, proyecto, horas_cubiertas " +
                "FROM view_detalle_reporte " +
                "WHERE matricula = ? LIMIT 1";
        PartialReportDTO partialReport = null;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REPORT_HEADER)) {

                preparedStatement.setString(1, studentNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        partialReport = buildReportHeaderFromResultSet(resultSet, studentNumber);
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al intentar recuperar el encabezado del reporte.", e);
            } else {
                throw new DAOException("Ocurrió un error al buscar la información del reporte.", e);
            }
        }

        return partialReport;
    }


    private PartialReportDTO buildReportHeaderFromResultSet(ResultSet resultSet, String studentNumber)
            throws SQLException {
        PartialReportDTO partialReport = new PartialReportDTO();
        partialReport.setStudentNumber(studentNumber);
        partialReport.setNrc(resultSet.getString("nrc"));
        partialReport.setProfessorName(resultSet.getString("profesor"));
        partialReport.setSchoolPeriod(resultSet.getString("periodo"));
        partialReport.setStudentName(resultSet.getString("alumno"));
        partialReport.setLinkedOrganization(resultSet.getString("organizacion"));
        partialReport.setProjectName(resultSet.getString("proyecto"));
        partialReport.setCoveredHours(String.valueOf(resultSet.getInt("horas_cubiertas")));
        return partialReport;
    }

    @Override
    public List<ReportDocumentFileDTO> getPartialReportsByIntern(String studentNumber) throws DAOException {
        List<ReportDocumentFileDTO> reportsList = new ArrayList<>();
        final String SELECT_REPORT_DOCUMENT = "SELECT d.id_documentos_iniciales, d.nombre_almacenado, d.ruta_archivo, " +
                "f_tiene_documento_calificacion(d.id_documentos_iniciales) AS 'tiene_calificacion', " +
                "er.calificacion " +
                "FROM documentos_practicantes d " +
                "LEFT JOIN evaluaciones_reportes er ON d.id_documentos_iniciales = er.id_documento " +
                "WHERE d.matricula = ? AND d.tipo = 'PARTIAL_REPORT'";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REPORT_DOCUMENT)) {
                preparedStatement.setString(1, studentNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ReportDocumentFileDTO reportDocumentFileDTO = new ReportDocumentFileDTO();
                        reportDocumentFileDTO.setDocumentId(resultSet.getInt("id_documentos_iniciales"));
                        reportDocumentFileDTO.setStoredName(resultSet.getString("nombre_almacenado"));
                        reportDocumentFileDTO.setFilePath(resultSet.getString("ruta_archivo"));

                        boolean hasGrade = resultSet.getBoolean("tiene_calificacion");
                        reportDocumentFileDTO.setGraded(hasGrade);
                        if (hasGrade) {
                            reportDocumentFileDTO.setGrade(resultSet.getInt("calificacion"));
                        }
                        reportsList.add(reportDocumentFileDTO);
                    }
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar los reportes parciales.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener los reportes parciales del alumno.");
            } else {
                throw new DAOException("Ocurrió un error al intentar obtener los reportes parciales.");
            }
        }

        return reportsList;
    }
}