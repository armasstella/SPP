package spp.businesslogic.exceptions;

public class InternException extends LogicLayerException {
    public InternException(String message) {
        super(message);
    }

    public InternException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InternException insertError(Throwable cause) {
        return new InternException("Error al registrar practicante.", cause);
    }

    public static InternException updateError(Throwable cause) {
        return new InternException("Error al actualizar practicante.", cause);
    }

    public static InternException deleteError(Throwable cause) {
        return new InternException("Error al eliminar practicante.", cause);
    }

    public static InternException fetchError(Throwable cause) {
        return new InternException("Error al obtener practicante.", cause);
    }

    public static InternException notFound(Throwable cause) {
        return new InternException("No se encontró el practicante indicado.", cause);
    }
}
