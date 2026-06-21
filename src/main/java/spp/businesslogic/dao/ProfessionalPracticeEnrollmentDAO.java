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
    public boolean registerProfessionalPracticeEnrollment(ProfessionalPracticeEnrollmentDTO
        professionalPracticeEnrollmentDTO) throws DAOException {
        final String INSERT_PROFESSIONAL_PRACTICE_ENROLLMENT = "INSERT INTO inscripciones_practicas_profesionales " +
                "(id_experiencia_educativa, id_usuario_practicante, matricula, " +
                "calificacion_final, id_proyecto, horas_cubiertas) VALUES (?, ?, ?, ?, ?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROFESSIONAL_PRACTICE_ENROLLMENT)) {
                preparedStatement.setInt(1,
                        professionalPracticeEnrollmentDTO.getCourseDTO().getCourseCode());
                preparedStatement.setInt(2,
                        professionalPracticeEnrollmentDTO.getInternDTO().getId());
                preparedStatement.setString(3,
                        professionalPracticeEnrollmentDTO.getInternDTO().getStudentNumber());
                preparedStatement.setInt(4,
                        professionalPracticeEnrollmentDTO.getFinalGrade());
                preparedStatement.setInt(5,
                        professionalPracticeEnrollmentDTO.getProjectDTO().getId());
                preparedStatement.setInt(6,
                        professionalPracticeEnrollmentDTO.getCoveredHours());

                isInsertSuccessful = preparedStatement.executeUpdate() != NO_ROWS_AFFECTED;

            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DAOException("WARN: Violación de integridad de datos al insertar", e);

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al insertar inscripción", e);
        }

        return isInsertSuccessful;

    }

    @Override
    public boolean assignProjectByStudentNumber(String studentNumber, int idProject) throws DAOException {
        boolean isProjectAssigned = false;
        final String ASSIGN_PROJECT = "UPDATE inscripciones_practicas_profesionales ipp " +
                "INNER JOIN experiencias_educativas ee ON ipp.id_experiencia_educativa = ee.id_experiencia_educativa " +
                "INNER JOIN periodos p ON ee.id_periodo = p.id_periodo " +
                "SET ipp.id_proyecto = ? " +
                "WHERE ipp.matricula = ? AND p.periodoActual = 1";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_PROJECT)) {
                preparedStatement.setInt(1, idProject);
                preparedStatement.setString(2, studentNumber);
                isProjectAssigned = preparedStatement.executeUpdate() > NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al asignar el proyecto al practicante", e);
        }

        return isProjectAssigned;
    }

}
