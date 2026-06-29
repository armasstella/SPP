package spp.businesslogic.compliance.document;

import spp.businesslogic.dao.FinalReportDAO;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;

public class DocumentationRegistry {
    private final InternDocumentDAO initialDAO = new InternDocumentDAO();
    private final FinalReportDAO finalDAO = new FinalReportDAO();

    public boolean isDocumentAlreadyUploaded(DocumentType type, String email) throws DAOException {
        switch (type) {
            case CLASS_SCHEDULE:
                return initialDAO.hasClassScheduleByInternEmail(email);
            case PSP:
                return initialDAO.hasPSPByInternEmail(email);
            case MONTHLY_REPORT:
                return initialDAO.hasMonthlyReportByInternEmail(email);
            case PARTIAL_REPORT:
                return initialDAO.hasPartialReportByInternEmail(email);
            case ACTIVITIES_PLAN:
                return initialDAO.hasActivitiesPlanByInternEmail(email);
            case SELF_EVALUATION:
                return initialDAO.hasSelfEvaluationByInternEmail(email);
            case EVALUATION_LINKED_ORGANIZATION:
                return initialDAO.hasEvaluationLinkedOrganizationByInternEmail(email);
            case FINAL_REPORT:
                return finalDAO.hasFinalReportByInternEmail(email);
            default:
                return false;
        }
    }
}