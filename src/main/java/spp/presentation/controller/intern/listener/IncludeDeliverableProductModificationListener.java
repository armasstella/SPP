package spp.presentation.controller.intern.listener;

import spp.businesslogic.dto.DeliverableProductDTO;
import spp.presentation.controller.intern.FinalReportProductsController;
import spp.utils.view.table.DoubleClickListener;

public class IncludeDeliverableProductModificationListener implements DoubleClickListener<DeliverableProductDTO> {

    private final FinalReportProductsController controller;

    public IncludeDeliverableProductModificationListener(FinalReportProductsController controller) {
        this.controller = controller;
    }

    @Override
    public void onItemSelected(DeliverableProductDTO selectedItem) {
        if (selectedItem != null) {
            controller.processProductModificationAction(selectedItem);
        }
    }
}