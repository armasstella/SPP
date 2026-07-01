package spp.utils.view.datepicker;

import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

public class CustomDayCellFactory implements Callback<DatePicker, DateCell> {

    private final DateValidationMode validationMode;

    public CustomDayCellFactory(DateValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    @Override
    public DateCell call(DatePicker datePicker) {
        CustomDateCell cell = new CustomDateCell(this.validationMode);
        return cell;
    }
}