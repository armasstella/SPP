package spp.businesslogic.dto;

import spp.utils.validation.PatternDomainValidator;

public class LinkedOrganizationDTO extends BaseDTO {

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

    public LinkedOrganizationDTO() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRfc(String rfc) {
        PatternDomainValidator validator = new PatternDomainValidator();

        if (validator.validateRfc(rfc)) {
            this.rfc = rfc.trim();
        } else {
            addErrors(validator.getPatternsErrors());
        }
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setFiscalAddress(String fiscalAddress) {
        this.fiscalAddress = fiscalAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean setEmail(String email) {
        boolean isValid;
        PatternDomainValidator validator = new PatternDomainValidator();

        if (validator.validateEmail(email)) {
            this.email = email.trim();
            isValid = true;
        } else {
            addErrors(validator.getPatternsErrors());
            isValid = false;
        }

        return isValid;
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

}
