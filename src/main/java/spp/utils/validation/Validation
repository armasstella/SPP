package spp.utils.validation;


import spp.utils.businessconstants.BusinessConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


public class Validation {

    private final List<String> errors = new ArrayList<>();

    public Validation() {

    }

    public boolean validatePattern(String value, String pattern, String errorMessage) {
        boolean isValidPattern;

        if (Pattern.matches(pattern, value)) {
            isValidPattern = true;
        } else {
            isValidPattern = false;
            errors.add(errorMessage);
        }
        return isValidPattern;
    }

    public boolean validateEmail(String email) {
        boolean isValidEmailPattern;

        isValidEmailPattern = validatePattern(email,
                    BusinessConstant.PATTERN_EMAIL,
                    BusinessConstant.MESSAGE_INVALID_EMAIL);

        return isValidEmailPattern;
    }

    public boolean validatePassword(String password) {
        boolean isValidPasswordPattern;

        isValidPasswordPattern = validatePattern(password,
                    BusinessConstant.PATTERN_PASSWORD,
                    BusinessConstant.MESSAGE_INVALID_PASSWORD);

        return isValidPasswordPattern;
    }

    public boolean validateStudentNumber(String studentNumber) {
        boolean isValidStudentNumberPattern;

        isValidStudentNumberPattern = validatePattern(studentNumber,
                    BusinessConstant.PATTERN_STUDENT_NUMBER,
                    BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER);

        return isValidStudentNumberPattern;
    }

    public boolean validateRfc(String rfc) {
        boolean isValidRfcPattern;

        isValidRfcPattern = validatePattern(rfc,
                BusinessConstant.PATTERN_RFC,
                BusinessConstant.MESSAGE_INVALID_RFC);

        return isValidRfcPattern;
    }

    public List<String> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
