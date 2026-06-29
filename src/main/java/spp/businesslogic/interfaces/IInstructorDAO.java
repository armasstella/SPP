package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IInstructorDAO {

    boolean registerInstructor(InstructorDTO instructorDTO) throws DAOException;
    boolean deactivateInstructor(InstructorDTO instructorDTO) throws DAOException;
    List<InstructorDTO> getActiveInstructors() throws DAOException;
    List<InstructorDTO> getActiveInstructorsIdentifiers() throws DAOException;
    String findActivePersonalNumberByEmail(String email) throws DAOException;
    boolean hasInstructorCourseAssigned(int instructorId) throws DAOException;

}
