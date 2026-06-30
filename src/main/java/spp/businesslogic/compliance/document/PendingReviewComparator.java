package spp.businesslogic.compliance.document;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.dto.InternDocumentReviewDTO;

import java.util.Comparator;
import java.util.List;

public class PendingReviewComparator implements Comparator<InternDTO> {

    @Override
    public int compare(InternDTO firstIntern, InternDTO secondIntern) {
        int priorityResult = 0;

        boolean firstHasPending = hasPendingDocuments(firstIntern);
        boolean secondHasPending = hasPendingDocuments(secondIntern);

        if (firstHasPending && !secondHasPending) {
            priorityResult = -1;
        } else if (!firstHasPending && secondHasPending) {
            priorityResult = 1;
        }

        return priorityResult;
    }

    private boolean hasPendingDocuments(InternDTO intern) {
        boolean hasPending = false;
        List<InternDocumentReviewDTO> documents = intern.getDocuments();

        if (documents != null) {
            int index = 0;
            int totalDocuments = documents.size();

            while (index < totalDocuments && !hasPending) {
                InternDocumentReviewDTO currentDocument = documents.get(index);
                boolean isDocumentEvaluated = currentDocument.isGraded();

                if (!isDocumentEvaluated) {
                    hasPending = true;
                }

                index++;
            }
        }

        return hasPending;
    }
}