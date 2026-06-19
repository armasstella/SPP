package spp.businesslogic.dto;

import java.util.regex.Pattern;

public class LinkedOrganizationDTO {

    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_RFC = 15;
    private static final int MAX_LENGTH_ADDRESS = 50;
    private static final int MAX_LENGTH_CITY = 50;
    private static final int MAX_LENGTH_STATE = 50;
    private static final int MAX_LENGTH_BUSINESS = 30;
    private static final int MAX_LENGTH_PHONE = 10;
    private static final int MAX_LENGTH_EMAIL = 30;

    private static final String RFC_REGEX = "^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$";
    private static final String PHONE_REGEX = "\\d{10}";
    private static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    private static final String MISSING_REQUIRED_DATA =
            "Faltan datos de la Organización Vinculada, por favor proporcione toda la información requerida.";
    private static final String INVALID_RFC = "El RFC debe tener un formato válido (ej. ABC123456XYZ).";
    private static final String INVALID_PHONE = "El número de teléfono debe contener exactamente 10 dígitos numéricos.";
    private static final String INVALID_EMAIL = "El correo electrónico no tiene un formato válido.";

    private int id;
    private String name;
    private String rfc;
    private String address;
    private String fiscalAddress;
    private String city;
    private String state;
    private String business;
    private String phoneNumber;
    private String email;

    public LinkedOrganizationDTO() {}

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        validateRequiredString(name, "Nombre", MAX_LENGTH_NAME);
        this.name = name;
    }

    public void setRfc(String rfc) {
        validateRfc(rfc);
        this.rfc = rfc;
    }

    public void setAddress(String address) {
        validateRequiredString(address, "Dirección", MAX_LENGTH_ADDRESS);
        this.address = address;
    }

    public void setFiscalAddress(String fiscalAddress) {
        validateRequiredString(fiscalAddress, "Dirección fiscal", MAX_LENGTH_ADDRESS);
        this.fiscalAddress = fiscalAddress;
    }

    public void setCity(String city) {
        validateRequiredString(city, "Ciudad", MAX_LENGTH_CITY);
        this.city = city;
    }

    public void setState(String state) {
        validateRequiredString(state, "Estado", MAX_LENGTH_STATE);
        this.state = state;
    }

    public void setBusiness(String business) {
        validateRequiredString(business, "Sector", MAX_LENGTH_BUSINESS);
        this.business = business;
    }

    public void setPhoneNumber(String phoneNumber) {
        validateNotNullOrEmpty(phoneNumber, "Teléfono");
        validateMaxLength(phoneNumber, MAX_LENGTH_PHONE, "Teléfono");
        validateRegex(phoneNumber, PHONE_REGEX, INVALID_PHONE);
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        validateNotNullOrEmpty(email, "Correo electrónico");
        validateMaxLength(email, MAX_LENGTH_EMAIL, "Correo electrónico");
        validateRegex(email, EMAIL_REGEX, INVALID_EMAIL);
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRfc() {
        return rfc;
    }

    public String getAddress() {
        return address;
    }

    public String getFiscalAddress() {
        return fiscalAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getBusiness() {
        return business;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return rfc + " | " + getName();
    }

    private void validateRequiredString(String value, String fieldName, int maxLength) {
        if (isNullOrEmpty(value)) {
            throw new IllegalArgumentException(MISSING_REQUIRED_DATA);
        }
        validateMaxLength(value, maxLength, fieldName);
    }

    private void validateRfc(String rfc) {
        if (isNullOrEmpty(rfc)) {
            throw new IllegalArgumentException(MISSING_REQUIRED_DATA);
        }
        validateMaxLength(rfc, MAX_LENGTH_RFC, "RFC");
        if (!Pattern.matches(RFC_REGEX, rfc)) {
            throw new IllegalArgumentException(INVALID_RFC);
        }
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

    private void validateNotNullOrEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(MISSING_REQUIRED_DATA);
        }
    }

    private void validateRegex(String value, String regex, String errorMessage) {
        if (!Pattern.matches(regex, value)) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}