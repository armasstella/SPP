package spp.utils.view.datepicker;

import javafx.scene.control.DateCell;
import java.time.LocalDate;

public class RestrictedDateCell extends DateCell {

    private final DateValidationMode validationMode;
    private static final int LEGAL_AGE_YEARS = 18;

    public RestrictedDateCell(DateValidationMode validationMode) {
        this.validationMode = validationMode;
    }

    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        boolean shouldDisable = false;

        if (empty || item == null) {
            shouldDisable = true;
        } else {
            if (this.validationMode == DateValidationMode.LEGAL_AGE_BIRTHDATE) {
                LocalDate legalAgeLimit = LocalDate.now().minusYears(LEGAL_AGE_YEARS);
                if (item.isAfter(legalAgeLimit)) {
                    shouldDisable = true;
                }
            }
        }

        setDisable(shouldDisable);
    }
}