package spp.utils.view.datepicker;

import javafx.event.EventHandler;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyEvent;

public class DatePickerConfigurator implements EventHandler<KeyEvent> {

    private static final String ALLOWED_CHARACTERS = "0123456789/-_";
    private static final int MAX_LENGTH = 10;
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    public static void configureSmartDatePicker(DatePicker datePicker, DateValidationMode validationMode) {
        if (datePicker != null) {
            datePicker.setPromptText(DEFAULT_DATE_FORMAT);

            DatePickerConfigurator filterInstance = new DatePickerConfigurator();
            datePicker.addEventFilter(KeyEvent.KEY_TYPED, filterInstance);
        }
    }

    @Override
    public void handle(KeyEvent event) {
        String character = event.getCharacter();
        boolean isAllowedChar = ALLOWED_CHARACTERS.contains(character);

        if (!isAllowedChar) {
            event.consume();
        } else {
            Object source = event.getSource();
            boolean isDatePicker = source instanceof DatePicker;

            if (isDatePicker) {
                DatePicker dp = (DatePicker) source;
                javafx.scene.control.TextField editor = dp.getEditor();
                String currentText = editor.getText();

                if (currentText != null) {
                    int caretPosition = editor.getCaretPosition();
                    String textBeforeCaret = currentText.substring(0, caretPosition);
                    String textAfterCaret = currentText.substring(caretPosition);
                    String futureText = textBeforeCaret + character + textAfterCaret;
                    
                    int length = futureText.length();
                    boolean isTooLong = length > MAX_LENGTH;
                    
                    String consecutiveSeparatorsRegex = ".*[\\-/_]{2,}.*";
                    boolean hasConsecutiveSeparators = futureText.matches(consecutiveSeparatorsRegex);

                    if (isTooLong) {
                        event.consume();
                    } else if (hasConsecutiveSeparators) {
                        event.consume();
                    }
                }
            }
        }
    }
}