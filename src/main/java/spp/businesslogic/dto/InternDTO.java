package spp.businesslogic.dto;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.Period;
import java.util.regex.Pattern;

public class InternDTO extends UserDTO {

    private static final int MAX_LENGTH_STUDENT_NUMBER = 9;
    private static final int MAX_LENGTH_SEX = 10;
    private static final int MAX_LENGTH_INDIGENOUS_LANG = 30;

    private static final String STUDENT_NUMBER_REGEX = "^S\\d{8}$";
    private static final String INDIGENOUS_LANG_REGEX = "^[\\p{L}\\s]+$";

    private static final String SEX_FEMALE = "Femenino";
    private static final String SEX_MALE = "Masculino";
    private static final String SEX_OTHER = "Otro";

    private static final String DEFAULT_NO_INDIGENOUS = "Ninguna";

    private static final String MISSING_REQUIRED_DATA =
            "Faltan datos del Practicante, por favor proporcione toda la información requerida.";
    private static final String INVALID_STUDENT_NUMBER =
            "La matrícula no es válida o ya está registrada en el sistema.";
    private static final String INVALID_SEX =
            "Debe indicar un valor válido para el campo Sexo.";
    private static final String INVALID_INDIGENOUS_LANGUAGE =
            "Debe indicar si habla una lengua indígena y, en su caso, especificarla correctamente.";
    private static final String INVALID_BIRTH_DATE =
            "La fecha de nacimiento debe ser anterior a la fecha actual y no superar los cien años.";

    private int id;
    private String studentNumber;
    private String sex;
    private boolean speaksIndigenousLanguage;
    private String indigenousLanguage;
    private LocalDateTime birthDate;

    public InternDTO() {
        super();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStudentNumber(String studentNumber) {
        validateStudentNumber(studentNumber);
        this.studentNumber = studentNumber;
    }

    public void setSex(String sex) {
        validateSex(sex);
        this.sex = sex;
    }

    public void setSpeaksIndigenousLanguage(boolean speaksIndigenousLanguage) {
        this.speaksIndigenousLanguage = speaksIndigenousLanguage;
        if (!speaksIndigenousLanguage) {
            this.indigenousLanguage = DEFAULT_NO_INDIGENOUS;
        }
    }

    public void setIndigenousLanguage(String indigenousLanguage) {
        validateIndigenousLanguage(indigenousLanguage);
        this.indigenousLanguage = indigenousLanguage;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        validateBirthDate(birthDate);
        this.birthDate = birthDate;
    }

    public int getId() {
        return id;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getSex() {
        return sex;
    }

    public boolean getSpeaksIndigenousLanguage() {
        return speaksIndigenousLanguage;
    }

    public String getIndigenousLanguage() {
        String result;
        if (!speaksIndigenousLanguage) {
            result = DEFAULT_NO_INDIGENOUS;
        } else {
            result = indigenousLanguage;
        }
        return result;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    private void validateStudentNumber(String studentNumber) {
        if (isNullOrEmpty(studentNumber)) {
            throw new IllegalArgumentException(MISSING_REQUIRED_DATA);
        }
        validateMaxLength(studentNumber, MAX_LENGTH_STUDENT_NUMBER, "Matrícula");
        if (!Pattern.matches(STUDENT_NUMBER_REGEX, studentNumber)) {
            throw new IllegalArgumentException(INVALID_STUDENT_NUMBER);
        }
    }

    private void validateSex(String sex) {
        if (isNullOrEmpty(sex)) {
            throw new IllegalArgumentException(INVALID_SEX);
        }
        validateMaxLength(sex, MAX_LENGTH_SEX, "Sexo");
        boolean isValidSex = sex.equals(SEX_FEMALE) ||
                sex.equals(SEX_MALE) ||
                sex.equals(SEX_OTHER);
        if (!isValidSex) {
            throw new IllegalArgumentException(INVALID_SEX);
        }
    }

    private void validateIndigenousLanguage(String indigenousLanguage) {
        if (speaksIndigenousLanguage) {
            if (isNullOrEmpty(indigenousLanguage)) {
                throw new IllegalArgumentException(INVALID_INDIGENOUS_LANGUAGE);
            }
            validateMaxLength(indigenousLanguage, MAX_LENGTH_INDIGENOUS_LANG, "Lengua indígena");
            if (!Pattern.matches(INDIGENOUS_LANG_REGEX, indigenousLanguage)) {
                throw new IllegalArgumentException(INVALID_INDIGENOUS_LANGUAGE);
            }
        }
    }

    private void validateBirthDate(LocalDateTime birthDate) {
        if (birthDate == null) {
            throw new IllegalArgumentException(MISSING_REQUIRED_DATA);
        }
        LocalDate birthDateOnly = birthDate.toLocalDate();
        LocalDate today = LocalDate.now();
        int age = Period.between(birthDateOnly, today).getYears();
        if (birthDateOnly.isAfter(today) || age > 100) {
            throw new IllegalArgumentException(INVALID_BIRTH_DATE);
        }
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void validateMaxLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe exceder "
                    + maxLength + " caracteres.");
        }
    }
}