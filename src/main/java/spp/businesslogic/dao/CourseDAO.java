package spp.businesslogic.dao;


import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ICourseDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


public class CourseDAO implements ICourseDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    public CourseDAO() {

    }

    @Override
    public boolean addCourse(CourseDTO courseDTO) throws DAOException {
        final String INSERT_COURSE = "INSERT INTO experiencias_educativas(nrc, bloque, seccion, periodo, " +
                "cupo, detalles, id_usuario_profesor, num_personal) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean isAddSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_COURSE);
                preparedStatement.setInt(1, courseDTO.getCourseCode());
                preparedStatement.setInt(2, courseDTO.getSection());
                preparedStatement.setInt(3, courseDTO.getSchoolBlock());
                preparedStatement.setString(4, courseDTO.getTerm());
                preparedStatement.setInt(5, courseDTO.getCapacity());
                preparedStatement.setString(6, courseDTO.getCourseDetails());
                if (courseDTO.getInstructorDTO() != null) {
                    preparedStatement.setInt(7, courseDTO.getInstructorDTO().getId());
                    preparedStatement.setString(8, courseDTO.getInstructorDTO().getPersonalNumber());
                } else {
                    preparedStatement.setNull(7, java.sql.Types.INTEGER);
                    preparedStatement.setNull(8, java.sql.Types.VARCHAR);
                }

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("WARN: Fallo al insertar curso. No se afectaron filas");
                }

                connection.commit();
                isAddSuccesful = true;

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("WARN: Violación a integridad de datos al insertar", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error general al insertar curso", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al insertar curso", e);
        }

        return isAddSuccesful;
    }

    @Override
    public boolean searchCourses() throws DAOException {
        boolean isSearchSuccessful = false;

        final String SEARCH_COURSES = "SELECT f_hay_experiencias_educativas()";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_COURSES);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccessful = resultSet.getBoolean(1);
                    if (!isSearchSuccessful) {
                        throw new DAOException("WARN: No se encontraron registros");
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar cursos");
        }

        return isSearchSuccessful;

    }

    @Override
    public List<CourseDTO> obtainAllActiveCourses() throws DAOException {
        List<CourseDTO> coursesList = new ArrayList<>();
        final String SELECT_ALL_COURSES = "SELECT " +
                " ee.id_experiencia_educativa, " +
                " ee.nrc, " +
                " ee.periodo, " +
                " ee.bloque, " +
                " ee.seccion, " +
                " COALESCE(CONCAT(u_prof.nombre, ' ', u_prof.apellidos), 'Sin profesor asignado') AS nombreProfesor, " +
                " COUNT(ipp.id_usuario_practicante) AS cantidadPracticantes " +
                "FROM experiencias_educativas ee " +
                "LEFT JOIN usuarios u_prof " +
                "    ON ee.id_usuario_profesor = u_prof.id_usuario " +
                "LEFT JOIN inscripciones_practicas_profesionales ipp " +
                "    ON ee.id_experiencia_educativa = ipp.id_experiencia_educativa " +
                "GROUP BY " +
                " ee.id_experiencia_educativa, " +
                " ee.nrc, " +
                " ee.periodo, " +
                " ee.bloque, " +
                " ee.seccion, " +
                " u_prof.nombre, " +
                " u_prof.apellidos";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_COURSES);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setIdCourse(resultSet.getInt("id_experiencia_educativa"));
                courseDTO.setCourseCode(Integer.parseInt(resultSet.getString("nrc")));
                courseDTO.setTerm(resultSet.getString("periodo"));
                courseDTO.setSchoolBlock(resultSet.getInt("bloque"));
                courseDTO.setSection(resultSet.getInt("seccion"));
                InstructorDTO instructorDTO = new InstructorDTO();
                instructorDTO.setFirstName(resultSet.getString("nombreProfesor"));
                courseDTO.setInstructorDTO(instructorDTO);
                courseDTO.setNumberOfInterns(resultSet.getInt("cantidadPracticantes"));
                coursesList.add(courseDTO);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al obtener cursos", e);
        }

        return coursesList;

    }

    @Override
    public boolean assignInstructorToCourse(CourseDTO courseDTO) throws DAOException {
        final String UPDATE_COURSE_INSTRUCTOR =
                "UPDATE experiencias_educativas SET id_usuario_profesor = ?, num_personal = ? WHERE id_experiencia_educativa = ?";
        boolean isUpdateSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_COURSE_INSTRUCTOR);
            preparedStatement.setInt(1, courseDTO.getInstructorDTO().getId());
            preparedStatement.setString(2, courseDTO.getInstructorDTO().getPersonalNumber());
            preparedStatement.setInt(3, courseDTO.getIdCourse());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == NO_ROWS_AFFECTED) {
                throw new DAOException("WARN: Fallo al asignar profesor. No se afectaron filas");
            }

            isUpdateSuccessful = true;

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al asignar profesor", e);
        }

        return isUpdateSuccessful;
    }

}