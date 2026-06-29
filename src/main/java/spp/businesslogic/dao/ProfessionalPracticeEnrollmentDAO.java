package spp.businesslogic.dao;


import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProfessionalPracticeEnrollmentDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;


public class ProfessionalPracticeEnrollmentDAO implements IProfessionalPracticeEnrollmentDAO {

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

                isInsertSuccessful = preparedStatement.executeUpdate() != BaseDAO.NO_ROWS_AFFECTED;

            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(ExceptionLevel.WARN, e);
            throw new DAOException("Verifique los datos ingresados", e);

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al insertar inscripción", e);
        }

        return isInsertSuccessful;

    }

    @Override
    public boolean assignProjectByStudentNumber(String studentNumber, int idProject) throws DAOException {
        final String ASSIGN_PROJECT = "UPDATE inscripciones_practicas_profesionales ipp " +
                "INNER JOIN periodos p ON ipp.id_periodo = p.id_periodo " +
                "SET ipp.id_proyecto = ? " +
                "WHERE ipp.matricula = ? AND p.periodoActual = 1";
        boolean isProjectAssigned = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_PROJECT)) {
                preparedStatement.setInt(1, idProject);
                preparedStatement.setString(2, studentNumber);
                int rowsAffected = preparedStatement.executeUpdate();
                isProjectAssigned = rowsAffected != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al asignar el proyecto al practicante", e);
        }

        return isProjectAssigned;
    }

    @Override
    public boolean assignCourseByStudentNumber(String studentNumber, int courseId) throws DAOException {
        final String ASSIGN_COURSE = "UPDATE inscripciones_practicas_profesionales ipp " +
                "INNER JOIN periodos p ON ipp.id_periodo = p.id_periodo " +
                "SET ipp.id_experiencia_educativa = ? " +
                "WHERE ipp.matricula = ? AND p.periodoActual = 1";
        boolean isCourseAssigned = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_COURSE)) {
                preparedStatement.setInt(1, courseId);
                preparedStatement.setString(2, studentNumber);
                int rowsAffected = preparedStatement.executeUpdate();
                isCourseAssigned = rowsAffected != BaseDAO.NO_ROWS_AFFECTED;
            }

        } catch (SQLException e) {
            AppLogger.logError(ExceptionLevel.FATAL, e);
            throw new DAOException("wError de conexión al asignar la experiencia educativa al practicante", e);
        }

        return isCourseAssigned;
    }

}
