package spp.businesslogic.dao;


import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInstructorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.dataaccess.connection.MySQLConnectionManager;
import spp.utils.exceptionmanager.ExceptionLevel;
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
    private final UserDAO userDAO = new UserDAO();

    public InstructorDAO() {

    }

    @Override
    public boolean registerInstructor(InstructorDTO instructorDTO) throws DAOException {
        final String INSERT_INSTRUCTOR = "INSERT INTO Profesores " +
                "(id_usuario, num_personal, turno) VALUES (?, ?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            MySQLConnectionManager.getInstance().disableAutoCommitConnection();
            int generatedId = userDAO.registerUser(instructorDTO);

            try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSTRUCTOR)) {
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, instructorDTO.getPersonalNumber());
                preparedStatement.setString(3, instructorDTO.getShift());

                if (preparedStatement.executeUpdate() != NO_ROWS_AFFECTED) {
                    isInsertSuccessful = true;
                    connection.commit();
                }
            }

        } catch (DAOException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(ExceptionLevel.ERROR, e);
            throw new DAOException("Error al insertar profesor", e);

        } catch (SQLIntegrityConstraintViolationException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("Verifique los datos ingresados", e);

        } catch (SQLException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al insertar profesor", e);
        } finally {
            MySQLConnectionManager.getInstance().enableAutoCommitConnection();
        }

        return isInsertSuccessful;

    }

    @Override
    public boolean deactivateInstructor(InstructorDTO instructorDTO) throws DAOException {
        final String INACTIVATE_INSTRUCTOR = "UPDATE Usuarios " +
                "INNER JOIN Profesores ON Usuarios.id_usuario = Profesores.id_usuario " +
                "SET Usuarios.estado = 'Inactivo' " +
                "WHERE Profesores.num_personal = ?;";
        boolean isDeactivationSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(INACTIVATE_INSTRUCTOR)) {
                preparedStatement.setString(1, instructorDTO.getPersonalNumber());
                isDeactivationSuccesful = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al desactivar profesor", e);
        }

        return isDeactivationSuccesful;

    }

    @Override
    public List<InstructorDTO> getActiveInstructors() throws DAOException {
        final String SELECT_ALL_INSTRUCTORS = "SELECT nombre, apellidos, correo_electronico, num_personal, turno " +
                "FROM Usuarios u INNER JOIN Profesores p ON u.id_usuario = p.id_usuario AND u.estado = 'Activo'";
        List<InstructorDTO> instructorsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_INSTRUCTORS);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    InstructorDTO instructorDTO = new InstructorDTO();
                    instructorDTO.setFirstName(resultSet.getString("nombre"));
                    instructorDTO.setFirstLastName(resultSet.getString("apellidos"));
                    instructorDTO.setEmail(resultSet.getString("correo_electronico"));
                    instructorDTO.setPersonalNumber(resultSet.getString("num_personal"));
                    instructorDTO.setShift(resultSet.getString("turno"));
                    instructorsList.add(instructorDTO);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al buscar profesores", e);
        }

        return instructorsList;

    }

    @Override
    public List<InstructorDTO> getActiveInstructorsIdentifiers() throws DAOException {
        final String SELECT_INSTRUCTOR = "SELECT u.id_usuario, p.num_personal, " +
                "CONCAT(u.nombre, ' ', u.apellidos) AS nombre_completo " +
                "FROM Profesores p " +
                "INNER JOIN Usuarios u ON p.id_usuario = u.id_usuario " +
                "WHERE u.estado = 'Activo'";
        List<InstructorDTO> instructorsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INSTRUCTOR);
                ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    InstructorDTO instructor = new InstructorDTO();
                    instructor.setId(resultSet.getInt("id_usuario"));
                    instructor.setPersonalNumber(resultSet.getString("num_personal"));
                    instructor.setFullName(resultSet.getString("nombre_completo"));
                    instructorsList.add(instructor);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al buscar profesores", e);
        }

        return instructorsList;

    }

    @Override
    public String findActivePersonalNumberByEmail(String email) throws DAOException {
        final String SELECT_PERSONAL_NUMBER = "SELECT num_personal FROM profesores p INNER JOIN usuarios u " +
                "WHERE p.id_usuario = u.id_usuario AND u.estado = 'Activo' AND u.correo_electronico = ?";
        String personalNumber = null;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PERSONAL_NUMBER)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        personalNumber = resultSet.getString(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al obtener numero personal", e);
        }

        return personalNumber;
    }

}