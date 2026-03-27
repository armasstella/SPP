package spp.businesslogic.domain;

public class Coordinador extends Usuario{

    private String numeroPersonal;

    public Coordinador () {

    }

    public Coordinador (String estado, String ultimaConexion, String primerNombre,
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
