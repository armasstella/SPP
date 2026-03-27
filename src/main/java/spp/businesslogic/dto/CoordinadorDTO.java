package spp.businesslogic.dto;

public class CoordinadorDTO extends UsuarioDTO{
    private String numeroPersonal;

    public CoordinadorDTO () {

    }

    public CoordinadorDTO (String estado, String ultimaConexion, String primerNombre,
                        String segundoNombre, String primerApellido, String segundoApellido,
                        String correoElectronico, String telefono, String contrasenia,
                        String numeroPersonal) {
        super(estado, ultimaConexion, primerNombre, segundoNombre, primerApellido, segundoApellido,
                correoElectronico, telefono, contrasenia);
        this.numeroPersonal = numeroPersonal;
    }

    public String getNumeroPersonal () {
        return numeroPersonal;
    }

    public void setNumeroPersonal (String numeroPersonal) {
        this.numeroPersonal = numeroPersonal;
    }
}

