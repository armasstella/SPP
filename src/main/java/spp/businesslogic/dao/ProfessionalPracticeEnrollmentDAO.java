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
                    throw new DAOException("WARN: Fallo al insertar la inscripción. No se afectaron filas");
                }

                connection.commit();
                isAddSuccesful = true;

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("WARN: Violación de integridad de datos al insertar", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error general al insertar inscripción", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al insertar inscripción", e);
        }

        return isAddSuccesful;

    }

    @Override
    public boolean assignProjectToInscription(String studentNumber, int idProject) throws DAOException {
        boolean isProjectAssigned = false;
        final String ASSIGN_PROJECT =
                "UPDATE inscripciones_practicas_profesionales " +
                        "SET id_proyecto = ? " +
                        "WHERE matricula = ?";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_PROJECT);
            preparedStatement.setInt(1, idProject);
            preparedStatement.setString(2, studentNumber);
            isProjectAssigned = preparedStatement.executeUpdate() > NO_ROWS_AFFECTED;

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al asignar el proyecto al practicante");
        }

        return isProjectAssigned;
    }

}
