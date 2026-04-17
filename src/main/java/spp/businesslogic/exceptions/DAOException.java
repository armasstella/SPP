package spp.businesslogic.exceptions;

public class DAOException extends Exception {
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DAOException insertError(Throwable cause) {
        return new DAOException("Error al registrar.", cause);
    }

    public static DAOException updateError(Throwable cause) {
        return new DAOException("Error al actualizar.", cause);
    }

    public static DAOException deleteError(Throwable cause) {
        return new DAOException("Error al eliminar.", cause);
    }

    public static DAOException fetchError(Throwable cause) {
        return new DAOException("Error al obtener.", cause);
    }

    public static DAOException notFound(Throwable cause) {
        return new DAOException("No se encontró lo indicado.");
    }
}
