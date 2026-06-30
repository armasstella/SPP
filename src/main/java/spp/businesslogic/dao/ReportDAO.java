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
                isGradeAssigned = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo asignar la calificación. Es posible que el reporte ya haya sido evaluado o el profesor no sea válido.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al asignar la calificación del reporte.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al intentar guardar la calificación.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al asignar la calificación del reporte.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error interno al asignar la calificación del reporte.");
            }
        }

        return isGradeAssigned;
    }


}