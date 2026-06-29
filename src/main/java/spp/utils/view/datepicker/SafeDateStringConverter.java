package spp.utils.view.datepicker;

import javafx.util.StringConverter;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class SafeDateStringConverter extends StringConverter<LocalDate> {

    private final DateTimeFormatter dateFormatter;
    private final DateValidationMode validationMode;
    private static final int LEGAL_AGE_YEARS = 18;

    public SafeDateStringConverter(String pattern, DateValidationMode validationMode) {
        this.dateFormatter = DateTimeFormatter.ofPattern(pattern);
        this.validationMode = validationMode;
    }

    @Override
    public String toString(LocalDate date) {
        String formattedDate = "";

        if (date != null) {
            formattedDate = dateFormatter.format(date);
        }

        return formattedDate;
    }

    @Override
    public LocalDate fromString(String text) {
        LocalDate parsedDate = null;

        if (text != null && !text.trim().isEmpty()) {
            try {
                LocalDate tempDate = LocalDate.parse(text, dateFormatter);
                if (isValidAccordingToMode(tempDate)) {
                    parsedDate = tempDate;
                }
            } catch (DateTimeParseException e) {
                AppLogger.log(ExceptionLevel.WARN, e);
            }
        }

        return parsedDate;
    }

    private boolean isValidAccordingToMode(LocalDate date) {
        boolean isDateValid = false;

        if (validationMode == DateValidationMode.ANY_DATE) {
            isDateValid = true;
        } else if (validationMode == DateValidationMode.LEGAL_AGE_BIRTHDATE) {
            LocalDate legalAgeLimit = LocalDate.now().minusYears(LEGAL_AGE_YEARS);
            if (!date.isAfter(legalAgeLimit)) {
                isDateValid = true;
            }
        }

        return isDateValid;
    }
}