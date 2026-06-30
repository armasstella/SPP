package spp.presentation.controller.intern.listener;

import spp.businesslogic.dto.DeliverableProductDTO;
import spp.presentation.controller.intern.FinalReportProductsController;
import spp.utils.view.table.DoubleClickListener;

public class DeliverableProductInclusionListener implements DoubleClickListener<DeliverableProductDTO> {

    private final FinalReportProductsController controller;

    public DeliverableProductInclusionListener(FinalReportProductsController controller) {
        this.controller = controller;
    }

    @Override
    public void onItemSelected(DeliverableProductDTO selectedItem) {
        if (selectedItem != null) {
            controller.includeDeliverableProduct(selectedItem);
        }
    }
}