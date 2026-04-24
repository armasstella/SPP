package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ProfessionalPracticeEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;

public interface IProfessionalPracticeEnrollmentDAO {
    boolean addProfessionalPracticeEnrollment(ProfessionalPracticeEnrollmentDTO professionalPracticeEnrollmentDTO)
            throws DAOException;
}
