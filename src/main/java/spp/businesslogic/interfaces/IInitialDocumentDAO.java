package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IInitialDocumentDAO {

    boolean saveDocument(String email, InitialDocumentDTO initialDocumentDTO) throws DAOException;
    boolean searchClassScheduleForIntern(String email) throws DAOException;
    boolean searchActivitiesScheduleForIntern(String email) throws DAOException;
    boolean searchPSPForIntern(String email) throws DAOException;
    boolean searchPartialReportForIntern(String email) throws DAOException;
    boolean searchMonthlyReportForIntern(String email) throws DAOException;
    boolean searchSelfEvaluationForIntern(String email) throws DAOException;
    boolean searchEvaluationLinkedOrganizationForIntern(String email) throws DAOException;
    boolean searchFinalReportForIntern(String email) throws DAOException;

}
