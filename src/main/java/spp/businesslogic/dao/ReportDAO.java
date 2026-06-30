package spp.businesslogic.dao;

import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IReportDAO;
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

public class ReportDAO implements IReportDAO {

    public ReportDAO() {
    }

    @Override
    public ReportDTO getReportDetailByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_REPORT_DATA = "SELECT * FROM view_detalle_reporte WHERE matricula = ? LIMIT 1";
        ReportDTO reportDTO = new ReportDTO();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_REPORT_DATA)) {
                preparedStatement.setString(1, studentNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        reportDTO.setNrc(resultSet.getString("nrc"));
                        reportDTO.setProfessorName(resultSet.getString("profesor"));
                        reportDTO.setSchoolPeriod(resultSet.getString("periodo"));
                        reportDTO.setStudentName(resultSet.getString("alumno"));
                        reportDTO.setLinkedOrganization(resultSet.getString("organizacion"));
                        reportDTO.setProjectName(resultSet.getString("proyecto"));
                        reportDTO.setTotalHours(String.valueOf(resultSet.getInt("horas_cubiertas")));
                    }
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar los datos del reporte.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener los datos del reporte.");
            } else {
                throw new DAOException("Ocurrió un error al intentar obtener los detalles del reporte.");
            }
        }

        return reportDTO;
    }

}