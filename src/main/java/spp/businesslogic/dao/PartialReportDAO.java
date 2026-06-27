package spp.businesslogic.dao;

import spp.businesslogic.dto.ReportDocumentFileDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IPartialReportDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartialReportDAO implements IPartialReportDAO {

    @Override
    public List<ReportDocumentFileDTO> getPartialReportsByIntern(String studentNumber) throws DAOException {
        List<ReportDocumentFileDTO> reportsList = new ArrayList<>();
        String SELECT_REPORT_DOCUMENT = "SELECT d.id_documentos_iniciales, d.nombre_almacenado, d.ruta_archivo, " +
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

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al obtener los reportes finales del alumno.", e);
        }

        return reportsList;

    }

}
