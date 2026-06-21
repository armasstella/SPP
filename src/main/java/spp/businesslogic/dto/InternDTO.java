package spp.businesslogic.dto;


import java.time.LocalDateTime;


public class InternDTO extends UserDTO {

    private int id;
    private String studentNumber;
    private String gender;
    private boolean speaksIndigenousLanguage;
    private String indigenousLanguage;
    private LocalDateTime birthDate;

    public InternDTO() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setIndigenousLanguage(String indigenousLanguage) {
        this.indigenousLanguage = indigenousLanguage;
    }

    public String getIndigenousLanguage() {
        return indigenousLanguage;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return studentNumber + " | " + super.getFullName();
    }

}