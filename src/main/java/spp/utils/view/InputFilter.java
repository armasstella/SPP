package spp.utils.view;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public final class InputFilter {

    public static final Pattern NAME_PATTERN = Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]*");
    public static final Pattern NUMERIC_PATTERN = Pattern.compile("\\d*");
    public static final Pattern EMAIL_CHARS_PATTERN = Pattern.compile("[a-zA-Z0-9@._\\-]*");
    public static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ\\s.,#\\-]*");
    public static final Pattern PASSWORD_PATTERN = Pattern.compile("[a-zA-Z0-9ñÑ.*%-]*");

    private InputFilter() {

    }

    public static void applyFilter(TextInputControl inputControl, Pattern regex, int maxLength) {
        inputControl.setTextFormatter(new TextFormatter<>(buildFilter(regex, maxLength)));

    }

    private static UnaryOperator<TextFormatter.Change> buildFilter(Pattern regex, int maxLength) {
        return change -> {
            String newText = change.getControlNewText();
            if (newText.length() <= maxLength && regex.matcher(newText).matches()) {
                return change;
            }
            return null;
        };

    }

}