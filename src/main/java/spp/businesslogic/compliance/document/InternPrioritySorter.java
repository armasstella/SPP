package spp.businesslogic.compliance.document;

import spp.businesslogic.dto.InternDTO;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InternPrioritySorter {

    public List<InternDTO> sortByPendingReviews(List<InternDTO> rawInternList) {
        List<InternDTO> sortedList = new ArrayList<>(rawInternList);

        PendingReviewComparator priorityComparator = new PendingReviewComparator();
        Collections.sort(sortedList, priorityComparator);

        return sortedList;
    }
}