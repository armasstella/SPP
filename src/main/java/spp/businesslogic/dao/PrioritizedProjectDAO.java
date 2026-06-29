package spp.businesslogic.dao;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IPrioritizedProjectDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.dataaccess.connection.MySQLConnectionManager;
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
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PrioritizedProjectDAO implements IPrioritizedProjectDAO {

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
                    if (rows == Statement.EXECUTE_FAILED || rows == DAOResultConstant.NO_ROWS_AFFECTED) {
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

        } catch (SQLIntegrityConstraintViolationException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("Los proyectos ya han sido priorizados o la información del practicante no es válida.");

        } catch (SQLTransactionRollbackException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de concurrencia al guardar los proyectos. Intente nuevamente.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al guardar los proyectos priorizados.");

        } catch (SQLTimeoutException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al guardar los proyectos.");

        } catch (SQLException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al guardar los proyectos priorizados.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error interno al guardar los proyectos priorizados.");
            }
        } finally {
            MySQLConnectionManager.getInstance().enableAutoCommitConnection();
        }

        return areProjectsSaved;
    }

    @Override
    public boolean findPrioritizedProjectsByInternEmail(String email) throws DAOException {
        final String CHECK_PRIORITIZED_PROJECTS = "SELECT f_tiene_proyectos_priorizados(u.id_usuario) FROM usuarios u WHERE u.correo_electronico = ?";
        boolean hasPrioritizedProjects = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_PRIORITIZED_PROJECTS)) {
                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        hasPrioritizedProjects = resultSet.getBoolean(1);
                    }
                }
            }
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar proyectos priorizados.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar proyectos priorizados.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar proyectos priorizados.");
            } else {
                throw new DAOException("Ocurrió un error interno al verificar los proyectos priorizados.");
            }
        }

        return hasPrioritizedProjects;
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
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con lel servidor al obtener proyectos priorizados.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar proyectos priorizados.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener proyectos del practicante.");
            } else {
                throw new DAOException("Ocurrió un error interno al recuperar la lista de proyectos priorizados.");
            }
        }
        return selectedProjectList;
    }
}