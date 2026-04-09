package spp.businesslogic.dao;


import spp.businesslogic.dto.CoordinatorDTO;
import spp.dataaccess.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class CoordinatorDAO {

    public CoordinatorDAO() {

    }

    public static void addCoordinator(CoordinatorDTO coordinatorDTO) {

        final String INSERT_USER = "INSERT INTO usuario" +
                "(estado, ultima_conexion, nombre, apellidos, " +
                "correo_electronico, telefono, contraseña) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String INSERT_COORDINATOR = "INSERT INTO coordinador " +
                "(id_usuario, num_personal) "  +
                "VALUES (?, ?)";

        try (Connection connection = MySQLConnection.getConnection ()) {

            connection.setAutoCommit(false);

            int idGenerated = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER,
                    Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, coordinatorDTO.getStatus());
                preparedStatement.setString(2, coordinatorDTO.getLastConnection());
                preparedStatement.setString(3, coordinatorDTO.getFirstName() + " "
                        + coordinatorDTO.getSecondName());
                preparedStatement.setString(4, coordinatorDTO.getFirstLastName() + " "
                        + coordinatorDTO.getSecondLastName());
                preparedStatement.setString(5, coordinatorDTO.getEmail());
                preparedStatement.setString(6, coordinatorDTO.getPhoneNumber());
                preparedStatement.setString(7, coordinatorDTO.getPassword());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Fallo al insertar el usuario. No se afectaron filas.");
                }

                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {

                    if (!resultSet.next()) {
                        throw new SQLException("No se generó ninguna llave");
                    }
                    idGenerated = resultSet.getInt(1);

                }

            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COORDINATOR)) {
                preparedStatement.setInt(1, idGenerated);
                preparedStatement.setString (2, coordinatorDTO.getNumeroPersonal ());

                int affectedRows = preparedStatement.executeUpdate ();

                if (affectedRows == 0) {
                    throw new SQLException ("Fallo al insertar al coordinador. No se afectaron filas.");
                }

            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main (String[] args) {
        try {
            CoordinatorDAO.addCoordinator(new CoordinatorDTO("null", "2026-03-27 15:32:08",
                    "Ernesto", "Jael",
                    "Riviera", "Jimenez", "ernesJael@email.com",
                    "9243657895", "pdjfsdi21.-..", "00100"));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
