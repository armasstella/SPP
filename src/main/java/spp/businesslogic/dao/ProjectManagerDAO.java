package spp.businesslogic.dao;


import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProjectManagerDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


public class ProjectManagerDAO implements IProjectManagerDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    public ProjectManagerDAO() {

    }

    @Override
    public boolean addProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException {
        final String INSERT_PROJECT_MANAGER = "INSERT INTO Encargados_Proyectos " + "(nombres, apellidos, " +
            "responsabilidad, rol, telefono)" + "VALUES (?, ?, ?, ?, ?)";
        boolean isAddSuccesful = false;

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
                    throw new DAOException("WARN: Fallo al insertar encargado de proyecto. No se afectaron filas");
                }

                connection.commit();
                isAddSuccesful = true;

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("WARN: Violación de integridad de datos al insertar", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error general al insertar encargado de proyecto", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al insertar encargado de proyecto", e);
        }

        return isAddSuccesful;

    }

    @Override
    public boolean updateProjectManagerDAO(ProjectManagerDTO projectManagerDTO) throws DAOException {
        return true;
    }

    @Override
    public List<ProjectManagerDTO> obtainActiveProjectManagers() throws DAOException {
        final String SELECT_PROJECT_MANAGER = "SELECT id_encargado_proyecto, CONCAT(nombres, ' ', apellidos) " +
                "AS nombre_completo FROM encargados_proyectos";
        List<ProjectManagerDTO> projectManagersList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECT_MANAGER);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                ProjectManagerDTO projectManager = new ProjectManagerDTO();
                projectManager.setId(resultSet.getInt("id_encargado_proyecto"));
                projectManager.setFirstName(resultSet.getString("nombre_completo"));
                projectManagersList.add(projectManager);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar encargados proyectos");
        }

        return projectManagersList;

    }

    @Override
    public boolean searchProjectManagerRegisters() throws DAOException {
        final String SEARCH_REGISTERS = "SELECT f_hay_encargados_proyectos()";
        boolean isSearchSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_REGISTERS);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccesful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar encargados proyectos");
        }

        return isSearchSuccesful;
    }

}
