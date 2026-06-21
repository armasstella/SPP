package spp.businesslogic.interfaces;


import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface ICourseDAO {

    boolean registerCourse(CourseDTO courseDTO) throws DAOException;
    boolean existsRegisteredCourses() throws DAOException;
    List<CourseDTO> getActiveCoursesStatistics() throws DAOException;
    boolean assignInstructorToCourse(CourseDTO courseDTO) throws DAOException;
    List<CourseDTO> getCourseCodesForActiveTerm() throws DAOException;

}