package spp.businesslogic.dto;

import java.util.regex.Pattern;

public class UserDTO {

    private static final int MAX_LENGTH_NAME = 30;
    private static final int MAX_LENGTH_EMAIL = 30;
    private static final int MAX_LENGTH_PHONE = 10;
    private static final int MAX_LENGTH_PASSWORD = 255;

    private static final String EMAIL_REGEX =
            "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_REGEX =
            "\\d{10}";
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    private static final String MISSING_REQUIRED_FIELD = "El campo %s no debe estar vacío.";
    private static final String INVALID_EMAIL = "El correo institucional no tiene un formato válido.";
    private static final String INVALID_PHONE = "El número de teléfono debe contener exactamente " +
            "10 dígitos numéricos.";
    private static final String INVALID_PASSWORD = "La contraseña debe tener al menos 8 caracteres, " +
            "incluir una mayúscula, una minúscula, un número y un carácter especial.";
    private static final String EXCEEDS_MAX_LENGTH = "El campo %s no debe exceder %d caracteres.";

    private String status;
    private String lastConnection;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String email;
    private String phoneNumber;
    private String password;

    private String fullName;

    public UserDTO() {
    }

    public void setFirstName(String firstName) {
        this.firstName = validateRequiredString(firstName, "Nombre");
    }

    public void setSecondName(String secondName) {
        this.secondName = validateOptionalString(secondName, "Segundo nombre");
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = validateRequiredString(firstLastName, "Apellido paterno");
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = validateOptionalString(secondLastName, "Apellido materno");
    }

    public void setEmail(String email) {
        validateNotNullOrEmpty(email, "Correo electrónico");
        validateMaxLength(email, MAX_LENGTH_EMAIL, "Correo electrónico");
        validateRegex(email, EMAIL_REGEX, INVALID_EMAIL);
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        validateNotNullOrEmpty(phoneNumber, "Teléfono");
        validateMaxLength(phoneNumber, MAX_LENGTH_PHONE, "Teléfono");
        validateRegex(phoneNumber, PHONE_REGEX, INVALID_PHONE);
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        validateNotNullOrEmpty(password, "Contraseña");
        validateMaxLength(password, MAX_LENGTH_PASSWORD, "Contraseña");
        validateRegex(password, PASSWORD_REGEX, INVALID_PASSWORD);
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastConnection() {
        return lastConnection;
    }

    public void setLastConnection(String lastConnection) {
        this.lastConnection = lastConnection;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public String getSecondLastName() {
        return secondLastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String validateRequiredString(String value, String fieldName) {
        validateNotNullOrEmpty(value, fieldName);
        validateMaxLength(value, MAX_LENGTH_NAME, fieldName);
        return value;
    }

    private String validateOptionalString(String value, String fieldName) {
        if (value != null) {
            validateMaxLength(value, MAX_LENGTH_NAME, fieldName);
        }
        return value;
    }

    private void validateNotNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            String message = String.format(MISSING_REQUIRED_FIELD, fieldName);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            String message = String.format(EXCEEDS_MAX_LENGTH, fieldName, maxLength);
            throw new IllegalArgumentException(message);
        }
    }

    private void validateRegex(String value, String regex, String errorMessage) {
        if (value == null || !Pattern.matches(regex, value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}