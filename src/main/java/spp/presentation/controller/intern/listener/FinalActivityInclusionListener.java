package spp.presentation.controller.intern.listener;

import spp.businesslogic.dto.ActivityDTO;
import spp.presentation.controller.intern.FinalReportGenerationController;
import spp.utils.view.table.DoubleClickListener;

public class FinalActivityInclusionListener implements DoubleClickListener<ActivityDTO> {

    private final FinalReportGenerationController controller;

    public FinalActivityInclusionListener(FinalReportGenerationController controller) {
        this.controller = controller;
    }

    @Override
    public void onItemSelected(ActivityDTO selectedItem) {
        if (selectedItem != null) {
            controller.includeActivity(selectedItem);
        }
    }
}