package spp.businesslogic.dao;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.LogicLayerException;
import spp.businesslogic.interfaces.IInternDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class InternDAO implements IInternDAO {

    private final UserDAO userDAO = new UserDAO();

    public InternDAO() {

    }

    @Override
    public void addIntern(InternDTO internDTO) throws DAOException {
        final String INSERT_INTERN = "INSERT INTO practicante " +
                "(id_usuario, matricula, sexo, habla_lengua_indigena, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                int generatedId = userDAO.insertUser(internDTO);

                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTERN);
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, internDTO.getStudentNumber());
                preparedStatement.setString(3, internDTO.getGender());
                preparedStatement.setBoolean(4, internDTO.getSpeaksIndigenousLanguage());
                preparedStatement.setTimestamp(5, Timestamp.valueOf(internDTO.getFechaNacimiento()));

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new LogicLayerException("Fallo al insertar al practicante. No se afectaron filas.");
                }

                connection.commit();

            } catch (LogicLayerException | SQLIntegrityConstraintViolationException e) {
                connection.rollback();
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

        InternDAO internDAO = new InternDAO();

        try {
            InternDTO intern = new InternDTO(
                    "activo",
                    LocalDateTime.now().toString(),
                    "Luis",
                    "",
                    "Torres",
                    "Garcia",
                    "lutogar@uv.mx",
                    "2280000000",
                    "sfdusod.-sd.",
                    "S21099999",
                    "M",
                    false,
                    LocalDateTime.of(2001, 5, 15, 0, 0)
            );
            internDAO.addIntern(intern);
            System.out.println("Practicante insertado correctamente.");
        } catch (Exception e) {
            System.out.println("Error esperado: " + e.getMessage());
            e.printStackTrace();
        }
    }
}