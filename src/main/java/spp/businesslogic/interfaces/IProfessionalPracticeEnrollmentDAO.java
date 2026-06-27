package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IProfessionalPracticeEnrollmentDAO {

    boolean registerProfessionalPracticeEnrollment(ProfessionalPracticeEnrollmentDTO professionalPracticeEnrollmentDTO)
            throws DAOException;
    boolean assignProjectByStudentNumber(String studentNumber, int idProject) throws DAOException;
    boolean assignCourseByStudentNumber(String studentNumber, int courseId) throws DAOException;

}
