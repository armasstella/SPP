package spp.businesslogic.dto;

public class LinkedOrganizationDTO {

        private String name;
        private String rfc;
        private String address;
        private String fiscalAddress;
        private String business;
        private String phoneNumber;
        private String email;
        private String personResponsible;

        public LinkedOrganizationDTO() {

        }

        public LinkedOrganizationDTO(String name, String rfc,
                                     String address, String fiscalAddress, String business,
                                     String phoneNumber, String email,
                                     String personResponsible) {
            this.name = name;
            this.rfc = rfc;
            this.address = address;
            this.fiscalAddress = fiscalAddress;
            this.business = business;
            this.phoneNumber = phoneNumber;
            this.email = email;
            this.personResponsible = personResponsible;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRfc() {
            return rfc;
        }

        public void setRfc(String rfc) {
            this.rfc = rfc;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getFiscalAddress() {
            return fiscalAddress;
        }

        public void setFiscalAddress(String fiscalAddress) {
            this.fiscalAddress = fiscalAddress;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPersonResponsible() {
            return personResponsible;
        }

        public void setPersonResponsible(String personResponsible) {
            this.personResponsible = personResponsible;
        }
}
