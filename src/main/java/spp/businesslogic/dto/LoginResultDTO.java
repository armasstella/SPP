package spp.businesslogic.dto;

public class LoginResultDTO {
    private final String userType;

    public LoginResultDTO(String userType) {
        this.userType = userType;
    }

    public String getUserType() {
        return userType;
    }
}