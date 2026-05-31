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
import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements ICourseDAO {

    public CourseDAO() {

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
                        throw new DAOException("No se encontraron experiencias educativas registradas");
                    }
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error de conexión al buscar cursos");
        }

        return isSearchSuccessful;

    }

    @Override
    public List<CourseDTO> obtainAllActiveCourses() throws DAOException {
        List<CourseDTO> coursesList = new ArrayList<>();
        final String SELECT_ALL_COURSES = "SELECT " +
                "ee.id_experiencia_educativa, " +
                "ee.nrc, " +
                "ee.periodo, " +
                "ee.bloque, " +
                "ee.seccion, " +
                "CONCAT(u_prof.nombre, ' ', u_prof.apellidos) AS nombreProfesor, " +
                "COUNT(ipp.id_usuario_practicante) AS cantidadPracticantes " +
                "FROM ExperienciasEducativas ee " +
                "INNER JOIN usuarios u_prof " +
                "    ON ee.id_usuario_profesor = u_prof.id_usuario " +
                "LEFT JOIN inscripciones_practicas_profesionales ipp " +
                "    ON ee.id_experiencia_educativa = ipp.id_experiencia_educativa " +
                "GROUP BY " +
                "    ee.id_experiencia_educativa, " +
                "    ee.nrc, " +
                "    ee.periodo, " +
                "    ee.bloque, " +
                "    ee.seccion, " +
                "    u_prof.nombre, " +
                "    u_prof.apellidos";

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
                courseDTO.setInstructor(instructorDTO);
                courseDTO.setNumberOfInterns(resultSet.getInt("cantidadPracticantes"));
                coursesList.add(courseDTO);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al obtener lista de cursos", e);
        }
        return coursesList;
    }
}