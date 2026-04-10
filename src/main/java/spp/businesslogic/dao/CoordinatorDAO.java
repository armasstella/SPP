package spp.businesslogic.dao;


import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.interfaces.ICoordinatorDAO;
import spp.dataaccess.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class CoordinatorDAO implements ICoordinatorDAO {

    public CoordinatorDAO() {

    }

    @Override
    public void addCordinator(CoordinatorDTO coordinatorDTO) {
        try (Connection connection = MySQLConnection.getConnection()) {
            connection.setAutoCommit(false);
            try {
                int generatedId = insertUser(connection, coordinatorDTO);
                insertCoordinator(connection, coordinatorDTO, generatedId);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int insertUser(Connection connection, CoordinatorDTO dto) throws SQLException{
        final String INSERT_USER = "INSERT INTO usuario " +
                "(estado, ultima_conexion, nombre, apellidos, " +
                "correo_electronico, telefono, contraseña) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(
                INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, dto.getStatus());
            ps.setString(2, dto.getLastConnection());
            ps.setString(3, dto.getFirstName() + " " + dto.getSecondName());
            ps.setString(4, dto.getFirstLastName() + " " + dto.getSecondLastName());
            ps.setString(5, dto.getEmail());
            ps.setString(6, dto.getPhoneNumber());
            ps.setString(7, dto.getPassword());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Fallo al insertar el usuario. No se afectaron filas.");
            }

            return getGeneratedKey(ps);
        }
    }

    @Override
    public void insertCoordinator(Connection connection, CoordinatorDTO dto, int userId) throws SQLException {
        final String INSERT_COORDINATOR = "INSERT INTO coordinador " +
                "(id_usuario, num_personal) VALUES (?, ?)";

        try (PreparedStatement ps = connection.prepareStatement(INSERT_COORDINATOR)) {
            ps.setInt(1, userId);
            ps.setString(2, dto.getNumeroPersonal());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Fallo al insertar al coordinador. No se afectaron filas.");
            }
        }
    }

    @Override
    public int getGeneratedKey(PreparedStatement preparedStatement) throws SQLException {
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (!resultSet.next()) {
                throw new SQLException("No se generó ninguna llave.");
            }
            return resultSet.getInt(1);
        }
    }

    public static void main (String[] args) {
        try {

            CoordinatorDAO coordinatorDAO = new CoordinatorDAO();

            coordinatorDAO.addCordinator(new CoordinatorDTO("null", "2026-03-27 15:32:08",
                    "Ernesto", "Jael",
                    "Riviera", "Jimenez", "ernesJael@email.com",
                    "9243657895", "pdjfsdi21.-..", "00100"));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
