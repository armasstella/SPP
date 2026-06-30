package spp.businesslogic.dao;


import spp.businesslogic.dto.InternEnrollmentConcludeDTO;
import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IProfessionalPracticeEnrollmentDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.sql.SQLTimeoutException;

public class ProfessionalPracticeEnrollmentDAO implements IProfessionalPracticeEnrollmentDAO {

    public ProfessionalPracticeEnrollmentDAO() {
    }

    @Override
    public boolean registerProfessionalPracticeEnrollment(ProfessionalPracticeEnrollmentDTO professionalPracticeEnrollmentDTO) throws DAOException {
        final String INSERT_PROFESSIONAL_PRACTICE_ENROLLMENT = "INSERT INTO inscripciones_practicas_profesionales " +
                "(id_experiencia_educativa, id_usuario_practicante, matricula, " +
                "calificacion_final, id_proyecto, horas_cubiertas) VALUES (?, ?, ?, ?, ?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PROFESSIONAL_PRACTICE_ENROLLMENT)) {
                preparedStatement.setInt(1, professionalPracticeEnrollmentDTO.getCourseDTO().getCourseCode());
                preparedStatement.setInt(2, professionalPracticeEnrollmentDTO.getInternDTO().getId());
                preparedStatement.setString(3, professionalPracticeEnrollmentDTO.getInternDTO().getStudentNumber());
                preparedStatement.setInt(4, professionalPracticeEnrollmentDTO.getFinalGrade());
                preparedStatement.setInt(5, professionalPracticeEnrollmentDTO.getProjectDTO().getId());
                preparedStatement.setInt(6, professionalPracticeEnrollmentDTO.getCoveredHours());

                isInsertSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;

            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo registrar la inscripción. Verifique que los datos (NRC, matrícula, proyecto) sean correctos y no estén duplicados.");

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al registrar la inscripción.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al registrar la inscripción.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al registrar la inscripción.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error interno al intentar registrar la inscripción.");
            }
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
            try (PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_PROJECT)) {
                preparedStatement.setInt(1, idProject);
                preparedStatement.setString(2, studentNumber);
                int rowsAffected = preparedStatement.executeUpdate();
                isProjectAssigned = rowsAffected != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al asignar el proyecto.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al asignar el proyecto.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al asignar el proyecto.");
            } else {
                throw new DAOException("Ocurrió un error interno al intentar asignar el proyecto al practicante.");
            }
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
            try (PreparedStatement preparedStatement = connection.prepareStatement(ASSIGN_COURSE)) {
                preparedStatement.setInt(1, courseId);
                preparedStatement.setString(2, studentNumber);
                int rowsAffected = preparedStatement.executeUpdate();
                isCourseAssigned = rowsAffected != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLInvalidAuthorizationSpecException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de comunicación con el servidor al asignar la experiencia educativa.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al asignar la experiencia educativa.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al asignar la experiencia educativa.");
            } else {
                throw new DAOException("Ocurrió un error interno al intentar asignar la experiencia educativa al practicante.");
            }
        }

        return isCourseAssigned;
    }

    @Override
    public boolean isPracticeCompletedByInternEmail(String email) throws DAOException {
        final String CHECK_COMPLETED = "SELECT f_estudiante_concluyo_periodo_activo(?)";
        boolean isCompleted = false;

        try (Connection connection = MySQLConnection.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_COMPLETED)) {

            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isCompleted = resultSet.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al verificar si la práctica está concluida.", e);
        }
        return isCompleted;
    }

    public InternEnrollmentConcludeDTO getEnrollmentConcludeDatayByInternEmail(String email) throws DAOException {
        String query = "SELECT * FROM view_resumen_inscripcion_concluida WHERE correo_electronico = ?";
        InternEnrollmentConcludeDTO enrollmentConcludeDTO = null;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        enrollmentConcludeDTO = new InternEnrollmentConcludeDTO();

                        enrollmentConcludeDTO.setStudentEmail(resultSet.getString("correo_electronico"));
                        enrollmentConcludeDTO.setStudentName(resultSet.getString("nombre_alumno"));
                        enrollmentConcludeDTO.setStudentNumber(resultSet.getString("matricula"));
                        enrollmentConcludeDTO.setProjectName(resultSet.getString("nombre_proyecto"));
                        enrollmentConcludeDTO.setCompanyName(resultSet.getString("nombre_empresa"));
                        enrollmentConcludeDTO.setInstructorName(resultSet.getString("nombre_profesor"));

                        int grade = resultSet.getInt("calificacion_final");
                        if (!resultSet.wasNull()) {
                            enrollmentConcludeDTO.setFinalGrade(grade);
                        } else {
                            enrollmentConcludeDTO.setFinalGrade(null);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al obtener resumen de inscripción concluida ", e);
        }

        return enrollmentConcludeDTO;
    }

}