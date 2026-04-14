package spp.businesslogic.dao;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DataAccessException;
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
    public void addProject(ProjectDTO projectDTO) {
        final String INSERT_PROJECT = "INSERT INTO Proyecto " +
                "(descripcion, disponibilidad, id_practicante_usuario, id_practicante_matricula, " +
                "id_coordinador_usuario, id_coordinador_num_personal) VALUES " +
                "(?, ?, ?, ?, ?, ?)";

        MySQLConnection database = new MySQLConnection();
        Connection connection = null;

        try {
            connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT);
            preparedStatement.setString(1, projectDTO.getDescription());
            preparedStatement.setString(2, String.valueOf(projectDTO.getDisponibility()));
            preparedStatement.setInt(3, 30);
            preparedStatement.setString(4, "S21099999");
            preparedStatement.setInt(5, 1);
            preparedStatement.setString(6, "00100");

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Fallo al insertar el proyecto. No se afectaron filas.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error de integridad al insertar proyecto", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error al insertar proyecto", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    AppLogger.logError(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        ProjectDAO projectDAO = new ProjectDAO();

        try {
            ProjectDTO project = new ProjectDTO();
            project.setDescription("Desarrollo de sistema de gestión de prácticas profesionales");
            project.setDisponibility(true);

            projectDAO.addProject(project);
            System.out.println("Proyecto insertado correctamente.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

}