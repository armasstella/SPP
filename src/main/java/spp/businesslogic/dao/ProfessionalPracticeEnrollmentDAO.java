package spp.businesslogic.dao;


import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProfessionalPracticeEnrollmentDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


public class ProfessionalPracticeEnrollmentDAO implements IProfessionalPracticeEnrollmentDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    public ProfessionalPracticeEnrollmentDAO() {

    }

    @Override
    public boolean addProfessionalPracticeEnrollment(ProfessionalPracticeEnrollmentDTO
        professionalPracticeEnrollmentDTO) throws DAOException {
        final String INSERT_PROFESSIONAL_PRACTICE_ENROLLMENT = "INSERT INTO Inscripciones_Practicas_Profesionales " +
                "(nrc, periodo, id_usuario_practicante, matricula, id_usuario_profesor, num_personal, " +
                "calificacion_final, id_proyecto, horas_cubiertas) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        boolean isAddSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement
                    (INSERT_PROFESSIONAL_PRACTICE_ENROLLMENT);
                preparedStatement.setString(1, professionalPracticeEnrollmentDTO.getNrc());
                preparedStatement.setString(2, professionalPracticeEnrollmentDTO.getTerm());
                preparedStatement.setInt(3, professionalPracticeEnrollmentDTO.getInternDTO().getId());
                preparedStatement.setString(4, professionalPracticeEnrollmentDTO.getInternDTO().
                        getStudentNumber());
                preparedStatement.setInt(5, professionalPracticeEnrollmentDTO.getInstructorDTO().getId());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("Fallo al insertar la inscripción. No se afectaron filas.");
                }

                connection.commit();
                isAddSuccesful = true;

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar la inscripción",e);

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar la inscripción. Se viola la integridad de los datos", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al insertar la inscripción", e);

            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }

        return isAddSuccesful;

    }

}
