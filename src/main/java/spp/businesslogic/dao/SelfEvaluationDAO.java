package spp.businesslogic.dao;

import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ISelfEvaluationDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;

public class SelfEvaluationDAO implements ISelfEvaluationDAO {

    public SelfEvaluationDAO() {
    }

    @Override
    public boolean saveSelfEvaluation(String email) throws DAOException {
        final String INSERT_SELFEVALUATION = "INSERT INTO autoevaluaciones (id_usuario_practicante, matricula) " +
                "SELECT p.id_usuario, p.matricula FROM usuarios u INNER JOIN practicantes p " +
                "ON u.id_usuario = p.id_usuario WHERE u.correo_electronico = ?";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SELFEVALUATION)) {
                preparedStatement.setString(1, email);
                isInsertSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }
        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.TRIGGER_EXCEPTION_CODE)) {
                throw new DAOException("Error de conexión al buscar periodo activo para guardar autoevaluación.");
            } else {
                throw new DAOException("Ocurrió un error al guardar la autoevaluación");
            }
        }

        return isInsertSuccessful;
    }

    @Override
    public SelfEvaluationDTO findEvaluationHeaderByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_HEADER_DATA =
                "SELECT CONCAT(ua.nombre, ' ', ua.apellidos) AS nombre_completo, " +
                        "p.matricula, ov.nombre AS organizacion_vinculada, ep.rol, " +
                        "CONCAT(ep.nombres, ' ', ep.apellidos) AS encargado_proyecto, " +
                        "COALESCE(pr.nombre, 'Sin proyecto asignado') AS proyecto " +
                        "FROM practicantes p " +
                        "INNER JOIN usuarios ua ON p.id_usuario = ua.id_usuario " +
                        "INNER JOIN inscripciones_practicas_profesionales i ON p.id_usuario = i.id_usuario_practicante AND p.matricula = i.matricula " +
                        "INNER JOIN proyectos pr ON i.id_proyecto = pr.id_proyecto " +
                        "INNER JOIN organizaciones_vinculadas ov ON pr.id_organizacion_vinculada = ov.id_organizacion_vinculada " +
                        "INNER JOIN encargados_proyectos ep ON pr.id_encargado_proyecto = ep.id_encargado_proyecto " +
                        "WHERE p.matricula = ? LIMIT 1";
        SelfEvaluationDTO evaluationHeader = null;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_HEADER_DATA)) {
                preparedStatement.setString(1, studentNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        evaluationHeader = new SelfEvaluationDTO();
                        evaluationHeader.setStudentName(resultSet.getString("nombre_completo"));
                        evaluationHeader.setStudentNumber(resultSet.getString("matricula"));
                        evaluationHeader.setLinkedOrganization(resultSet.getString("organizacion_vinculada"));
                        evaluationHeader.setDepartment(resultSet.getString("rol"));
                        evaluationHeader.setProjectManager(resultSet.getString("encargado_proyecto"));
                        evaluationHeader.setProjectName(resultSet.getString("proyecto"));
                    }
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar los datos del alumno.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar datos del alumno.");
            } else {
                throw new DAOException("Ocurrió un error al consultar los datos de la autoevaluación.");
            }
        }

        return evaluationHeader;
    }

    @Override
    public boolean hasSelfEvaluation(String email) throws DAOException {
        final String CHECK_SELFEVALUATION = "SELECT f_realizo_autoevaluacion(?)";
        boolean hasSelfevaluation = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_SELFEVALUATION)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasSelfevaluation = resultSet.getBoolean(1);;
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al verificar autoevaluación de el practicante", e);
        }

        return hasSelfevaluation;
    }

}