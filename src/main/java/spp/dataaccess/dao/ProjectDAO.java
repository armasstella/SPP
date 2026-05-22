package spp.dataaccess.dao;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO implements IProjectDAO {
    private static final int NO_ROWS_AFFECTED = 0;

    public ProjectDAO() {

    }

    @Override
    public boolean addProject(ProjectDTO projectDTO) throws DAOException {
        final String INSERT_PROJECT = "INSERT INTO Proyectos " +
                "(descripcion, " +
                "id_organizacion_vinculada, id_encargado_proyecto, cupo, nombre) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT);
                preparedStatement.setString(1, projectDTO.getDescription());
                preparedStatement.setInt(2, projectDTO.getLinkedOrganizationDTO().getId());
                preparedStatement.setInt(3, projectDTO.getProjectManagerDTO().getId());
                preparedStatement.setInt(4, projectDTO.getPlacesAvailable());
                preparedStatement.setString(5, projectDTO.getName());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Fallo al insertar el proyecto. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el proyecto", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el proyecto. Se viola la integridad de los datos", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al insertar el proyecto", e);
            } finally {
               connection.setAutoCommit(true);
               connection.close();
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }
        return true;
    }

    @Override
    public boolean deleteProject(ProjectDTO projectDTO) throws DAOException {
        final String DELETE_PROJECT = "DELETE FROM Proyectos WHERE id_proyecto = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PROJECT);
                preparedStatement.setInt(1, projectDTO.getId());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Fallo al eliminar el proyecto. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al eliminar el proyecto", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al eliminar el proyecto. Se viola la integridad de los datos", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al eliminar el proyecto", e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }
        return true;
    }

    @Override
    public boolean updateProject(ProjectDTO projectDTO) throws DAOException {
        final String UPDATE_PROJECT = "UPDATE Proyectos SET descripcion = ?, " +
                "nombre = ?, cupo = ?  WHERE id_proyecto = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT)) {

                preparedStatement.setString(1, projectDTO.getDescription());
                preparedStatement.setString(2, projectDTO.getName());
                preparedStatement.setInt(3, projectDTO.getPlacesAvailable());
                preparedStatement.setInt(4, projectDTO.getId());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Fallo al actualizar el proyecto. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al actualizar el proyecto", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al actualizar el proyecto. Se viola la integridad de los datos", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al actualizar el proyecto", e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }
        return true;
    }

    @Override
    public List<ProjectDTO> obtainAllProjects() throws DAOException {
        List<ProjectDTO> projectsList = new ArrayList<>();
        final String SELECT_ALL_PROJECTS = "SELECT " +
                "p.id_proyecto, " +
                "p.nombre, " +
                "p.descripcion, " +
                "p.disponibilidad, " +
                "p.cupo, " +
                "ov.nombre as 'nombre_ov', " +
                "CONCAT(ep.nombres, ' ', ep.apellidos) as 'nombre_rp' " +
                "FROM proyectos p " +
                "INNER JOIN organizaciones_vinculadas ov " +
                " ON p.id_organizacion_vinculada = ov.id_organizacion_vinculada " +
                "INNER JOIN encargados_proyectos ep " +
                " ON ep.id_encargado_proyecto = p.id_encargado_proyecto;";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_PROJECTS);
            ResultSet resultSet = preparedStatement.executeQuery();

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

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener lista de proyectos.");
        }

        return projectsList;
    }
}