package spp.businesslogic.dao;


import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IPrioritizedProjectDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.dataaccess.connection.MySQLConnectionManager;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class PrioritizedProjectDAO implements IPrioritizedProjectDAO {

    @Override
    public boolean savePrioritizedProjects(String email, List<ProjectDTO> chosenProjects) throws DAOException {
        boolean areProjectsSaved = false;
        final String SAVE_PRIORITIZED_PROJECT =
                "INSERT INTO proyectos_priorizados " +
                        "(id_usuario_practicante, matricula, id_proyecto, nivel_prioridad) " +
                        "SELECT p.id_usuario, p.matricula, ?, ? " +
                        "FROM practicantes p WHERE p.matricula = ?";
        InternDAO internDAO = new InternDAO();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_PRIORITIZED_PROJECT);

            int insertedRows = 0;
            for (int index = 0; index < chosenProjects.size(); index++) {
                int priorityLevel = index + 1;
                preparedStatement.setInt(1, chosenProjects.get(index).getId());
                preparedStatement.setInt(2, priorityLevel);
                preparedStatement.setString(3, internDAO.obtainStudentNumber(email));
                insertedRows += preparedStatement.executeUpdate();
            }

            if (insertedRows == chosenProjects.size()) {
                connection.commit();
                areProjectsSaved = true;
            } else {
                connection.rollback();
            }

        } catch (SQLException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error al guardar los proyectos priorizados");
        } finally {
            MySQLConnectionManager.getInstance().enableAutoCommitConnection();
        }

        return areProjectsSaved;
    }

    @Override
    public boolean searchPrioritizedProjectsRegister(String email) throws DAOException {
        final String SEARCH = "SELECT f_tiene_proyectos_priorizados(?)";
        boolean isSearchSuccesful = false;
        UserDAO userDAO = new UserDAO();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH);
            preparedStatement.setInt(1, userDAO.obtainId(email));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccesful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar proyectos priorizados");
        }

        return isSearchSuccesful;

    }

}