package spp.businesslogic.dao;

import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IUserDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;
import spp.utils.password.PasswordHasher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLDataException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Statement;

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
                    throw new DAOException("Fallo al insertar usuario. No se afectaron filas.");
                }

                return getGeneratedKey(preparedStatement);
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("El Usuario que usted está intentando registrar ya existe y está activo.", e);

        } catch (SQLDataException e) {
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("El formato o la longitud de los datos ingresados no es compatible.", e);

        } catch (SQLTransactionRollbackException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de concurrencia: la transacción fue abortada por el servidor.", e);

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor de datos.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al conectar con el servidor.", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al insertar usuario", e);
            } else {
                throw new DAOException("Ocurrió un error interno al intentar registrar al usuario.", e);
            }
        }
    }

    public int getGeneratedKey(PreparedStatement preparedStatement) throws DAOException {
        try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
            if (!resultSet.next()) {
                throw new DAOException("Ocurrió un problema interno al vincular el perfil del usuario. Intente registrarlo nuevamente.");
            }
            return resultSet.getInt(1);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al finalizar el registro. Por favor, vuelva a intentarlo.", e);
        }
    }

    @Override
    public int obtainId(String email) throws DAOException {
        final String SELECT_ID = "SELECT id_usuario FROM Usuarios WHERE correo_electronico = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID)) {

                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt("id_usuario");
                    }
                    throw new DAOException("Usuario no encontrado con email: " + email);
                }
            }
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al obtener id usuario", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar id usuario", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener id usuario", e);
            } else {
                throw new DAOException("Error interno de base de datos al obtener id usuario", e);
            }
        }
    }

    @Override
    public LoginResultDTO login(String email, String password) throws DAOException {
        final String CALL_SP_OBTAIN_USER = "CALL sp_obtener_usuario_login(?)";
        LoginResultDTO loginResultDTO = new LoginResultDTO();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CALL_SP_OBTAIN_USER)) {

                preparedStatement.setString(1, email);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        if (passwordHasher.verifyPassword(resultSet.getString("contraseña"), password)) {
                            String userType = resultSet.getString("tipo_usuario");
                            loginResultDTO = loginResultDTO.success(userType);
                        } else {
                            loginResultDTO = loginResultDTO.failure("Contraseña incorrecta");
                        }
                    } else {
                        loginResultDTO = loginResultDTO.failure("Correo incorrecto");
                    }
                }
            }
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación en el servidor al intentar iniciar sesión.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al verificar credenciales.", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al intentar iniciar sesión", e);
            } else {
                throw new DAOException("Error crítico de base de datos", e);
            }
        }

        return loginResultDTO;
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
                    }
                }
            }
        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de autenticación al buscar disponibilidad del email.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar el email.", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error en conexión al buscar email", e);
            } else {
                throw new DAOException("Error interno de base de datos al buscar email", e);
            }
        }

        return emailExists;
    }
}