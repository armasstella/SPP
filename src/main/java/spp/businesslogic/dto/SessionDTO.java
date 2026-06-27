package spp.businesslogic.dto;


public class SessionDTO {

    private final String email;
    private final String activeTerm;

    public SessionDTO(String email, String activeTerm) {
        this.email = email;
        this.activeTerm = activeTerm;
    }

    public String getEmail() {
        return email;
    }

    public String getActiveTerm() {
        return activeTerm;
    }
}
