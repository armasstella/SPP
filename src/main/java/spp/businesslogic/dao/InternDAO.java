package spp.businesslogic.dao;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.enums.DocumentationPhase;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IInternDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.dataaccess.connection.MySQLConnectionManager;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;
import java.sql.SQLTransactionRollbackException;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InternDAO implements IInternDAO {

    public InternDAO() {
    }

    @Override
    public boolean registerIntern(InternDTO internDTO) throws DAOException {
        final String INSERT_INTERN = "INSERT INTO Practicantes " +
                "(id_usuario, matricula, sexo, habla_lengua_indigena, detalle_lengua_indigena, fecha_nacimiento) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            MySQLConnectionManager.getInstance().disableAutoCommitConnection();

            UserDAO userDAO = new UserDAO();
            int generatedId = userDAO.registerUser(internDTO);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTERN)) {
                preparedStatement.setInt(1, generatedId);
                preparedStatement.setString(2, internDTO.getStudentNumber());
                preparedStatement.setString(3, internDTO.getSex());
                preparedStatement.setBoolean(4, internDTO.getSpeaksIndigenousLanguage());
                preparedStatement.setString(5, internDTO.getIndigenousLanguage());
                preparedStatement.setTimestamp(6, Timestamp.valueOf(internDTO.getBirthDate()));

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected != DAOResultConstant.NO_ROWS_AFFECTED) {
                    isInsertSuccessful = true;
                    connection.commit();
                }
            }
        } catch (DAOException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al insertar practicante: " + e.getMessage());

        } catch (SQLIntegrityConstraintViolationException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("El practicante ya se encuentra activo en el sistema.", e);

        } catch (SQLTransactionRollbackException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de concurrencia: la operación fue abortada. Intente de nuevo.", e);

        } catch (SQLTimeoutException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al registrar practicante.", e);

        } catch (SQLException e) {
            MySQLConnectionManager.getInstance().rollbackSafe();
            AppLogger.log(ExceptionLevel.FATAL, e);
            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al registrar al practicante.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error al intentar registrar al practicante.", e);
            }
        } finally {
            MySQLConnectionManager.getInstance().enableAutoCommitConnection();
        }

        return isInsertSuccessful;
    }

    @Override
    public boolean existsStudentByStudentNumber(String studentNumber) throws DAOException {
        final String SEARCH_STUDENT = "SELECT f_existe_estudiante(?)";
        boolean studentExists = false;
        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_STUDENT)) {
                preparedStatement.setString(1, studentNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        studentExists = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al verificar matrícula.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al consultar la existencia de la matrícula.", e);
        }
        return studentExists;
    }

    @Override
    public List<InternDTO> getActiveInterns() throws DAOException {
        final String SELECT_ALL_INTERNS = "SELECT p.matricula, CONCAT(u.nombre, ' ', u.apellidos) " +
                "AS 'nombre_completo', u.correo_electronico " +
                "FROM Usuarios u INNER JOIN Practicantes p on u.id_usuario = p.id_usuario AND u.estado = 'Activo' " +
                "ORDER BY p.matricula";
        List<InternDTO> internsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_INTERNS);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    InternDTO internDTO = new InternDTO();
                    internDTO.setStudentNumber(resultSet.getString("matricula"));
                    internDTO.setFullName(resultSet.getString("nombre_completo"));
                    internDTO.setEmail(resultSet.getString("correo_electronico"));
                    internsList.add(internDTO);
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al obtener practicantes.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al obtener la lista de practicantes activos.", e);
        }
        return internsList;
    }

    @Override
    public String findActiveStudentNumberByEmail(String email) throws DAOException {
        final String SELECT_STUDENT_NUMBER = "SELECT matricula FROM practicantes p INNER JOIN usuarios u " +
                "WHERE p.id_usuario = u.id_usuario AND u.estado = 'Activo' AND u.correo_electronico = ?";
        String studentNumber = null;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_STUDENT_NUMBER)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        studentNumber = resultSet.getString(1);
                    }
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar matrícula.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al obtener la matrícula", e);
        }
        return studentNumber;
    }

    @Override
    public boolean deactivateIntern(InternDTO internDTO) throws DAOException {
        final String INACTIVATE_INTERN = "UPDATE Usuarios " +
                "INNER JOIN Practicantes ON Usuarios.id_usuario = Practicantes.id_usuario " +
                "SET Usuarios.estado = 'Inactivo' " +
                "WHERE Practicantes.matricula = ?";
        boolean isDeactivationSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INACTIVATE_INTERN)) {
                preparedStatement.setString(1, internDTO.getStudentNumber());
                isDeactivationSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }
        } catch (SQLTransactionRollbackException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de concurrencia al desactivar practicante.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al desactivar practicante.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al desactivar practicante", e);
        }
        return isDeactivationSuccessful;
    }

    @Override
    public List<InternDTO> findUnassignedInternsIdentifiers() throws DAOException {
        final String SELECT_INTERNS_WITHOUT_PROJECT = "SELECT p.matricula, CONCAT(u.nombre, ' ', u.apellidos) AS " +
                "'nombre_completo' " +
                "FROM practicantes p " +
                "INNER JOIN usuarios u ON p.id_usuario = u.id_usuario " +
                "INNER JOIN inscripciones_practicas_profesionales i ON i.id_usuario_practicante = p.id_usuario AND " +
                "i.matricula = p.matricula " +
                "WHERE i.id_proyecto IS NULL";
        List<InternDTO> internList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INTERNS_WITHOUT_PROJECT);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    InternDTO internDTO = new InternDTO();
                    internDTO.setStudentNumber(resultSet.getString("matricula"));
                    internDTO.setFullName(resultSet.getString("nombre_completo"));
                    internList.add(internDTO);
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar practicantes sin proyecto.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al obtener practicantes sin proyecto asignado", e);
        }
        return internList;
    }

    @Override
    public List<InternDTO> getAssignedInternsByProfessorEmail(String email) throws DAOException {
        final String SELECT_INTERNS_BY_PROFESSOR_EMAIL = "SELECT DISTINCT p.matricula, " +
                "CONCAT(ua.nombre, ' ', ua.apellidos) AS nombre_completo " +
                "FROM practicantes p " +
                "INNER JOIN usuarios ua ON p.id_usuario = ua.id_usuario " +
                "INNER JOIN inscripciones_practicas_profesionales i ON p.matricula = i.matricula " +
                "INNER JOIN experiencias_educativas ee ON i.id_experiencia_educativa = ee.id_experiencia_educativa " +
                "INNER JOIN usuarios u_profesor ON ee.id_usuario_profesor = u_profesor.id_usuario " +
                "WHERE u_profesor.correo_electronico = ?";
        List<InternDTO> internsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_INTERNS_BY_PROFESSOR_EMAIL)) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        InternDTO intern = new InternDTO();
                        intern.setStudentNumber(resultSet.getString("matricula"));
                        intern.setFullName(resultSet.getString("nombre_completo"));
                        internsList.add(intern);
                    }
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al buscar practicantes del profesor.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al buscar los practicantes asignados al profesor", e);
        }
        return internsList;
    }

    public DocumentationPhase findCurrentPhaseById(int internId) throws DAOException {
        String CHECK_PHASE = "SELECT f_obtener_fase_documental_practicante(?) as 'fase'";
        DocumentationPhase currentPhase = null;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_PHASE)) {
                preparedStatement.setInt(1, internId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String phaseValue = resultSet.getString("fase");
                        if (phaseValue != null) {
                            currentPhase = DocumentationPhase.valueOf(phaseValue);
                        }
                    }
                }
            }

        } catch (IllegalArgumentException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error con datos al obtener la fase de documentación.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error al consultar la fase documental del practicante.", e);
        }

        return currentPhase;
    }

}