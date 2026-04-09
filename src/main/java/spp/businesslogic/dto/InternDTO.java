package spp.businesslogic.dto;

import java.time.LocalDateTime;

public class InternDTO extends UserDTO {

    private String studentNumber;
    private String gender;
    private boolean speaksIndigenousLanguage;
    private LocalDateTime fechaNacimiento;

    public InternDTO() {
        super();
    }

    public InternDTO(String estado, String ultimaConexion, String primerNombre,
                     String segundoNombre, String primerApellido, String segundoApellido,
                     String correoElectronico, String telefono, String contrasenia,
                     String  matricula, String sexo, boolean hablaLenguaIndigena,
                     LocalDateTime fechaNacimiento) {
        super(estado, ultimaConexion, primerNombre, segundoNombre, primerApellido, segundoApellido,
                correoElectronico, telefono, contrasenia);
        this.studentNumber = matricula;
        this.gender = sexo;
        this.speaksIndigenousLanguage = hablaLenguaIndigena;
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setGender(String gender) {
        this.studentNumber = gender;
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


    public void setFechaNacimiento (LocalDateTime fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public LocalDateTime getFechaNacimiento () {
        return fechaNacimiento;
    }

}