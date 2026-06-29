package spp.businesslogic.dao;

import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectManagerDAO;
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
import java.util.ArrayList;
import java.util.List;

public class ProjectManagerDAO implements IProjectManagerDAO {

    public ProjectManagerDAO() {
    }

    @Override
    public boolean registerProjectManager(ProjectManagerDTO projectManagerDTO, int linkedOrganizationId) throws
            DAOException {
        final String INSERT_PROJECT_MANAGER = "INSERT INTO Encargados_Proyectos " +
                "(nombres, apellidos, responsabilidad, rol, telefono, id_organizacion_vinculada) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT_MANAGER)) {
                preparedStatement.setString(1,
                        projectManagerDTO.getFirstName() + " " + projectManagerDTO.getSecondName());
                preparedStatement.setString(2,
                        projectManagerDTO.getFirstLastName() + " " + projectManagerDTO.getSecondLastName());
                preparedStatement.setString(3,
                        projectManagerDTO.getResponsibility());
                preparedStatement.setString(4,
                        projectManagerDTO.getRole());
                preparedStatement.setString(5,
                        projectManagerDTO.getPhoneNumber());
                preparedStatement.setInt(6,
                        linkedOrganizationId);

                isInsertSuccessful = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("El encargado de proyecto ya existe o la organización vinculada no es válida. " +
                    "Verifique que los datos no estén duplicados.", e);

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al registrar el encargado de proyecto.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al registrar el encargado de proyecto.", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al registrar el encargado de proyecto.", e);
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error interno al intentar registrar el encargado de proyecto.", e);
            }
        }

        return isInsertSuccessful;
    }

    @Override
    public List<ProjectManagerDTO> getActiveProjectManagers() throws DAOException {
        final String SELECT_PROJECT_MANAGER = "SELECT id_encargado_proyecto, CONCAT(nombres, ' ', apellidos) " +
                "AS nombre_completo FROM encargados_proyectos";
        List<ProjectManagerDTO> projectManagersList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECT_MANAGER);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    ProjectManagerDTO projectManager = new ProjectManagerDTO();
                    projectManager.setId(resultSet.getInt("id_encargado_proyecto"));
                    projectManager.setFirstName(resultSet.getString("nombre_completo"));
                    projectManagersList.add(projectManager);
                }
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al obtener la lista de encargados de " +
                    "proyectos.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar la lista de encargados de proyectos.", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar encargados de proyectos.", e);
            } else {
                throw new DAOException("Ocurrió un error interno al obtener los encargados de proyectos.", e);
            }
        }

        return projectManagersList;
    }

    @Override
    public boolean existsProjectManagers() throws DAOException {
        final String SEARCH_REGISTERS = "SELECT f_hay_encargados_proyectos()";
        boolean projectManagersExists = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_REGISTERS)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        projectManagersExists = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al verificar la existencia de encargados de " +
                    "proyectos.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar la disponibilidad de encargados de " +
                    "proyectos.", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al verificar si existen encargados de proyectos.", e);
            } else {
                throw new DAOException("Ocurrió un error interno al consultar los encargados de proyectos.", e);
            }
        }

        return projectManagersExists;
    }

    @Override
    public List<ProjectManagerDTO> getProjectManagersByOrganization(int organizationId) throws DAOException {
        final String SELECT_BY_ORGANIZATION = "SELECT id_encargado_proyecto, CONCAT(nombres, ' ', apellidos) AS " +
                "nombre_completo FROM encargados_proyectos WHERE id_organizacion_vinculada = ?";
        List<ProjectManagerDTO> projectManagersList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BY_ORGANIZATION)) {
                preparedStatement.setInt(1, organizationId);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();
                        projectManagerDTO.setId(resultSet.getInt("id_encargado_proyecto"));
                        projectManagerDTO.setFirstName(resultSet.getString("nombre_completo"));
                        projectManagersList.add(projectManagerDTO);
                    }
                }
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al obtener los encargados por organización.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al filtrar encargados por organización.", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al filtrar encargados por organización.", e);
            } else {
                throw new DAOException("Error interno de base de datos al filtrar encargados por organización.", e);
            }
        }

        return projectManagersList;
    }
}