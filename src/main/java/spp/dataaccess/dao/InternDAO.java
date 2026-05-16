package spp.dataaccess.dao;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInternDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.sql.ResultSet;

public class InternDAO implements IInternDAO {

    private final UserDAO userDAO = new UserDAO();

    public InternDAO() {

    }

    @Override
    public boolean addIntern(InternDTO internDTO) throws DAOException {
        boolean isAddSuccessful = false;
        final String INSERT_INTERN = "INSERT INTO Practicantes " +
                "(id_usuario, matricula, sexo, habla_lengua_indigena, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.addUser(internDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTERN);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, internDTO.getStudentNumber());
                preparedStatement.setString(3, internDTO.getGender());
                preparedStatement.setString(4, internDTO.getSpeaksIndigenousLanguage() + ": " + internDTO.getIndigenousLanguage());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(internDTO.getBirthDate()));

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Fallo al insertar al practicante. No se afectaron filas.");
                }

                connection.commit();
                isAddSuccessful = true;

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el usuario", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new SQLIntegrityConstraintViolationException("Error al insertar el usuario: Datos duplicados", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el practicante", e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }

        return isAddSuccessful;
    }

    @Override
    public int obtainId(String studentNumber) throws DAOException {
        final String SELECT_ID = "SELECT U.id_usuario FROM Usuarios U INNER JOIN Practicantes P " +
                "ON U.id_usuario = P.id_usuario AND P.matricula = ?";
        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID);
            preparedStatement.setString(1, studentNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_usuario");
                }
                throw new DAOException("Usuario no encontrado con matricula: " + studentNumber);
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener practicante", e);
        }
    }

    @Override
    public boolean searchStudentNumberRegister(String studentNumber) throws DAOException {
        boolean isSearchSuccessful = false;
        final String SEARCH_STUDENT = "SELECT f_existe_estudiante(?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_STUDENT);
            preparedStatement.setString(1, studentNumber);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                    if (!isSearchSuccessful) {
                        throw new DAOException("Esta matrícula no es valida");
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al buscar matricula", e);
        }
        return isSearchSuccessful;
    }



}