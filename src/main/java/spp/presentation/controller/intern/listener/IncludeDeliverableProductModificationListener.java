package spp.presentation.controller.intern.listener;

import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.dto.DeliverableProductDTO;
import spp.presentation.controller.intern.FinalReportGenerationController;
import spp.presentation.controller.intern.MonthlyReportGenerationController;
import spp.utils.view.table.DoubleClickListener;

public class IncludeDeliverableProductModificationListener implements DoubleClickListener<DeliverableProductDTO> {

    private final FinalReportGenerationController controller;

    public IncludeDeliverableProductModificationListener(FinalReportGenerationController controller) {
        this.controller = controller;
    }

    @Override
    public void onItemSelected(DeliverableProductDTO selectedItem) {
        if (selectedItem != null) {
            controller.processProductModificationAction(selectedItem);
        }
    }
}