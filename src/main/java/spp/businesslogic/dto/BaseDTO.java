package spp.businesslogic.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BaseDTO {

    private final List<String> validationsErrors = new ArrayList<>();

    protected void addErrors(List<String> errors) {
        this.validationsErrors.addAll(errors);
    }

    public boolean isValid() {
        return validationsErrors.isEmpty();
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(validationsErrors);
    }
}
