package spp.utils.view.datepicker;

import java.time.LocalDate;

public class DateValidator {

    private static final int MIN_YEAR = 1980;
    private static final int LEGAL_AGE_YEARS = 18;

    public static boolean isDateValid(LocalDate date, DateValidationMode mode) {
        boolean isValid = false;

        if (date != null) {
            int year = date.getYear();
            boolean isAfterOrEqualMinYear = year >= MIN_YEAR;

            if (isAfterOrEqualMinYear) {
                if (mode == DateValidationMode.ANY_DATE) {
                    int currentYear = LocalDate.now().getYear();
                    boolean isBeforeOrEqualCurrentYear = year <= currentYear;

                    if (isBeforeOrEqualCurrentYear) {
                        isValid = true;
                    }
                } else if (mode == DateValidationMode.LEGAL_AGE_BIRTHDATE) {
                    LocalDate legalAgeLimit = LocalDate.now().minusYears(LEGAL_AGE_YEARS);
                    boolean isBeforeOrEqualLegalLimit = !date.isAfter(legalAgeLimit);

                    if (isBeforeOrEqualLegalLimit) {
                        isValid = true;
                    }
                }
            }
        }

        return isValid;
    }
}