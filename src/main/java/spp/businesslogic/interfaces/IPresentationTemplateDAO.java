package spp.businesslogic.interfaces;

import spp.businesslogic.dto.PresentationTemplateDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para la gestión de plantillas de presentación asociadas a instructores o practicantes.
 * Proporciona operaciones para guardar documentos de plantilla (por ejemplo, presentaciones) en la capa de persistencia.
 */
public interface IPresentationTemplateDAO {

    /**
     * Guarda un documento de plantilla de presentación asociado a un identificador personal.
     *
     * Propósito: Persistir la plantilla (archivo o referencia) que un usuario identificado por su número personal
     * utiliza como base para presentaciones, prácticas o evaluaciones. Esto permite almacenar y recuperar
     * plantillas específicas por usuario.
     *
     * @param personalNumber Número personal del usuario (instructor o practicante) al que pertenece la plantilla. No debe ser null ni vacío.
     * @param presentationTemplateDTO DTO que contiene los datos del documento de plantilla (metadatos, contenido o referencia al archivo). No debe ser null.
     * @return true si el documento se guardó correctamente; false si la operación falló por reglas de negocio (p. ej. validaciones).
     * @throws DAOException Si ocurre un error en la capa de persistencia (problemas de conexión, errores SQL, mapeo) durante el guardado.
     */
    boolean saveDocument(String personalNumber, PresentationTemplateDTO presentationTemplateDTO) throws DAOException;

}
