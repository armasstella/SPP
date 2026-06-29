package spp.utils.view.inputdata;

import javafx.scene.control.TextFormatter;
import java.util.function.UnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormatFilter implements UnaryOperator<TextFormatter.Change> {

    private final Pattern allowedPattern;
    private final int maximumLength;

    public FormatFilter(Pattern allowedPattern, int maximumLength) {
        this.allowedPattern = allowedPattern;
        this.maximumLength = maximumLength;
    }

    @Override
    public TextFormatter.Change apply(TextFormatter.Change textChange) {
        TextFormatter.Change validatedChange;

        String proposedText = textChange.getControlNewText();
        int proposedTextLength = proposedText.length();

        Matcher patternMatcher = this.allowedPattern.matcher(proposedText);
        boolean matchesPattern = patternMatcher.matches();
        boolean isWithinMaximumLength = proposedTextLength <= this.maximumLength;

        if (matchesPattern && isWithinMaximumLength) {
            validatedChange = textChange;
        } else {
            validatedChange = null;
        }

        return validatedChange;

    }
}
