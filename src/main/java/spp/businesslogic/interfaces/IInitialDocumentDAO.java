package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IInitialDocumentDAO {

    boolean saveDocument(String studentNumber, InitialDocumentDTO initialDocumentDTO) throws DAOException;
    boolean hasClassScheduleByInternEmail(String email) throws DAOException;
    boolean hasActivitiesScheduleByInternEmail(String email) throws DAOException;
    boolean hasPSPByInternEmail(String email) throws DAOException;
    boolean hasPartialReportByInternEmail(String email) throws DAOException;
    boolean hasMonthlyReportByInternEmail(String email) throws DAOException;
    boolean hasSelfEvaluationByInternEmail(String email) throws DAOException;
    boolean hasEvaluationLinkedOrganizationByInternEmail(String email) throws DAOException;

}
