package spp.businesslogic.compliance.document;

import spp.businesslogic.dao.InternDAO;
import spp.businesslogic.dao.InternDocumentDAO;
import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.InternDocumentReviewDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.Collections;
import java.util.List;

public class InternDocumentManager {

    private final InternDAO internDAO = new InternDAO();
    private final InternDocumentDAO internDocumentDAO = new InternDocumentDAO();

    public List<InternDTO> getPrioritizedInternsForProfessor(String professorEmail) throws DAOException {
        List<InternDTO> assignedInterns = internDAO.getAssignedInternsByProfessorEmail(professorEmail);

        for (InternDTO internDTO : assignedInterns) {
            String studentNumber = internDTO.getStudentNumber();
            List<InternDocumentReviewDTO> internDocuments =
                    internDocumentDAO.findDocumentsWithEvaluationStatusByStudentNumber(studentNumber);

            internDTO.setDocuments(internDocuments);
        }

        PendingReviewComparator complianceComparator = new PendingReviewComparator();
        Collections.sort(assignedInterns, complianceComparator);

        return assignedInterns;
    }
}