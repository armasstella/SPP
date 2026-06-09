package spp.businesslogic.dto;

import java.util.regex.Pattern;

public class CoordinatorDTO extends UserDTO {

    private static final int MAX_LENGTH_PERSONAL_NUMBER = 5;
    private static final String PERSONAL_NUMBER_REGEX = "^[A-Z0-9]{5}$";

    private static final String MISSING_REQUIRED_DATA =
            "Faltan datos del Coordinador, por favor proporcione toda la información requerida.";
    private static final String INVALID_PERSONAL_NUMBER =
            "El número de personal debe contener exactamente 5 caracteres alfanuméricos en mayúscula.";

    private String personalNumber;

    public CoordinatorDTO() {
        super();
    }

    public void setPersonalNumber(String personalNumber) {
        validatePersonalNumber(personalNumber);
        this.personalNumber = personalNumber;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    private void validatePersonalNumber(String personalNumber) {
        if (isNullOrEmpty(personalNumber)) {
            throw new IllegalArgumentException(MISSING_REQUIRED_DATA);
        }
        validateMaxLength(personalNumber);
        if (!Pattern.matches(PERSONAL_NUMBER_REGEX, personalNumber)) {
            throw new IllegalArgumentException(INVALID_PERSONAL_NUMBER);
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void validateMaxLength(String value) {
        if (value != null && value.length() > CoordinatorDTO.MAX_LENGTH_PERSONAL_NUMBER) {
            throw new IllegalArgumentException("El campo " + "Número de personal" + " no debe exceder "
                    + CoordinatorDTO.MAX_LENGTH_PERSONAL_NUMBER + " caracteres.");
        }
    }
}