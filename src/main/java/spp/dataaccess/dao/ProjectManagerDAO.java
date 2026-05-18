package spp.dataaccess.dao;

import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectManagerDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class ProjectManagerDAO implements IProjectManagerDAO {
    private static final int NO_ROWS_AFFECTED = 0;

    public ProjectManagerDAO() {

    }

    @Override
    public boolean addProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException {
        final String INSERT_PROJECT_MANAGER = "INSERT INTO Encargados_Proyectos " + "(nombres, apellidos, " +
            "responsabilidad, rol, telefono)" + "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT_MANAGER);
                preparedStatement.setString(1, projectManagerDTO.getFirstName() + " " +
                    projectManagerDTO.getSecondName());
                preparedStatement.setString(2, projectManagerDTO.getFirstLastName() + " " +
                    projectManagerDTO.getSecondLastName());
                preparedStatement.setString(3, projectManagerDTO.getResponsability());
                preparedStatement.setString(4, projectManagerDTO.getRole());
                preparedStatement.setString(5, projectManagerDTO.getPhoneNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Fallo al insertar el encargado del proyecto. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el encargado del proyecto", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el encargado del proyecto. Se viola la integridad de " +
                        "los datos", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al insertar el encargado del proyecto", e);
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
    public boolean updateProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException {
        return true;
    }
}
