package spp.businesslogic.interfaces;

import spp.businesslogic.dto.CoordinatorDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ICoordinatorDAO {
    void addCordinator(CoordinatorDTO coordinatorDTO);
    int insertUser(Connection connection, CoordinatorDTO dto) throws SQLException;
    void insertCoordinator(Connection connection, CoordinatorDTO dto, int userId) throws SQLException;
    int getGeneratedKey(PreparedStatement preparedStatement) throws SQLException;
}
