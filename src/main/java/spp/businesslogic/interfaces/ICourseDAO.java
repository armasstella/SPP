package spp.businesslogic.interfaces;

import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

public interface ICourseDAO {

    List<CourseDTO> obtainAllActiveCourses() throws DAOException;
}