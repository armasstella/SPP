package spp.businesslogic.interfaces;

import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IInstructorDAO {
    void addInstructor(InstructorDTO instructorDTO) throws DAOException;
}
