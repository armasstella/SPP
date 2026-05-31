package spp.utils.view;


import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import java.util.function.UnaryOperator;


public final class InputFilter {

    public static final String TEXT_PATTERN = "[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ¿?¡!'\"()/$#=%+\\-\\[\\]{}.,_ ]*";
    public static final String NUMERIC_PATTERN = "\\d*";

    private InputFilter() {

    }

    public static void applyFilter(TextInputControl inputControl, String regex, int maxLength) {
        inputControl.setTextFormatter(new TextFormatter<>(buildFilter(regex, maxLength)));

    }

    private static UnaryOperator<TextFormatter.Change> buildFilter(String regex, int maxLength) {
        return change -> {
            TextFormatter.Change result = null;
            String newText = change.getControlNewText();
            if (newText.length() <= maxLength && newText.matches(regex)) {
                result = change;
            }
            return result;
        };

    }

}