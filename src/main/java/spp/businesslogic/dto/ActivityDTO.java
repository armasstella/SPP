package spp.businesslogic.dto;

import java.time.LocalDateTime;

public class ActivityDTO {

    private String title;
    private String description;
    private LocalDateTime deadline;

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

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }


}
