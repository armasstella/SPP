package spp.businesslogic.dto;

import spp.utils.validation.Validation;

import java.time.LocalDateTime;

public class InternDTO extends UserDTO {

    private int id;
    private String studentNumber;
    private String sex;
    private boolean speaksIndigenousLanguage;
    private String indigenousLanguage;
    private LocalDateTime birthDate;

    public InternDTO() {
        super();
    }

    public boolean setStudentNumber(String studentNumber) {
        boolean isValid;
        Validation validator = new Validation();

        if (validator.validateStudentNumber(studentNumber)) {
            this.studentNumber = studentNumber.trim();
            isValid = true;
        } else {
            addErrors(validator.getErrors());
            isValid = false;
        }

        return isValid;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSpeaksIndigenousLanguage(boolean speaksIndigenousLanguage) {
        this.speaksIndigenousLanguage = speaksIndigenousLanguage;
    }

    public void setIndigenousLanguage(String indigenousLanguage) {
        this.indigenousLanguage = indigenousLanguage;
    }

    public void setBirthDate(LocalDateTime birthDate) {
        this.birthDate = birthDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    public String getStudentNumber() {
        return studentNumber;
    }

    public String getSex() {
        return sex;
    }
    public boolean getSpeaksIndigenousLanguage() {
        return speaksIndigenousLanguage;
    }

    public String getIndigenousLanguage() {
        return indigenousLanguage;
    }

    public LocalDateTime getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return studentNumber + " | " + super.getFullName();
    }
}
