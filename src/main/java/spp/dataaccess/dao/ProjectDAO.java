package spp.dataaccess.dao;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ProjectDAO implements IProjectDAO {

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
                if (affectedRows == 0) {
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
}