package spp.presentation.controller.intern.listener;

import spp.businesslogic.dto.ActivityDTO;
import spp.presentation.controller.intern.MonthlyReportGenerationController;
import spp.utils.view.table.DoubleClickListener;

public class IncludedActivityModificationListener implements DoubleClickListener<ActivityDTO> {

    private final MonthlyReportGenerationController controller;

    public IncludedActivityModificationListener(MonthlyReportGenerationController controller) {
        this.controller = controller;
    }

    @Override
    public void onItemSelected(ActivityDTO selectedItem) {
        if (selectedItem != null) {
            controller.processActivityModificationAction(selectedItem);
        }
    }
}