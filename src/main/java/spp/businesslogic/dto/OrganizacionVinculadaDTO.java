package spp.businesslogic.dto;

public class OrganizacionVinculadaDTO {

        private String nombre;
        private String rfc;
        private String direccion;
        private String direccionFiscal;
        private String giro;
        private String telefono;
        private String correoElectronico;
        private String personaResponsable;

        public OrganizacionVinculadaDTO() {

        }

        public OrganizacionVinculadaDTO (String nomnbre, String rfc,
                                        String direccion, String direccionFiscal, String giro,
                                        String telefono, String correoElectronico,
                                        String personaResponsable) {
            this.nombre = nombre;
            this.rfc = rfc;
            this.direccion = direccion;
            this.direccionFiscal = direccionFiscal;
            this.giro = giro;
            this.telefono = telefono;
            this.correoElectronico = correoElectronico;
            this.personaResponsable = personaResponsable;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getRfc() {
            return rfc;
        }

        public void setRfc(String rfc) {
            this.rfc = rfc;
        }

        public String getDireccion() {
            return direccion;
        }

        public void setDireccion(String direccion) {
            this.direccion = direccion;
        }

        public String getDireccionFiscal() {
            return direccionFiscal;
        }

        public void setDireccionFiscal(String direccionFiscal) {
            this.direccionFiscal = direccionFiscal;
        }

        public String getGiro() {
            return giro;
        }

        public void setGiro(String giro) {
            this.giro = giro;
        }

        public String getTelefono() {
            return telefono;
        }

        public void setTelefono(String telefono) {
            this.telefono = telefono;
        }

        public String getCorreoElectronico() {
            return correoElectronico;
        }

        public void setCorreoElectronico(String correoElectronico) {
            this.correoElectronico = correoElectronico;
        }

        public String getPersonaResponsable() {
            return personaResponsable;
        }

        public void setPersonaResponsable(String personaResponsable) {
            this.personaResponsable = personaResponsable;
        }
    }
}
