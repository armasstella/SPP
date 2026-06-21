package spp.businesslogic.dto;


public class ActiveSessionDTO {

    private static SessionDTO sessionDTO = null;

    private ActiveSessionDTO() {

    }

    public static void initialize(SessionDTO session) {
        sessionDTO = session;
    }

    public static void close() {
        sessionDTO = null;
    }

    public static SessionDTO get() {
        if (sessionDTO == null) {
            throw new IllegalStateException("No hay sesión activa");
        }
        return sessionDTO;
    }

    public static boolean isActive() {
        return sessionDTO != null;
    }

}
