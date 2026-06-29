package spp.businesslogic.dao;


import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.dto.TermDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ICourseDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.dataaccess.connection.MySQLConnectionManager;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


public class CourseDAO implements ICourseDAO {

    public CourseDAO() {

    }

    @Override
    public boolean registerCourse(CourseDTO courseDTO, int activeTermId) throws DAOException {
        final String INSERT_COURSE = "INSERT INTO experiencias_educativas(nrc, bloque, seccion, " +
                "cupo, detalles, id_usuario_profesor, num_personal, id_periodo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            try(PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURSE)) {
                preparedStatement.setInt(1, courseDTO.getCourseCode());
                preparedStatement.setInt(2, courseDTO.getSchoolBlock());
                preparedStatement.setInt(3, courseDTO.getSection());
                preparedStatement.setInt(4, courseDTO.getCapacity());
                preparedStatement.setString(5, courseDTO.getCourseDetails());
                if (courseDTO.getInstructorDTO() != null) {
                    preparedStatement.setInt(6, courseDTO.getInstructorDTO().getId());
                    preparedStatement.setString(7, courseDTO.getInstructorDTO().getPersonalNumber());
                } else {
                    preparedStatement.setNull(6, java.sql.Types.INTEGER);
                    preparedStatement.setNull(7, java.sql.Types.VARCHAR);
                }
                preparedStatement.setInt(8, activeTermId);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows != DAOResultConstant.NO_ROWS_AFFECTED) {
                    isInsertSuccessful = true;
                }

            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No puede haber dos EE con el mismo NRC para este periodo", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al insertar curso", e);
        } finally {
            MySQLConnectionManager.getInstance().enableAutoCommitConnection();
        }

        return isInsertSuccessful;
    }

    @Override
    public boolean existsRegisteredCourses() throws DAOException {
        boolean coursesExist = false;

        final String SEARCH_COURSES = "SELECT f_hay_experiencias_periodo_activo()";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_COURSES)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        coursesExist = resultSet.getBoolean(1);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al buscar cursos", e);
        }

        return coursesExist;

    }

    @Override
    public List<CourseDTO> getActiveCoursesStatistics() throws DAOException {
        final String SELECT_ALL_COURSES = "SELECT * FROM view_detalle_cursos_activos";
        List<CourseDTO> coursesList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COURSES)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        coursesList.add(buildCourseDTOFromResultSet(resultSet));
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al obtener cursos", e);
        }

        return coursesList;

    }

    private static CourseDTO buildCourseDTOFromResultSet(ResultSet resultSet) throws SQLException {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setIdCourse(resultSet.getInt("id_experiencia_educativa"));
        courseDTO.setCourseCode(Integer.parseInt(resultSet.getString("nrc")));
        TermDTO termDTO = new TermDTO();
        termDTO.setName(resultSet.getString("periodo"));
        courseDTO.setTermDTO(termDTO);
        courseDTO.setSchoolBlock(resultSet.getInt("bloque"));
        courseDTO.setSection(resultSet.getInt("seccion"));
        InstructorDTO instructorDTO = new InstructorDTO();
        instructorDTO.setFirstName(resultSet.getString("nombreProfesor"));
        courseDTO.setInstructorDTO(instructorDTO);
        courseDTO.setNumberOfInterns(resultSet.getInt("cantidadPracticantes"));
        return courseDTO;
    }

    @Override
    public boolean assignInstructorToCourse(CourseDTO courseDTO) throws DAOException {
        final String UPDATE_COURSE_INSTRUCTOR = "UPDATE experiencias_educativas " +
                "SET id_usuario_profesor = ?, num_personal = ? WHERE id_experiencia_educativa = ?";
        boolean isUpdateSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();

            try (PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COURSE_INSTRUCTOR)) {
                preparedStatement.setInt(1, courseDTO.getInstructorDTO().getId());
                preparedStatement.setString(2, courseDTO.getInstructorDTO().getPersonalNumber());
                preparedStatement.setInt(3, courseDTO.getIdCourse());

                isUpdateSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;

            }

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al asignar profesor", e);
        }

        return isUpdateSuccessful;
    }

    @Override
    public List<CourseDTO> getCourseCodesForActiveTerm() throws DAOException {
        final String SELECT_COURSE_CODES = "SELECT ee.id_experiencia_educativa AS 'id_ee', ee.nrc FROM " +
                "experiencias_educativas ee INNER JOIN periodos p WHERE ee.id_periodo = p.id_periodo " +
                "AND p.periodoActual = 1;";
        List<CourseDTO> coursesList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try(PreparedStatement preparedStatement = connection.prepareStatement(SELECT_COURSE_CODES)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        CourseDTO course = new CourseDTO();
                        course.setIdCourse(resultSet.getInt("id_ee"));
                        course.setCourseCode(resultSet.getInt("nrc"));
                        coursesList.add(course);
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al buscar nrc de cursos", e);
        }

        return coursesList;

    }

}