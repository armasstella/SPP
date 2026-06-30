package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InternDocumentDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;


public interface IInitialDocumentDAO {

    boolean saveDocument(String studentNumber, InternDocumentDTO internDocumentDTO) throws DAOException;
    boolean hasClassScheduleByInternEmail(String email) throws DAOException;
    boolean hasActivitiesPlanByInternEmail(String email) throws DAOException;
    boolean hasPSPByInternEmail(String email) throws DAOException;
    boolean hasPartialReportByInternEmail(String email) throws DAOException;
    boolean hasMonthlyReportByInternEmail(String email) throws DAOException;
    boolean hasSelfEvaluationByInternEmail(String email) throws DAOException;
    boolean hasEvaluationLinkedOrganizationByInternEmail(String email) throws DAOException;
    boolean assignGrade(int documentId, int grade, String comments) throws DAOException;
    List<InternDocumentDTO> getDocumentsByConcludedEnrollment(String email) throws DAOException;

}
