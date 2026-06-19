package spp.businesslogic.dto;

public class ProfessionalPracticeEnrollmentDTO {

    private static final int MIN_GRADE = 0;
    private static final int MAX_GRADE = 10;
    private static final int MIN_HOURS = 0;

    private CourseDTO courseDTO;
    private InternDTO internDTO;
    private int finalGrade;
    private ProjectDTO projectDTO;
    private int coveredHours;

    public ProfessionalPracticeEnrollmentDTO() {
    }

    public void setInternDTO(InternDTO internDTO) {
        validateNotNull(internDTO, "Practicante");
        this.internDTO = internDTO;
    }

    public InternDTO getInternDTO() {
        return internDTO;
    }

    public void setFinalGrade(int finalGrade) {
        if (finalGrade < MIN_GRADE || finalGrade > MAX_GRADE) {
            throw new IllegalArgumentException("La calificación final debe estar entre " +
                    MIN_GRADE + " y " + MAX_GRADE + ".");
        }
        this.finalGrade = finalGrade;
    }

    public int getFinalGrade() {
        return finalGrade;
    }

    public void setProjectDTO(ProjectDTO projectDTO) {
        this.projectDTO = projectDTO;
    }

    public ProjectDTO getProjectDTO() {
        return projectDTO;
    }

    public void setCoveredHours(int coveredHours) {
        if (coveredHours < MIN_HOURS) {
            throw new IllegalArgumentException("Las horas cubiertas no pueden ser un número negativo.");
        }
        this.coveredHours = coveredHours;
    }

    public int getCoveredHours() {
        return coveredHours;
    }

    public void setCourseDTO(CourseDTO courseDTO) {
        validateNotNull(courseDTO, "Experiencia Educativa");
        this.courseDTO = courseDTO;
    }

    public CourseDTO getCourseDTO() {
        return courseDTO;
    }

    protected void validateNotNull(Object object, String fieldName) {
        if (object == null) {
            throw new IllegalArgumentException("Debe asignar un " + fieldName + " válido.");
        }
    }
}