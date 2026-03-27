package spp.businesslogic.domain;

public class Practicante extends Usuario {

    private String matricula;
    private String sexo;
    private String hablaLenguaIndigena;
    private String fechaNacimiento;

    public Practicante () {

    }

    public Practicante (String estado, String ultimaConexion, String primerNombre,
                        String segundoNombre, String primerApellido, String segundoApellido,
                        String correoElectronico, String telefono, String contrasenia,
                        String matricula, String sexo, String hablaLenguaIndigena,
                        String fechaNacimiento) {
        super(estado, ultimaConexion, primerNombre, segundoNombre, primerApellido, segundoApellido,
                correoElectronico, telefono, contrasenia);
        this.matricula = matricula;
        this.sexo = sexo;
        this.hablaLenguaIndigena = hablaLenguaIndigena;
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setMatricula (String matricula) {
        this.matricula = matricula;
    }

    public String getMatricula () {
        return matricula;
    }

    public void setSexo (String sexo) {
        this.matricula = sexo;
    }

    public String getSexo () {
        return sexo;
    }

    public void setHablaLenguaIndigena (String hablaLenguaIndigena) {
        this.hablaLenguaIndigena = hablaLenguaIndigena;
    }

    public String getHablaLenguaIndigena () {
        return hablaLenguaIndigena;
    }


    public void setFechaNacimiento (String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getFechaNacimiento () {
        return fechaNacimiento;
    }

}