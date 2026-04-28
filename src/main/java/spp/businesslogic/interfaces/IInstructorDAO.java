package spp.businesslogic.interfaces;

import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IInstructorDAO {
    boolean addInstructor(InstructorDTO instructorDTO) throws DAOException;
    boolean login(String personalNumber, String password) throws DAOException;
}
