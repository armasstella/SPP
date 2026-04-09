package spp.businesslogic.dao;

import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.interfaces.IUserDAO;
import spp.dataaccess.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class UserDAO implements IUserDAO {

    public UserDAO() {

    }

    @Override
    public void addUser(UserDTO userDTO) {

        String sqlUsuario = "INSERT INTO usuario" +
                "(estado, ultima_conexion, nombre, apellidos, " +
                "correo_electronico, telefono, contraseña) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUsuario, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, userDTO.getStatus());
                preparedStatement.setString(2, userDTO.getLastConnection());
                preparedStatement.setString(3, userDTO.getFirstName() + " " + userDTO.getSecondName());
                preparedStatement.setString(4, userDTO.getFirstLastName() + " " + userDTO.getSecondLastName());
                preparedStatement.setString(5, userDTO.getEmail());
                preparedStatement.setString(6, userDTO.getPhoneNumber());
                preparedStatement.setString(7, userDTO.getPassword());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Fallo al insertar el usuario. No se afectaron filas.");
                }

            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
