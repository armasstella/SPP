package spp.businesslogic.dto;

import java.time.LocalDateTime;

public class InternDTO extends UserDTO {


    private String studentNumber;
    private String gender;
    private boolean speaksIndigenousLanguage;
    private LocalDateTime birthDate;

    public InternDTO() {
        super();
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setSpeaksIndigenousLanguage(boolean speaksIndigenousLanguage) {
        this.speaksIndigenousLanguage = speaksIndigenousLanguage;
    }

    public boolean getSpeaksIndigenousLanguage() {
        return speaksIndigenousLanguage;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

}