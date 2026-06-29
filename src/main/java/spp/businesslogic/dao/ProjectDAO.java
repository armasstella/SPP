package spp.businesslogic.dao;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectDAO;
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements IProjectDAO {

    public ProjectDAO() {
    }

    @Override
    public int registerProject(ProjectDTO projectDTO) throws DAOException {
        final String INSERT_PROJECT = "INSERT INTO Proyectos " +
                "(descripcion, id_organizacion_vinculada, id_encargado_proyecto, cupo, nombre) " +
                "VALUES (?, ?, ?, ?, ?)";
        int projectIdGenerated = -1;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT,
                    Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, projectDTO.getDescription());
                preparedStatement.setInt(2, projectDTO.getLinkedOrganizationDTO().getId());
                preparedStatement.setInt(3, projectDTO.getProjectManagerDTO().getId());
                preparedStatement.setInt(4, projectDTO.getPlacesAvailable());
                preparedStatement.setString(5, projectDTO.getName());

                preparedStatement.executeUpdate();

                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        projectIdGenerated = resultSet.getInt(1);
                    }
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("El proyecto no pudo ser registrado. Verifique que los datos no estén duplicados " +
                    "o que la organización y el encargado existan.", e);

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al registrar el proyecto.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al registrar el proyecto.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al registrar el proyecto.", e);
            } else {
                throw new DAOException("Ocurrió un error interno al intentar registrar el proyecto.", e);
            }
        }

        return projectIdGenerated;
    }

    @Override
    public boolean deleteProject(ProjectDTO projectDTO) throws DAOException {
        final String DELETE_PROJECT = "DELETE FROM Proyectos WHERE id_proyecto = ?";
        boolean isDeletionSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT)) {
                preparedStatement.setInt(1, projectDTO.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows != BaseDAO.NO_ROWS_AFFECTED) {
                    isDeletionSuccesful = true;
                }
            }

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al eliminar el proyecto.", e);
            } else {
                throw new DAOException("Ocurrió un error interno al intentar eliminar el proyecto.", e);
            }
        }

        return isDeletionSuccesful;
    }

    @Override
    public boolean updateProject(ProjectDTO projectDTO) throws DAOException {
        final String UPDATE_PROJECT = "UPDATE Proyectos SET descripcion = ?, " +
                "nombre = ?, cupo = ? WHERE id_proyecto = ?";
        boolean isUpdateSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT)) {
                preparedStatement.setString(1, projectDTO.getDescription());
                preparedStatement.setString(2, projectDTO.getName());
                preparedStatement.setInt(3, projectDTO.getPlacesAvailable());
                preparedStatement.setInt(4, projectDTO.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows != BaseDAO.NO_ROWS_AFFECTED) {
                    isUpdateSuccesful = true;
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo actualizar el proyecto. Verifique que los datos ingresados sean válidos " +
                    "y no estén duplicados.", e);

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al actualizar el proyecto.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al actualizar el proyecto.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al actualizar el proyecto.", e);
            } else {
                throw new DAOException("Ocurrió un error interno al intentar actualizar el proyecto.", e);
            }
        }

        return isUpdateSuccesful;
    }

    @Override
    public List<ProjectDTO> findProjectsDetailsForActiveTerm() throws DAOException {
        final String SELECT_ALL_PROJECTS = "SELECT p.id_proyecto, p.nombre, p.descripcion, p.disponibilidad, " +
                "p.cupo, ov.nombre as 'nombre_ov', CONCAT(ep.nombres, ' ', ep.apellidos) as 'nombre_rp' " +
                "FROM proyectos p " +
                "INNER JOIN organizaciones_vinculadas ov ON p.id_organizacion_vinculada = ov.id_organizacion_vinculada " +
                "INNER JOIN encargados_proyectos ep ON ep.id_encargado_proyecto = p.id_encargado_proyecto " +
                "INNER JOIN periodos per ON p.id_periodo = per.id_periodo " +
                "WHERE per.periodoActual = 1 AND p.disponibilidad = 'Disponible'";
        List<ProjectDTO> projectsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PROJECTS);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    ProjectDTO projectDTO = new ProjectDTO();
                    LinkedOrganizationDTO linkedOrganizationDTO = new LinkedOrganizationDTO();
                    ProjectManagerDTO projectManagerDTO = new ProjectManagerDTO();

                    projectDTO.setId(resultSet.getInt("id_proyecto"));
                    projectDTO.setName(resultSet.getString("nombre"));
                    projectDTO.setDescription(resultSet.getString("descripcion"));
                    projectDTO.setAvailability(resultSet.getString("disponibilidad"));
                    projectDTO.setPlacesAvailable(resultSet.getInt("cupo"));

                    linkedOrganizationDTO.setName(resultSet.getString("nombre_ov"));
                    projectDTO.setLinkedOrganizationDTO(linkedOrganizationDTO);

                    projectManagerDTO.setFirstName(resultSet.getString("nombre_rp"));
                    projectDTO.setProjectManagerDTO(projectManagerDTO);

                    projectsList.add(projectDTO);
                }
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al obtener detalles de los proyectos.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar los proyectos.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener proyectos.", e);
            } else {
                throw new DAOException("Ocurrió un error interno al consultar la lista de proyectos.", e);
            }
        }

        return projectsList;
    }

    @Override
    public boolean hasMinimumProjectsForActiveTerm() throws DAOException {
        final String CHECK_MINIMUM_PROJECT = "SELECT f_hay_cantidad_minima_proyectos()";
        boolean hasMinimum = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_MINIMUM_PROJECT);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    hasMinimum = resultSet.getBoolean(1);
                }
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al verificar el mínimo de proyectos.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al verificar la cantidad de proyectos.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al contar proyectos.", e);
            } else {
                throw new DAOException("Ocurrió un error interno al verificar el mínimo de proyectos.", e);
            }
        }

        return hasMinimum;
    }
}