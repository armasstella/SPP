package spp.businesslogic.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseDTO {

    private final List<String> validationErrors = new ArrayList<>();

    protected void addErrors(List<String> errors) {
        this.validationErrors.addAll(errors);
    }

    public boolean isValid() {
        return validationErrors.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(validationErrors);
    }
}
