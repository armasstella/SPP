package spp.businesslogic.dto;

public class CourseDTO {

    private static final int MAX_LENGTH_TERM = 50;
    private static final int MAX_LENGTH_DETAILS = 500;
    private static final int MIN_POSITIVE_VALUE = 1;
    private static final int MIN_ZERO_VALUE = 0;

    private int idCourse;
    private int courseCode;
    private String term;
    private int schoolBlock;
    private int section;
    private int capacity;
    private String courseDetails;
    private InstructorDTO instructorDTO;
    private int numberOfInterns;

    public CourseDTO() {
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public int getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(int courseCode) {
        if (courseCode < MIN_POSITIVE_VALUE) {
            throw new IllegalArgumentException("El NRC debe ser un número positivo mayor a cero.");
        }
        this.courseCode = courseCode;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        validateNotEmpty(term);
        validateStringLength(term, MAX_LENGTH_TERM, "Periodo");
        this.term = term;
    }

    public int getSchoolBlock() {
        return schoolBlock;
    }

    public void setSchoolBlock(int schoolBlock) {
        if (schoolBlock < MIN_POSITIVE_VALUE) {
            throw new IllegalArgumentException("El bloque debe ser un número positivo mayor a cero.");
        }
        this.schoolBlock = schoolBlock;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        if (section < MIN_POSITIVE_VALUE) {
            throw new IllegalArgumentException("La sección debe ser un número positivo mayor a cero.");
        }
        this.section = section;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        if (capacity < MIN_POSITIVE_VALUE) {
            throw new IllegalArgumentException("El cupo del curso debe ser mayor a cero.");
        }
        this.capacity = capacity;
    }

    public String getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(String courseDetails) {
        if (courseDetails != null) {
            validateStringLength(courseDetails, MAX_LENGTH_DETAILS, "Detalles del curso");
        }
        this.courseDetails = courseDetails;
    }

    public InstructorDTO getInstructorDTO() {
        return instructorDTO;
    }

    public void setInstructorDTO(InstructorDTO instructorDTO) {
        this.instructorDTO = instructorDTO;
    }

    public int getNumberOfInterns() {
        return numberOfInterns;
    }

    public void setNumberOfInterns(int numberOfInterns) {
        if (numberOfInterns < MIN_ZERO_VALUE) {
            throw new IllegalArgumentException("La cantidad de practicantes no puede ser negativa.");
        }
        this.numberOfInterns = numberOfInterns;
    }


    protected void validateNotEmpty(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo Periodo no debe estar vacío.");
        }
    }

    protected void validateStringLength(String value, int maxLength, String fieldName) {
        if (value != null && value.length() > maxLength) {
            throw new IllegalArgumentException("El campo " + fieldName + " no debe exceder " + maxLength + " caracteres.");
        }
    }
}