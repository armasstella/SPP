package spp.businesslogic.dao;


import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IPrioritizedProjectDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.dataaccess.connection.MySQLConnectionManager;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class PrioritizedProjectDAO implements IPrioritizedProjectDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    @Override
    public boolean savePrioritizedProjects(String email, List<ProjectDTO> prioritizedProjectsList) throws DAOException {
        final String SAVE_PRIORITIZED_PROJECT =
                "INSERT INTO proyectos_priorizados (id_usuario_practicante, matricula, id_proyecto, nivel_prioridad) " +
                        "SELECT p.id_usuario, p.matricula, ?, ? " +
                        "FROM practicantes p " +
                        "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                        "WHERE u.correo_electronico = ? AND u.estado = 'Activo'";

        boolean areProjectsSaved = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            MySQLConnectionManager.getInstance().disableAutoCommitConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PRIORITIZED_PROJECT)) {
                for (int index = 0; index < prioritizedProjectsList.size(); index++) {
                    int priorityLevel = index + 1;
                    preparedStatement.setInt(1, prioritizedProjectsList.get(index).getId());
                    preparedStatement.setInt(2, priorityLevel);
                    preparedStatement.setString(3, email);
                    preparedStatement.addBatch();
                }

                int[] affectedRowsPerStatement = preparedStatement.executeBatch();

                boolean isBatchSuccessful = true;
                for (int rows : affectedRowsPerStatement) {
                    if (rows == Statement.EXECUTE_FAILED || rows == NO_ROWS_AFFECTED) {
                        isBatchSuccessful = false;
                        break;
                    }
                }

                if (isBatchSuccessful) {
                    connection.commit();
                    areProjectsSaved = true;
                } else {
                    MySQLConnectionManager.getInstance().rollbackSafe();
                }
            }

        } catch (SQLException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de base de datos al guardar los proyectos priorizados", e);

        } finally {
            MySQLConnectionManager.getInstance().enableAutoCommitConnection();
        }

        return areProjectsSaved;
    }

    @Override
    public boolean findPrioritizedProjectsByInternEmail(String email) throws DAOException {
        final String CHECK_PRIORITIZED_PROJECTS = "SELECT f_tiene_proyectos_priorizados(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean has_prioritized_projects = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_PRIORITIZED_PROJECTS)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        has_prioritized_projects = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar proyectos priorizados");
        }

        return has_prioritized_projects;

    }

    @Override
    public List<ProjectDTO> findPrioritizedProjectsIdentifiersByStudentNumber(String studentNumber) throws DAOException {
        final String PRIORITIZED_PROJECTS = "SELECT pr.id_proyecto, pr.nombre " +
                "FROM proyectos_priorizados pp " +
                "INNER JOIN proyectos pr ON pp.id_proyecto = pr.id_proyecto " +
                "WHERE pp.matricula = ? " +
                "ORDER BY pp.nivel_prioridad ASC";
        List<ProjectDTO> selectedProjectList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(PRIORITIZED_PROJECTS)) {
                preparedStatement.setString(1, studentNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ProjectDTO projectDTO = new ProjectDTO();
                        projectDTO.setId(resultSet.getInt("id_proyecto"));
                        projectDTO.setName(resultSet.getString("nombre"));
                        selectedProjectList.add(projectDTO);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error al obtener proyectos del practicante");
        }
        return selectedProjectList;
    }

}