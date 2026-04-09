package spp.businesslogic.dto;

public class InstructorDTO extends UserDTO {

    private String numeroPersonal;
    private String turno;

    public InstructorDTO() {
        super();
    }

    public InstructorDTO(String estado, String ultimaConexion, String primerNombre,
                         String segundoNombre, String primerApellido, String segundoApellido,
                         String correoElectronico, String telefono, String contrasenia,
                         String numeroPersonal, String turno) {
        super(estado, ultimaConexion, primerNombre, segundoNombre, primerApellido, segundoApellido,
                correoElectronico, telefono, contrasenia);
        this.numeroPersonal = numeroPersonal;
        this.turno = turno;
    }


    public String getNumeroPersonal () {
        return numeroPersonal;
    }

    public void setNumeroPersonal (String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }

    public String getTurno () {
        return turno;
    }

    public void setTurno (String turno) {
        this.turno = turno;
    }
}
