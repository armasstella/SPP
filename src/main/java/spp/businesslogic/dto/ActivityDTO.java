package spp.businesslogic.dto;

import java.time.LocalDateTime;

public class ActivityDTO {

    private String title;
    private String description;
    private LocalDateTime submissionDate;
    private InstructorDTO instructorDTO;

    public ActivityDTO() {

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setSubmissionDate(LocalDateTime submissionDate) {
        this.submissionDate = submissionDate;
    }

    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public InstructorDTO getInstructorDTO() {
        return instructorDTO;
    }

    public void setInstructorDTO(InstructorDTO instructorDTO) {
        this.instructorDTO = instructorDTO;
    }



}
