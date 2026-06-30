package spp.businesslogic.compliance.document;

import spp.businesslogic.dao.FinalReportDAO;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.exceptions.DAOException;

public class DocumentationRegistry {
    private final InternDocumentDAO internDocumentDAO = new InternDocumentDAO();
    private final FinalReportDAO finalReportDAO = new FinalReportDAO();

    public boolean isDocumentAlreadyUploaded(DocumentType type, String email) throws DAOException {
        switch (type) {
            case CLASS_SCHEDULE:
                return internDocumentDAO.hasClassScheduleByInternEmail(email);
            case PSP:
                return internDocumentDAO.hasPSPByInternEmail(email);
            case MONTHLY_REPORT:
                return internDocumentDAO.hasMonthlyReportByInternEmail(email);
            case PARTIAL_REPORT:
                return internDocumentDAO.hasPartialReportByInternEmail(email);
            case ACTIVITIES_PLAN:
                return internDocumentDAO.hasActivitiesPlanByInternEmail(email);
            case SELF_EVALUATION:
                return internDocumentDAO.hasSelfEvaluationByInternEmail(email);
            case EVALUATION_LINKED_ORGANIZATION:
                return internDocumentDAO.hasEvaluationLinkedOrganizationByInternEmail(email);
            case FINAL_REPORT:
                return finalReportDAO.hasFinalReportByInternEmail(email);
            case RELEASE_LETTER:
                return internDocumentDAO.hasReleaseLetterByInternEmail(email);
            default:
                return false;
        }
    }
}