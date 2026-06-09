package spp.businesslogic.dto;

import java.util.regex.Pattern;

public class InstructorDTO extends UserDTO {

    private static final int MAX_LENGTH_PERSONAL_NUMBER = 5;
    private static final int MAX_LENGTH_SHIFT = 45;
    private static final String PERSONAL_NUMBER_REGEX = "^[A-Z0-9]{5}$";

    private static final String MISSING_REQUIRED_DATA =
            "Faltan datos del Profesor, por favor proporcione toda la información requerida.";
    private static final String INVALID_PERSONAL_NUMBER =
            "El número de personal debe contener exactamente 5 caracteres alfanuméricos en mayúscula.";

    private int id;
    private String personalNumber;
    private String shift;

    public InstructorDTO() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPersonalNumber(String personalNumber) {
        validatePersonalNumber(personalNumber);
        this.personalNumber = personalNumber;
    }

    public void setShift(String shift) {
        validateShift(shift);
        this.shift = shift;
    }

    public int getId() {
        return id;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public String getShift() {
        return shift;
    }

    private void validatePersonalNumber(String personalNumber) {
        if (isNullOrEmpty(personalNumber)) {
            throw new IllegalArgumentException(MISSING_REQUIRED_DATA);
        }
        validateMaxLength(personalNumber, MAX_LENGTH_PERSONAL_NUMBER, "Número de personal");
        if (!Pattern.matches(PERSONAL_NUMBER_REGEX, personalNumber)) {
            throw new IllegalArgumentException(INVALID_PERSONAL_NUMBER);
        }
    }

    private void validateShift(String shift) {
        if (isNullOrEmpty(shift)) {
            throw new IllegalArgumentException("El campo Turno no debe estar vacío.");
        }
        validateMaxLength(shift, MAX_LENGTH_SHIFT, "Turno");
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe exceder "
                    + maxLength + " caracteres.");
        }
    }

    @Override
    public String toString() {
        return personalNumber + " | " + getFirstName();
    }
}