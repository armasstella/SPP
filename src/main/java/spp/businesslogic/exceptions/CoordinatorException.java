package spp.businesslogic.exceptions;

public class CoordinatorException extends LogicLayerException {

    public CoordinatorException(String message) {
        super(message);
    }

    public CoordinatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public static CoordinatorException insertError(Throwable cause) {
        return new CoordinatorException("Error al registrar el coordinador.", cause);
    }

    public static CoordinatorException updateError(Throwable cause) {
        return new CoordinatorException("Error al actualizar el coordinador.", cause);
    }

    public static CoordinatorException deleteError(Throwable cause) {
        return new CoordinatorException("Error al eliminar el coordinador.", cause);
    }

    public static CoordinatorException fetchError(Throwable cause) {
        return new CoordinatorException("Error al obtener el coordinador.", cause);
    }

    public static CoordinatorException notFound(Throwable cause) {
        return new CoordinatorException("No se encontró el coordinador indicado.");
    }
}