package spp.utils.businessconstants;

public final class BusinessConstant {

    private BusinessConstant() {

    }

    public static final String PATTERN_EMAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PATTERN_PASSWORD =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
    public static final String PATTERN_STUDENT_NUMBER = "^S\\d{8}$";
    public static final String PATTERN_RFC = "^[A-ZÑ&]{3,4}[0-9]{6}[A-Z0-9]{3}$";

    public static final String MESSAGE_INVALID_EMAIL = "El correo electrónico no tiene un formato válido.";
    public static final String MESSAGE_INVALID_PASSWORD = "La contraseña debe tener al menos 8 caracteres, " +
            "incluir una mayúscula, una minúscula, un número y un carácter especial.";
    public static final String MESSAGE_INVALID_STUDENT_NUMBER = "La matrícula no es válida o ya está registrada " +
            "en el sistema.";
    public static final String MESSAGE_INVALID_RFC = "El RFC debe tener un formato válido (ej. ABC123456XYZ).";
}
