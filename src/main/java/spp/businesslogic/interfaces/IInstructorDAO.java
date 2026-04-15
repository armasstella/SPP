package spp.businesslogic.interfaces;

import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.InstructorException;

import java.sql.Connection;
import java.sql.SQLException;

public interface IInstructorDAO {
    void addInstructor(InstructorDTO instructorDTO) throws InstructorException;
    void insertInstructor(InstructorDTO instructorDTO, int userId) throws SQLException;
}
