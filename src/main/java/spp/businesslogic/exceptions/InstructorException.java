package spp.businesslogic.exceptions;

public class InstructorException extends LogicLayerException {
    public InstructorException(String message) {
        super(message);
    }

    public InstructorException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InstructorException insertError(Throwable cause) {
        return new InstructorException("Error al registrar el profesor.", cause);
    }

    public static InstructorException updateError(Throwable cause) {
        return new InstructorException("Error al actualizar el profesor.", cause);
    }

    public static InstructorException deleteError(Throwable cause) {
        return new InstructorException("Error al eliminar el profesor.", cause);
    }

    public static InstructorException fetchError(Throwable cause) {
        return new InstructorException("Error al obtener el profesor.", cause);
    }

    public static InstructorException notFound(Throwable cause) {
        return new InstructorException("No se encontró el profesor indicado.", cause);
    }
}
