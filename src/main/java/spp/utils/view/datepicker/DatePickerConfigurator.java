package spp.utils.view.datepicker;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class DatePickerConfigurator {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static void configureSmartDatePicker(DatePicker datePicker, DateValidationMode validationMode) {
        if (datePicker != null) {
            datePicker.setEditable(false);
            TextField editor = datePicker.getEditor();
            editor.setEditable(false);
            datePicker.setPromptText(DEFAULT_DATE_FORMAT);
            CustomDayCellFactory factory = new CustomDayCellFactory(validationMode);
            datePicker.setDayCellFactory(factory);
        }
    }
}