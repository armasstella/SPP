package spp.businesslogic.dto;

public class UsuarioDTO {

    private String estado;
    private String ultimaConexion;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String correoElectronico;
    private String telefono;
    private String contrasenia;

    public UsuarioDTO() {

    }

    public UsuarioDTO (String estado, String ultimaConexion, String primerNombre,
                    String segundoNombre, String primerApellido, String segundoApellido,
                    String correoElectronico, String telefono, String contrasenia) {
        this.estado = estado;
        this.ultimaConexion = ultimaConexion;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.contrasenia = contrasenia;
    }

    public String getEstado () {
        return estado;
    }

    public void setEstado (String estado) {
        this.estado = estado;
    }

    public String getUltimaConexion () {
        return ultimaConexion;
    }

    public void setUltimaConexion (String ultimaConexion) {
        this.ultimaConexion = ultimaConexion;
    }

    public String getPrimerNombre () {
        return primerNombre;
    }

    public void setPrimerNombre (String primerNombre) {
        this.primerNombre = primerNombre;
    }

    public String getSegundoNombre () {
        return segundoNombre;
    }

    public void setSegundoNombre (String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }

    public String getPrimerApellido () {
        return primerApellido;
    }

    public void setPrimerApellido (String primerApellido) {
        this.primerApellido = primerApellido;
    }

    public String getSegundoApellido () {
        return segundoApellido;
    }

    public void setSegundoApellido (String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }

    public String getCorreoElectronico () {
        return correoElectronico;
    }

    public void setCorreoElectronico (String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    public String getTelefono () {
        return telefono;
    }

    public void setTelefono (String telefono) {
        this.telefono = telefono;
    }

    public String getContrasenia () {
        return contrasenia;
    }

    public void setContrasenia (String contrasenia) {
        this.contrasenia = contrasenia;
    }
}
