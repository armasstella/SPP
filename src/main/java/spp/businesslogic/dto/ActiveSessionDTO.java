package spp.businesslogic.dto;


public class ActiveSessionDTO {

    private static SessionDTO sessionDTO = null;
    private static String token = null;

    private ActiveSessionDTO() {

    }

    public static void initialize(SessionDTO session, String generatedToken) {
        sessionDTO = session;
        token = generatedToken;
    }

    public static void close() {
        sessionDTO = null;
        token = null;
    }

    public static SessionDTO get() {
        if (sessionDTO == null) {
            throw new IllegalStateException("No hay sesión activa");
        }
        return sessionDTO;
    }

    public static String getToken() {
        return token;
    }

    public static boolean isActive() {
        return sessionDTO != null;
    }

}
