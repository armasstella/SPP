package spp.businesslogic.dto;

import spp.utils.validation.PatternDomainValidator;

public class UserDTO extends BaseDTO {

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
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public void setSecondLastName(String secondLastName) {
        this.secondLastName = secondLastName;
    }

    public boolean setEmail(String email) {
        boolean isValid;
        PatternDomainValidator validator = new PatternDomainValidator();

        if (validator.validateEmail(email)) {
            this.email = email;
            isValid = true;
        } else {
            addErrors(validator.getPatternsErrors());
            isValid = false;
        }

        return isValid;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean setPassword(String password) {
        boolean isValid;
        PatternDomainValidator validator = new PatternDomainValidator();

        if (validator.validatePassword(password)) {
            this.password = password;
            isValid = true;
        } else {
            addErrors(validator.getPatternsErrors());
            isValid = false;
        }

        return isValid;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastConnection(String lastConnection) {
        this.lastConnection = lastConnection;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public String getLastConnection() {
        return lastConnection;
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
}
