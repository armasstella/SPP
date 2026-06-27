package spp.businesslogic.dao;


import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ProjectDAO implements IProjectDAO {

    private static final int NO_ROWS_AFFECTED = 0;

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

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT, Statement.RETURN_GENERATED_KEYS);) {
                preparedStatement.setString(1, projectDTO.getDescription());
                preparedStatement.setInt(2, projectDTO.getLinkedOrganizationDTO().getId());
                preparedStatement.setInt(3, projectDTO.getProjectManagerDTO().getId());
                preparedStatement.setInt(4, projectDTO.getPlacesAvailable());
                preparedStatement.setString(5, projectDTO.getName());
                preparedStatement.executeUpdate();
                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        projectIdGenerated = resultSet.getInt(1);
                        System.out.println(projectIdGenerated);
                    }
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("Verifique los datos ingresados", e);


        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al insertar proyecto", e);
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
                if (affectedRows != NO_ROWS_AFFECTED) {
                    isDeletionSuccesful = true;
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al eliminar proyecto", e);
        }

        return isDeletionSuccesful;

    }

    @Override
    public boolean updateProject(ProjectDTO projectDTO) throws DAOException {
        final String UPDATE_PROJECT = "UPDATE Proyectos SET descripcion = ?, " +
                "nombre = ?, cupo = ?  WHERE id_proyecto = ?";
        boolean isUpdateSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_PROJECT)) {
                preparedStatement.setString(1, projectDTO.getDescription());
                preparedStatement.setString(2, projectDTO.getName());
                preparedStatement.setInt(3, projectDTO.getPlacesAvailable());
                preparedStatement.setInt(4, projectDTO.getId());
                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows != NO_ROWS_AFFECTED) {
                    isUpdateSuccesful = true;
                }
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("Verifique los datos ingresados", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al actualizar proyecto", e);
        }

        return isUpdateSuccesful;

    }

    @Override
    public List<ProjectDTO> findProjectsDetailsForActiveTerm() throws DAOException {
        final String SELECT_ALL_PROJECTS = "SELECT  p.id_proyecto, p.nombre, p.descripcion, p.disponibilidad, " +
                "p.cupo, ov.nombre as 'nombre_ov', CONCAT(ep.nombres, ' ', ep.apellidos) as 'nombre_rp' " +
                "FROM proyectos p INNER JOIN organizaciones_vinculadas ov ON " +
                "p.id_organizacion_vinculada = ov.id_organizacion_vinculada " +
                "INNER JOIN encargados_proyectos ep " +
                "ON ep.id_encargado_proyecto = p.id_encargado_proyecto";
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

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al obtener proyectos.", e);
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

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al contar proyectos", e);
        }

        return hasMinimum;

    }



}