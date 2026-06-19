package spp.businesslogic.dto;

import java.util.regex.Pattern;

public class ProjectManagerDTO {

    private static final int MAX_LENGTH_NAME = 50;
    private static final int MAX_LENGTH_ROLE = 50;
    private static final int MAX_LENGTH_RESPONSIBILITY = 100;
    private static final String PHONE_REGEX = "\\d{10}";

    private int id;
    private String firstName;
    private String secondName;
    private String firstLastName;
    private String secondLastName;
    private String responsibility;
    private String role;
    private String phoneNumber;

    public ProjectManagerDTO() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        validateNotEmpty(firstName, "Primer nombre");
        validateStringLength(firstName, MAX_LENGTH_NAME, "Primer nombre");
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        validateStringLength(secondName, MAX_LENGTH_NAME, "Segundo nombre");
        this.secondName = secondName;
    }

    public void setFirstLastName(String firstLastName) {
        validateNotEmpty(firstLastName, "Apellido paterno");
        validateStringLength(firstLastName, MAX_LENGTH_NAME, "Apellido paterno");
        this.firstLastName = firstLastName;
    }

    public void setSecondLastName(String secondLastName) {
        validateStringLength(secondLastName, MAX_LENGTH_NAME, "Apellido materno");
        this.secondLastName = secondLastName;
    }

    public void setResponsibility(String responsibility) {
        validateNotEmpty(responsibility, "Responsabilidad");
        validateStringLength(responsibility, MAX_LENGTH_RESPONSIBILITY, "Responsabilidad");
        this.responsibility = responsibility;
    }

    public void setRole(String role) {
        validateNotEmpty(role, "Rol");
        validateStringLength(role, MAX_LENGTH_ROLE, "Rol");
        this.role = role;
    }

    public void setPhoneNumber(String phoneNumber) {
        validateNotEmpty(phoneNumber, "Teléfono");
        validateRegex(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    protected void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe estar vacío.");
        }
    }

    protected void validateStringLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe exceder " + maxLength + " caracteres.");
        }
    }

    protected void validateRegex(String value) {
        if (value == null || !Pattern.matches(ProjectManagerDTO.PHONE_REGEX, value)) {
            throw new IllegalArgumentException("El número de teléfono debe contener exactamente 10 dígitos numéricos.");
        }
    }

    public int getId() {
        return id;
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

    public String getResponsibility() {
        return responsibility;
    }

    public String getRole() {
        return role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return firstName;
    }
}