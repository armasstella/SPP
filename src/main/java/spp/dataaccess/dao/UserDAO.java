package spp.dataaccess.dao;

import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IUserDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.ResultSet;


class UserDAO implements IUserDAO {

    public UserDAO() {

    }

    @Override
    public int addUser(UserDTO userDTO) throws DAOException {
        final String INSERT_USER = "INSERT INTO Usuarios " +
                "(estado, ultima_conexion, nombre, apellidos, " +
                "correo_electronico, telefono, contraseña) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_USER, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, userDTO.getStatus());
            preparedStatement.setString(2, userDTO.getLastConnection());
            preparedStatement.setString(3, userDTO.getFirstName() + " " + userDTO.getSecondName());
            preparedStatement.setString(4, userDTO.getFirstLastName() + " " +
                    userDTO.getSecondLastName());
            preparedStatement.setString(5, userDTO.getEmail());
            preparedStatement.setString(6, userDTO.getPhoneNumber());
            preparedStatement.setString(7, userDTO.getPassword());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DAOException("Fallo al insertar el usuario. No se afectaron filas.");
            }

            return getGeneratedKey(preparedStatement);

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DAOException("Error de integridad de datos al insertar usuario", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error general al insertar usuario", e);
        }
    }

    @Override
    public int getGeneratedKey(PreparedStatement preparedStatement) throws DAOException {
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (!resultSet.next()) {
                throw new DAOException("No se generó ninguna llave.");
            }
            return resultSet.getInt(1);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener llave generada", e);
        }
    }

    @Override
    public int obtainId(String email) throws DAOException {
        final String SELECT_ID = "SELECT id FROM Usuarios WHERE correo_electronico = ?";
        try (Connection connection = MySQLConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
                throw new DAOException("Usuario no encontrado con email: " + email);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener ID de usuario", e);
        }
    }
}