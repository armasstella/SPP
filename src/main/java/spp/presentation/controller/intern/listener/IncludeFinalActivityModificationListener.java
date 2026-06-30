package spp.presentation.controller.intern.listener;

import spp.businesslogic.dto.ActivityDTO;
import spp.presentation.controller.intern.FinalReportActivitiesController;
import spp.utils.view.table.DoubleClickListener;

public class IncludeFinalActivityModificationListener implements DoubleClickListener<ActivityDTO> {

    private final FinalReportActivitiesController controller;

    public IncludeFinalActivityModificationListener(FinalReportActivitiesController controller) {
        this.controller = controller;
    }

    @Override
    public void onItemSelected(ActivityDTO selectedItem) {
        if (selectedItem != null) {
            controller.processActivityModificationAction(selectedItem);
        }
    }
}