package spp.businesslogic.dao;

import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DataAccessException;
import spp.businesslogic.exceptions.InstructorException;
import spp.businesslogic.exceptions.LogicLayerException;
import spp.businesslogic.interfaces.IInstructorDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class InstructorDAO implements IInstructorDAO {

    private final UserDAO userDAO = new UserDAO();

    public InstructorDAO() {

    }

    @Override
    public void addInstructor(InstructorDTO instructorDTO) throws InstructorException {
        try {
            int generatedId = userDAO.insertUser(instructorDTO);
            insertInstructor(instructorDTO, generatedId);
        } catch (LogicLayerException e) {
            AppLogger.logError(e);
            throw InstructorException.insertError(e);
        }
    }

    @Override
    public void insertInstructor(InstructorDTO instructorDTO, int userId) throws DataAccessException {
        final String INSERT_INSTRUCTOR = "INSERT INTO profesor " +
                "(id_usuario, num_personal, turno) VALUES (?, ?, ?)";

        MySQLConnection database = new MySQLConnection();
        Connection connection = null;

        try {
            connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSTRUCTOR);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, instructorDTO.getNumeroPersonal());
            preparedStatement.setString(3, instructorDTO.getTurno());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Fallo al insertar al instructor. No se afectaron filas.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error de integridad al insertar instructor", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error al insertar instructor", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    AppLogger.logError(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            InstructorDAO instructorDAO = new InstructorDAO();

            instructorDAO.addInstructor(new InstructorDTO("null", "2026-03-27 14:33:08",
                    "Juan", "",
                    "Alfonso", "Rodriguez", "juan@email.com",
                    "2299000011", "contr4s3ñA..", "54321", "Matutino"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}