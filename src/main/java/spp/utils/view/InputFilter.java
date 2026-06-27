package spp.utils.view;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import java.util.regex.Pattern;

public final class InputFilter {

    private InputFilter() {

    }

    public static void applyFormatFilter(TextInputControl inputField, Pattern allowedPattern, int maximumLength) {
        FormatFilter customFilter = new FormatFilter(allowedPattern, maximumLength);
        TextFormatter<String> textFormatter = new TextFormatter<>(customFilter);

        inputField.setTextFormatter(textFormatter);
    }

    public static boolean hasMinimumLength(TextInputControl inputField, int minimumLength) {
        boolean meetsMinimum;
        String currentText = inputField.getText();

        if (currentText != null) {
            int currentTextLength = currentText.trim().length();

            meetsMinimum = currentTextLength >= minimumLength;
        } else {
            meetsMinimum = false;
        }

        return meetsMinimum;
    }
}
