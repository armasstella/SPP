package spp.businesslogic.dao;

import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IFinalReportDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class FinalReportDAO implements IFinalReportDAO {

    public FinalReportDAO() {
    }

    @Override
    public List<ReportDocumentFileDTO> getFinalReportsByIntern(String studentNumber) throws DAOException {
        List<ReportDocumentFileDTO> reportsList = new ArrayList<>();
        final String SELECT_REPORT_DOCUMENT = "SELECT d.id_documentos_iniciales, d.nombre_almacenado, d.ruta_archivo, " +
                "f_tiene_documento_calificacion(d.id_documentos_iniciales) AS 'tiene_calificacion', " +
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
                        ReportDocumentFileDTO document = new ReportDocumentFileDTO();
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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al obtener los reportes finales.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar los reportes finales.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener los reportes finales del alumno.");
            } else {
                throw new DAOException("Ocurrió un error interno al intentar obtener los reportes finales.");
            }
        }

        return reportsList;
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

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar la existencia del reporte final.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al verificar el reporte final.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al verificar la existencia del reporte final.");
            } else {
                throw new DAOException("Ocurrió un error interno al consultar el reporte final.");
            }
        }

        return hasReport;
    }
}