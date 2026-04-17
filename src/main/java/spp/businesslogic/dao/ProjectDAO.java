package spp.businesslogic.dao;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.LogicLayerException;
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
    public void addProject(ProjectDTO projectDTO) throws DAOException {
        final String INSERT_PROJECT = "INSERT INTO Proyecto " +
                "(descripcion, disponibilidad, id_practicante_usuario, id_practicante_matricula, " +
                "id_coordinador_usuario, id_coordinador_num_personal) VALUES " +
                "(?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROJECT);
                preparedStatement.setString(1, projectDTO.getDescription());
                preparedStatement.setString(2, String.valueOf(projectDTO.getDisponibility()));
                preparedStatement.setInt(3, 30);
                preparedStatement.setString(4, "S21099999");
                preparedStatement.setInt(5, 1);
                preparedStatement.setString(6, "00100");

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new LogicLayerException("Fallo al insertar el proyecto. No se afectaron filas.");
                }

                connection.commit();

            } catch (LogicLayerException | SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } finally {
               connection.setAutoCommit(true);
               connection.close();
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw DAOException.insertError(e);
        }
    }
}