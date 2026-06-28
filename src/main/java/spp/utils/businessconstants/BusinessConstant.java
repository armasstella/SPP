package spp.utils.businessconstants;

import java.util.regex.Pattern;

public final class BusinessConstant {

    private BusinessConstant() {

    }

    public static final Pattern PATTERN_EMAIL =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    public static final Pattern PATTERN_PASSWORD =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$");
    public static final Pattern PATTERN_STUDENT_NUMBER =
            Pattern.compile("^S\\d{8}$");
    public static final Pattern PATTERN_RFC =
            Pattern.compile("^([A-ZÑ&]{3})(\\d{2})(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])([A-Z0-9]{3})$");
    public static final Pattern PATTERN_TERM =
            Pattern.compile("^(FEBRERO - JULIO|AGOSTO - ENERO) \\d{2}$");

    public static final String MESSAGE_INVALID_EMAIL =
            ("El correo electrónico no tiene un formato válido.");
    public static final String MESSAGE_INVALID_PASSWORD =
            ("La contraseña debe tener al menos 8 caracteres, incluir una mayúscula, una minúscula, un número " +
                    "y un carácter especial.");
    public static final String MESSAGE_INVALID_STUDENT_NUMBER = ("La matrícula no es válida o ya está registrada " +
            "en el sistema.");
    public static final String MESSAGE_INVALID_RFC =
            ("El RFC debe tener un formato válido (ej. ABC123456XYZ).");
    public static final String MESSAGE_INVALID_TERM =
            ("El periodo escolar debe tener un formato válido (ej. FEB-JUL-26 o AGO-ENE-26).");
}
