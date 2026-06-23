package spp.businesslogic.dto;

public class CourseDTO {

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
        this.courseCode = courseCode;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getSchoolBlock() {
        return schoolBlock;
    }

    public void setSchoolBlock(int schoolBlock) {
        this.schoolBlock = schoolBlock;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(String courseDetails) {
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
        this.numberOfInterns = numberOfInterns;
    }

}
