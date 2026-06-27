package spp.businesslogic.dto;


public class LoginResultDTO {
    private String userType;
    private boolean success;
    private String message;

    public LoginResultDTO() {

    }

    public String getUserType() {
        return userType;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public LoginResultDTO success(String userType) {
        this.userType = userType;
        this.success = true;
        this.message = "Bienvenido al sistema.";
        return this;
    }

    public LoginResultDTO failure(String message) {
        this.userType = null;
        this.success = false;
        this.message = message;
        return this;
    }

}