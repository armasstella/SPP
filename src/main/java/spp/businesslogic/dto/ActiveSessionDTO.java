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
        return sessionDTO;
    }

    public static boolean isActive() {
        return sessionDTO != null;
    }

}
