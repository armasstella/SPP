package spp.presentation.controller.intern.listener;

import spp.businesslogic.dto.ActivityDTO;
import spp.presentation.controller.intern.MonthlyReportGenerationController;
import spp.utils.view.table.DoubleClickListener;

public class MonthlyActivityInclusionListener implements DoubleClickListener<ActivityDTO> {

    private final MonthlyReportGenerationController controller;

    public MonthlyActivityInclusionListener(MonthlyReportGenerationController controller) {
        this.controller = controller;
    }

    @Override
    public void onItemSelected(ActivityDTO selectedItem) {
        if (selectedItem != null) {
            controller.includeActivity(selectedItem);
        }
    }
}