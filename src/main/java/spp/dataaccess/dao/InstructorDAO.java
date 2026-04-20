package spp.dataaccess.dao;

import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
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
    public void addInstructor(InstructorDTO instructorDTO) throws DAOException {
        final String INSERT_INSTRUCTOR = "INSERT INTO profesor " +
                "(id_usuario, num_personal, turno) VALUES (?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.insertUser(instructorDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INSTRUCTOR);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, instructorDTO.getNumeroPersonal());
                preparedStatement.setString(3, instructorDTO.getTurno());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new LogicLayerException("Fallo al insertar al instructor. No se afectaron filas.");
                }

                connection.commit();

            } catch (LogicLayerException | SQLIntegrityConstraintViolationException e) {
                connection.rollback();;
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw DAOException.insertError(e);
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