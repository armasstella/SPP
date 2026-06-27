package spp.utils.validation;


import spp.utils.businessconstants.BusinessConstant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;


public class PatternDomainValidator {

    private final List<String> errors = new ArrayList<>();

    public PatternDomainValidator() {

    }

    public boolean validatePattern(String value, Pattern compiledPattern, String errorMessage) {
        boolean isValidPattern;

        if (compiledPattern.matcher(value).matches()) {
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
                Pattern.compile(String.valueOf(BusinessConstant.PATTERN_PASSWORD)),
                BusinessConstant.MESSAGE_INVALID_PASSWORD);

        return isValidPasswordPattern;
    }

    public boolean validateStudentNumber(String studentNumber) {
        boolean isValidStudentNumberPattern;

        isValidStudentNumberPattern = validatePattern(studentNumber,
                Pattern.compile(String.valueOf(BusinessConstant.PATTERN_STUDENT_NUMBER)),
                    BusinessConstant.MESSAGE_INVALID_STUDENT_NUMBER);

        return isValidStudentNumberPattern;
    }

    public boolean validateRfc(String rfc) {
        boolean isValidRfcPattern;

        isValidRfcPattern = validatePattern(rfc,
                Pattern.compile(String.valueOf(BusinessConstant.PATTERN_RFC)),
                BusinessConstant.MESSAGE_INVALID_RFC);

        return isValidRfcPattern;
    }

    public boolean validateTerm(String term) {
        boolean isValidTermPattern;

        isValidTermPattern = validatePattern(term,
                Pattern.compile(String.valueOf(BusinessConstant.PATTERN_TERM)),
                BusinessConstant.MESSAGE_INVALID_TERM);

        return isValidTermPattern;
    }

    public List<String> getPatternsErrors() {
        return Collections.unmodifiableList(errors);
    }
}
