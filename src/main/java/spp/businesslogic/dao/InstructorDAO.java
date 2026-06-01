package spp.businesslogic.dao;


import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInstructorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class InstructorDAO implements IInstructorDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    public InstructorDAO() {

    }

    @Override
    public boolean addInstructor(InstructorDTO instructorDTO) throws DAOException {
        final String INSERT_INSTRUCTOR = "INSERT INTO Profesores " +
                "(id_usuario, num_personal, turno) VALUES (?, ?, ?)";
        UserDAO userDAO = new UserDAO();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.addUser(instructorDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSTRUCTOR);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, instructorDTO.getPersonalNumber());
                preparedStatement.setString(3, instructorDTO.getShift());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Fallo al insertar al profesor. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el usuario", e);

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new SQLIntegrityConstraintViolationException(
                        "Error al insertar el profesor: Datos duplicados", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar el profesor", e);

            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }

        return true;

    }

    @Override
    public int obtainId(String personalNumber) throws DAOException {
        final String SELECT_ID = "SELECT U.id_usuario FROM Usuarios U INNER JOIN Profesores P " +
                "ON U.id_usuario = P.id_usuario AND P.num_personal = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID);
            preparedStatement.setString(1, personalNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id_usuario");
                }
                throw new DAOException("Usuario no encontrado con número de personal: " + personalNumber);
            }

        } catch (SQLException e) {
            throw new DAOException("Error al obtener profesor", e);
        }

    }

    @Override
    public boolean deactivateInstructor(InstructorDTO instructorDTO) throws DAOException {
        final String INACTIVATE_INSTRUCTOR = "UPDATE Usuarios " +
                "INNER JOIN Profesores ON Usuarios.id_usuario = Profesores.id_usuario " +
                "SET Usuarios.estado = 'Inactivo' " +
                "WHERE Profesores.num_personal = ?;";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INACTIVATE_INSTRUCTOR);
                preparedStatement.setString(1, instructorDTO.getPersonalNumber());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Error. No se afectaron filas al inactivar el profesor.");
                }

                connection.commit();

            } catch (SQLException | DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al inactivar el profesor", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }

        return true;

    }

    @Override
    public List<InstructorDTO> obtainAllActiveInstructors() throws DAOException {
        final String SELECT_ALL_INSTRUCTORS = "SELECT nombre, apellidos, correo_electronico, num_personal, turno " +
                "FROM Usuarios u INNER JOIN Profesores p ON u.id_usuario = p.id_usuario AND u.estado = 'Activo'";
        List<InstructorDTO> instructorsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_INSTRUCTORS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                InstructorDTO instructorDTO = new InstructorDTO();
                instructorDTO.setFirstName(resultSet.getString("nombre"));
                instructorDTO.setFirstLastName(resultSet.getString("apellidos"));
                instructorDTO.setEmail(resultSet.getString("correo_electronico"));
                instructorDTO.setPersonalNumber(resultSet.getString("num_personal"));
                instructorDTO.setShift(resultSet.getString("turno"));
                instructorsList.add(instructorDTO);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener lista de profesores", e);
        }

        return instructorsList;

    }

    @Override
    public List<InstructorDTO> getListActiveInstructors() throws DAOException {
        String query = "SELECT u.id_usuario, p.num_personal, CONCAT(u.nombre, ' ', u.apellidos) AS nombre_completo " +
                "FROM Profesores p " +
                "INNER JOIN Usuarios u ON p.id_usuario = u.id_usuario " +
                "WHERE u.estado = 'Activo'";
        List<InstructorDTO> instructorsList = new ArrayList<>();

        try (Connection connection = MySQLConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                InstructorDTO instructor = new InstructorDTO();
                instructor.setId(resultSet.getInt("id_usuario"));
                instructor.setPersonalNumber(resultSet.getString("num_personal"));
                instructor.setFirstName(resultSet.getString("nombre_completo"));
                instructorsList.add(instructor);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener la lista de profesores activos", e);
        }

        return instructorsList;
    }


}