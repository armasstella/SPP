package spp.businesslogic.dao;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.exceptions.DataAccessException;
import spp.businesslogic.exceptions.InternException;
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
    public void addIntern(InternDTO internDTO) throws InternException {
        try {
            int generatedId = userDAO.insertUser(internDTO);
            insertIntern(internDTO, generatedId);
        } catch (LogicLayerException e) {
            AppLogger.logError(e);
            throw InternException.insertError(e);
        }
    }

    @Override
    public void insertIntern(InternDTO internDTO, int userId) throws LogicLayerException {
        final String INSERT_INTERN = "INSERT INTO practicante " +
                "(id_usuario, matricula, sexo, habla_lengua_indigena, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?)";

        MySQLConnection database = new MySQLConnection();
        Connection connection = null;

        try {
            connection = database.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTERN);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, internDTO.getStudentNumber());
            preparedStatement.setString(3, internDTO.getGender());
            preparedStatement.setBoolean(4, internDTO.getSpeaksIndigenousLanguage());
            preparedStatement.setTimestamp(5, Timestamp.valueOf(internDTO.getFechaNacimiento()));

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Fallo al insertar al practicante. No se afectaron filas.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new LogicLayerException("Error de integridad al insertar practicante", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new LogicLayerException("Error al insertar practicante", e);
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