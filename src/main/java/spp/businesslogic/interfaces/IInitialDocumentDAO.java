package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InitialDocumentDTO;
import spp.businesslogic.exceptions.DAOException;


public interface IInitialDocumentDAO {

    boolean saveDocument(String studentNumber, InitialDocumentDTO initialDocumentDTO) throws DAOException;
    boolean searchClassScheduleForIntern(String studentNumber) throws DAOException;
    boolean searchActivitiesScheduleForIntern(String studentNumber) throws DAOException;
    boolean searchPSPForIntern(String studentNumber) throws DAOException;
    boolean searchPartialReportForIntern(String studentNumber) throws DAOException;
    boolean searchSelfEvaluationForIntern(String studentNumber) throws DAOException;
    boolean searchEvaluationLinkedOrganizationForIntern(String studentNumber) throws DAOException;
    boolean searchFinalReportForIntern(String studentNumber) throws DAOException;

}
