package spp.businesslogic.dao;

import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.CoordinatorException;
import spp.businesslogic.exceptions.LogicLayerException;
import spp.businesslogic.interfaces.ICoordinatorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class CoordinatorDAO implements ICoordinatorDAO {

    private final UserDAO userDAO = new UserDAO();

    public CoordinatorDAO() {

    }

    @Override
    public void addCoordinator(CoordinatorDTO coordinatorDTO) throws CoordinatorException {
        try {
            int generatedId = userDAO.insertUser(coordinatorDTO);
            insertCoordinator(coordinatorDTO, generatedId);
        } catch (LogicLayerException e) {
            AppLogger.logError(e);
            throw CoordinatorException.insertError(e);
        }

    }

    @Override
    public void insertCoordinator(CoordinatorDTO dto, int userId) throws LogicLayerException {
        final String INSERT_COORDINATOR = "INSERT INTO coordinador " +
                "(id_usuario, num_personal) VALUES (?, ?)";

        MySQLConnection database = new MySQLConnection();

        try (Connection connection = database.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COORDINATOR);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, dto.getNumeroPersonal());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new LogicLayerException("Fallo al insertar al coordinador. No se afectaron filas.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new LogicLayerException("Error de integridad al insertar coordinador", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new LogicLayerException("Error al insertar coordinador", e);
        }
    }

    public static void main(String[] args) {
        try {
            CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

            coordinatorDAO.addCoordinator(new CoordinatorDTO("null", "2026-04-13 17:12:08",
                    "Luna", "Luisa",
                    "Linares", "Contreras", "lululico@email.com",
                    "2289090456", "sdj8sdyd.", "12300"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}