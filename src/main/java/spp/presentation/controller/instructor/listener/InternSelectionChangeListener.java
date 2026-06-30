package spp.presentation.controller.instructor.listener;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import spp.businesslogic.dto.InternDTO;
import spp.presentation.controller.instructor.InternSelectorController;

public class InternSelectionChangeListener implements ChangeListener<InternDTO> {

    private final InternSelectorController controller;

    public InternSelectionChangeListener(InternSelectorController controller) {
        this.controller = controller;
    }

    @Override
    public void changed(ObservableValue<? extends InternDTO> observable, InternDTO oldValue, InternDTO newValue) {
        if (newValue != null) {
            controller.handleInternSelection(newValue);
        }
    }
}