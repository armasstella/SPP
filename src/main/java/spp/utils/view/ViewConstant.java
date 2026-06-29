package spp.utils.view;

import java.util.regex.Pattern;

public final class ViewConstant {

    private ViewConstant() {
    }

    public static final Pattern PATTERN_ALPHABETIC = Pattern.compile("[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ\\s]*");
    public static final Pattern PATTERN_NUMERIC = Pattern.compile("\\d*");
    public static final Pattern PATTERN_EMAIL_CHARS = Pattern.compile("[a-zA-Z0-9@._\\-]*");
    public static final Pattern PATTERN_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9áéíóúÁÉÍÓÚüÜñÑ\\s.,#\\-]*");
    public static final Pattern PATTERN_PASSWORD_CHARS = Pattern.compile("[a-zA-Z0-9@$!%*?&]*");

    public static final int MAX_LENGTH_NAME_PART = 30;
    public static final int MAX_LENGTH_PASSWORD = 20;
    public static final int MAX_LENGTH_STUDENT_NUMBER = 9;
    public static final int MAX_LENGTH_PERSONAL_NUMBER = 5;
    public static final int MAX_LENGTH_MORAL_RFC = 12;
    public static final int MAX_LENGTH_NRC = 5;
    public static final int MAX_LENGTH_EMAIL = 30;
    public static final int MAX_LENGTH_PHONE = 10;
    public static final int MAX_LENGTH_ADDRESS = 50;
    public static final int MAX_LENGTH_TERM = 10;
    public static final int MAX_LENGTH_CAPACITY = 2;

    public static final int MAX_LENGTH_INTERN_ACTIVITY_TITLE = 100;

    public static final int MAX_LENGTH_COURSE_DETAILS = 45;
    public static final int MAX_LENGTH_PROJECT_DESCRIPTION = 100;
    public static final int MAX_LENGTH_ACTIVITY_DESCRIPTION = 100;
    public static final int MAX_LENGTH_INTERN_ACTIVITY_DESCRIPTION = 255;

    public static final int MAX_LENGTH_INDIGENOUS_LANGUAGE = 30;
    public static final int MAX_LENGTH_BUSINESS = 30;
    public static final int MAX_LENGTH_MANAGER_RESPONSIBILITY = 30;
    public static final int MAX_LENGTH_MANAGER_ROLE = 20;

    public static final int MAX_LENGTH_TITLE = 50;
    public static final int MAX_LENGTH_DESCRIPTION = 45;

    public static final int MAX_GRADE = 10;
    public static final int MAX_PROGRESS = 100;
    public static final int MAX_CHOSEN_PROJECTS = 3;

    public static final int MIN_LENGTH_NAME = 3;
    public static final int MIN_LENGTH_INDIGENOUS_LANGUAGE_NAME = 4;
    public static final int MIN_LENGTH_PASSWORD = 8;
    public static final int MIN_LENGTH_NRC = 5;
    public static final int MIN_LENGTH_TERM = 10;
    public static final int MIN_LENGTH_CATEGORY = 3;
    public static final int MIN_LENGTH_PHONE = 10;
    public static final int MIN_LENGTH_ADDRESS = 6;
    public static final int MIN_LENGTH_MORAL_RFC = 12;
    public static final int MIN_LENGTH_PERSONAL_NUMBER = 5;
    public static final int MIN_GRADE = 0;

    public static final int ID_ZERO_INVALID = 0;
    public static final int ALLOWED_POSITIVE_NUMERIC_VALUE = 0;
    public static final int EMPTY_FILE = 0;
}