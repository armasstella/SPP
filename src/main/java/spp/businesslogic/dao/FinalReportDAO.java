package spp.businesslogic.dao;


import spp.businesslogic.dto.FinalReportDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IFinalReportDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class FinalReportDAO implements IFinalReportDAO {

    @Override
    public FinalReportDTO obtainReportData(String studentNumber) throws DAOException {
        FinalReportDTO report = new FinalReportDTO();
        final String SELECT_REPORT_DATA =
                "SELECT ee.nrc, CONCAT(up.nombre,' ',up.apellidos) AS profesor, ee.periodo, " +
                        "CONCAT(ua.nombre,' ',ua.apellidos) AS alumno, ov.nombre AS organizacion, " +
                        "pr.nombre AS proyecto, i.horas_cubiertas " +
                        "FROM inscripciones_practicas_profesionales i " +
                        "INNER JOIN practicantes p " +
                        "    ON i.id_usuario_practicante = p.id_usuario AND i.matricula = p.matricula " +
                        "INNER JOIN usuarios ua ON p.id_usuario = ua.id_usuario " +
                        "LEFT JOIN experiencias_educativas ee " +
                        "    ON i.id_experiencia_educativa = ee.id_experiencia_educativa " +
                        "LEFT JOIN usuarios up ON ee.id_usuario_profesor = up.id_usuario " +
                        "LEFT JOIN proyectos pr ON i.id_proyecto = pr.id_proyecto " +
                        "LEFT JOIN organizaciones_vinculadas ov " +
                        "    ON pr.id_organizacion_vinculada = ov.id_organizacion_vinculada " +
                        "WHERE i.matricula = ? LIMIT 1";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REPORT_DATA);
            preparedStatement.setString(1, studentNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    report.setNrc(resultSet.getString("nrc"));
                    report.setProfessorName(resultSet.getString("profesor"));
                    report.setSchoolPeriod(resultSet.getString("periodo"));
                    report.setStudentName(resultSet.getString("alumno"));
                    report.setLinkedOrganization(resultSet.getString("organizacion"));
                    report.setProjectName(resultSet.getString("proyecto"));
                    report.setTotalHours(String.valueOf(resultSet.getInt("horas_cubiertas")));
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener los datos del reporte");
        }

        return report;
    }
}
