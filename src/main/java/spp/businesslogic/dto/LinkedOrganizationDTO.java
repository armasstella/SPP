package spp.businesslogic.dto;

public class LinkedOrganizationDTO {

        private String name;
        private String rfc;
        private String address;
        private String fiscalAddress;
        private String business;
        private String phoneNumber;
        private String email;

        public LinkedOrganizationDTO() {

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
}
