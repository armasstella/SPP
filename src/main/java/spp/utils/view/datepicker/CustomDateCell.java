package spp.utils.view.datepicker;

import javafx.scene.control.DateCell;
import java.time.LocalDate;

public class CustomDateCell extends DateCell {

    private final DateValidationMode validationMode;
    private final int MIN_YEAR_ANY_DATE = 2020;
    private final int MAX_YEAR_ANY_DATE = 2040;
    private final int MIN_MONTH_ANY_DATE = 1;
    private final int MAX_MONTH_ANY_DATE = 12;
    private final int MIN_DAY_ANY_DATE = 1;
    private final int MAX_DAY_ANY_DATE = 31;
    private final int YEAR_TO_SUBSTRACT = 18;
    private final int MIN_BIRTH_YEAR_DATE = 1980;
    private final int MIN_BIRTH_MONTH_DATE = 1;
    private final int MIN_BIRTH_DAY_DATE = 1;
    private final String INVALID_DATE_STYLE = "-fx-background-color: #e0e0e0; -fx-text-fill: #a0a0a0;";

    public CustomDateCell(DateValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    @Override
    public void updateItem(LocalDate date, boolean empty) {
        super.updateItem(date, empty);

        if (empty) {
            this.setDisable(true);
        } else if (date == null) {
            this.setDisable(true);
        } else {
            boolean isInvalid = false;

            if (this.validationMode == DateValidationMode.ANY_DATE) {
                LocalDate minAnyDate = LocalDate.of(MIN_YEAR_ANY_DATE, MIN_MONTH_ANY_DATE, MIN_DAY_ANY_DATE);
                LocalDate maxAnyDate = LocalDate.of(MAX_YEAR_ANY_DATE, MAX_MONTH_ANY_DATE, MAX_DAY_ANY_DATE);

                boolean isBeforeMin = date.isBefore(minAnyDate);
                boolean isAfterMax = date.isAfter(maxAnyDate);

                if (isBeforeMin) {
                    isInvalid = true;
                } else if (isAfterMax) {
                    isInvalid = true;
                }

            } else if (this.validationMode == DateValidationMode.LEGAL_AGE_BIRTHDATE) {
                LocalDate now = LocalDate.now();
                LocalDate legalAgeLimit = now.minusYears(YEAR_TO_SUBSTRACT);
                LocalDate minBirthDate = LocalDate.of(MIN_BIRTH_YEAR_DATE, MIN_BIRTH_MONTH_DATE, MIN_BIRTH_DAY_DATE);

                boolean isAfterLimit = date.isAfter(legalAgeLimit);
                boolean isBeforeMin = date.isBefore(minBirthDate);

                if (isAfterLimit) {
                    isInvalid = true;
                } else if (isBeforeMin) {
                    isInvalid = true;
                }
            }

            this.setDisable(isInvalid);

            if (isInvalid) {
                this.setStyle(INVALID_DATE_STYLE);
            }
        }
    }
}