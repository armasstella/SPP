package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


public interface IInstructorDAO {

    boolean addInstructor(InstructorDTO instructorDTO) throws DAOException;
    int obtainId(String personalNumber) throws DAOException;
    boolean deactivateInstructor(InstructorDTO instructorDTO) throws DAOException;
    List<InstructorDTO> obtainAllActiveInstructors() throws DAOException;
    List<InstructorDTO> obtainActiveInstructorForComboBox() throws DAOException;

}
