package spp.businesslogic.dao;

import spp.businesslogic.dto.ReportDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IReportDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportDAO implements IReportDAO {

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

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al obtener los datos del reporte", e);
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
                isGradeAssigned = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al asignar la calificación del reporte", e);
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
                isGradeUpdated = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al actualizar la calificación del reporte", e);
        }

        return isGradeUpdated;
    }

}
