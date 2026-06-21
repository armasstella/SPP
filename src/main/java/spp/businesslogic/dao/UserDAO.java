package spp.businesslogic.dao;


import spp.businesslogic.dto.ActiveSessionDTO;
import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.dto.SessionDTO;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IUserDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import spp.utils.password.PasswordHasher;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.ResultSet;


public class UserDAO implements IUserDAO {

    private static final int NO_ROWS_AFFECTED = 0;
    private final PasswordHasher passwordHasher = new PasswordHasher();

    public UserDAO() {

    }

    @Override
    public int registerUser(UserDTO userDTO) throws DAOException {
        final String INSERT_USER = "INSERT INTO Usuarios (nombre, apellidos, correo_electronico, telefono, contraseña) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1,
                        userDTO.getFirstName() + " " + userDTO.getSecondName());
                preparedStatement.setString(2,
                        userDTO.getFirstLastName() + " " + userDTO.getSecondLastName());
                preparedStatement.setString(3,
                        userDTO.getEmail());
                preparedStatement.setString(4,
                        userDTO.getPhoneNumber());
                preparedStatement.setString(5,
                        passwordHasher.hashPassword(userDTO.getPassword()));

                if (preparedStatement.executeUpdate() == NO_ROWS_AFFECTED) {
                    throw new DAOException("WARN: Fallo al insertar usuario. No se afectaron filas.");
                }

                return getGeneratedKey(preparedStatement);
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DAOException("WARN: Violación de integridad de datos al insertar", e);

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al insertar usuario", e);
        }

    }


    public int getGeneratedKey(PreparedStatement preparedStatement) throws DAOException {
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (!resultSet.next()) {
                throw new DAOException("WARN: No se generó ninguna Primary Key");
            }
            return resultSet.getInt(1);

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("ERROR: Error al obtener llave generada", e);
        }

    }

    @Override
    public int obtainId(String email) throws DAOException {
        final String SELECT_ID = "SELECT id_usuario FROM Usuarios WHERE correo_electronico = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID);
            preparedStatement.setString(1, email);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_usuario");
                }
                throw new DAOException("ERROR: Usuario no encontrado con email: " + email);
            }

        } catch (SQLException e) {
            throw new DAOException("FATAL: Error de conexión al obtener id usuario", e);
        }

    }

    @Override
    public LoginResultDTO login(String email, String password) throws DAOException {
        final String CALL_SP_OBTAIN_USER = "CALL sp_obtener_usuario_login(?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CALL_SP_OBTAIN_USER)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedHash = resultSet.getString("contraseña");
                        String userType = resultSet.getString("tipo_usuario");

                        if (passwordHasher.verifyPassword(storedHash, password)) {

                            int idUser = obtainId(email);
                            ActiveSessionDTO.initialize(new SessionDTO(email));

                            return new LoginResultDTO(userType);
                        }
                    }
                }

                throw new DAOException("ERROR: Credenciales incorrectas");
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al verificar credenciales", e);
        }

    }

    @Override
    public boolean existsEmailRegister(String email) throws DAOException {
        final String SEARCH_EMAIL = "SELECT f_existe_correo_electronico(?)";
        boolean emailExists = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_EMAIL)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        emailExists = resultSet.getBoolean(1);
                        if (!emailExists) {
                            throw new DAOException("ERROR: Correo no encontrado en el sistema");
                        }
                    }

                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error en conexión al buscar email", e);
        }

        return emailExists;

    }

}