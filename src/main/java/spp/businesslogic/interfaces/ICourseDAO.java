package spp.businesslogic.interfaces;

import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface ICourseDAO {

    boolean searchCourses() throws DAOException;
    List<CourseDTO> obtainAllActiveCourses() throws DAOException;
}
