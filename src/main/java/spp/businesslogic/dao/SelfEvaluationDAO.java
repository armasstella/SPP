package spp.businesslogic.dao;


import spp.businesslogic.dto.SelfEvaluationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ISelfEvaluationDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class SelfEvaluationDAO implements ISelfEvaluationDAO {

    public SelfEvaluationDTO obtainEvaluationData(String email) throws DAOException {
        SelfEvaluationDTO evaluation = null;
        InternDAO internDAO = new InternDAO();

        final String SELECT_EVALUATION_DATA =
                "SELECT CONCAT(ua.nombre, ' ', ua.apellidos) AS student_name, " +
                        "p.matricula AS student_number, " +
                        "ov.nombre AS linked_organization, " +
                        "ep.rol AS department, " +
                        "CONCAT(ep.nombres, ' ', ep.apellidos) AS project_manager, " +
                        "pr.nombre AS project_name " +
                        "FROM practicantes p " +
                        "INNER JOIN usuarios ua ON p.id_usuario = ua.id_usuario " +
                        "INNER JOIN inscripciones_practicas_profesionales i " +
                        "    ON p.id_usuario = i.id_usuario_practicante AND p.matricula = i.matricula " +
                        "INNER JOIN proyectos pr ON i.id_proyecto = pr.id_proyecto " +
                        "INNER JOIN organizaciones_vinculadas ov " +
                        "    ON pr.id_organizacion_vinculada = ov.id_organizacion_vinculada " +
                        "INNER JOIN encargados_proyectos ep " +
                        "    ON pr.id_encargado_proyecto = ep.id_encargado_proyecto " +
                        "WHERE p.matricula = ? LIMIT 1";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EVALUATION_DATA);
            preparedStatement.setString(1, internDAO.obtainStudentNumber(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    evaluation = new SelfEvaluationDTO();
                    evaluation.setStudentName(resultSet.getString("student_name"));
                    evaluation.setStudentNumber(resultSet.getString("student_number"));
                    evaluation.setLinkedOrganization(resultSet.getString("linked_organization"));
                    evaluation.setDepartment(resultSet.getString("department"));
                    evaluation.setProjectManager(resultSet.getString("project_manager"));

                    String projectName = resultSet.getString("project_name");
                    if (projectName != null) {
                        evaluation.setProjectName(projectName);
                    } else {
                        evaluation.setProjectName("");
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar datos para autoevaluación");
        }

        return evaluation;

    }

}