package spp.utils.view.datepicker;

import javafx.scene.control.DatePicker;

public class DatePickerConfigurator {

    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static void configureSmartDatePicker(DatePicker datePicker, DateValidationMode validationMode) {
        if (datePicker != null) {
            SafeDateStringConverter stringConverter = new SafeDateStringConverter(DEFAULT_DATE_FORMAT, validationMode);
            RestrictedDateCellFactory cellFactory = new RestrictedDateCellFactory(validationMode);

            datePicker.setConverter(stringConverter);
            datePicker.setDayCellFactory(cellFactory);

            datePicker.getEditor().focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    datePicker.setValue(datePicker.getConverter().fromString(datePicker.getEditor().getText()));
                }
            });
        }
    }
}