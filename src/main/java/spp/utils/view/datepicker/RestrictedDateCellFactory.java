package spp.utils.view.datepicker;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

public class RestrictedDateCellFactory implements Callback<DatePicker, DateCell> {

    private final DateValidationMode validationMode;

    public RestrictedDateCellFactory(DateValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    @Override
    public DateCell call(DatePicker param) {
        return new RestrictedDateCell(this.validationMode);
    }
}