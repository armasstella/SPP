package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InstructorDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de instructores.
 * Define las operaciones de persistencia relacionadas con registro, desactivación,
 * consulta de instructores activos y comprobaciones específicas como asignación de cursos.
 */
public interface IInstructorDAO {

    /**
     * Registra un nuevo instructor en el sistema.
     *
     * Propósito: Persistir los datos del instructor (nombre, correo, número personal, especialidad, contacto, etc.)
     * para que pueda ser asignado a cursos y participar en procesos académicos.
     *
     * @param instructorDTO DTO que contiene la información del instructor a registrar. No debe ser null y debe
     *                      incluir los campos obligatorios.
     * @return true si el registro fue exitoso; false si la operación falló por reglas de negocio (p. ej. duplicado).
     * @throws DAOException Si ocurre un error en la capa de acceso a datos (errores SQL, restricciones, problemas de conexión).
     */
    boolean registerInstructor(InstructorDTO instructorDTO) throws DAOException;

    /**
     * Desactiva a un instructor existente.
     *
     * Propósito: Marcar como inactivo al instructor para que deje de aparecer en listados y no pueda asignarse
     * a nuevos cursos, conservando el registro histórico en la base de datos.
     *
     * @param instructorDTO DTO que identifica al instructor a desactivar; normalmente incluye el id o número personal.
     * @return true si la desactivación se realizó correctamente; false si el instructor no existía o no pudo desactivarse.
     * @throws DAOException Si ocurre un error al actualizar el estado en la persistencia.
     */
    boolean deactivateInstructor(InstructorDTO instructorDTO) throws DAOException;

    /**
     * Recupera la lista de instructores activos en el sistema.
     *
     * Propósito: Proveer a la capa de presentación con los instructores disponibles
     * para asignación a cursos, búsqueda y gestión administrativa.
     *
     * @return Lista de InstructorDTO que representan a los instructores activos; devuelve una lista vacía si no hay registros.
     * @throws DAOException Si ocurre un error al consultar la persistencia o al mapear los resultados.
     */
    List<InstructorDTO> getActiveInstructors() throws DAOException;

    /**
     * Recupera identificadores y datos básicos de los instructores activos.
     *
     * Propósito: Obtener información mínima (por ejemplo id y nombre) de instructores activos para poblar selectores,
     * listas desplegables o para optimizar consultas donde no se requiere toda la entidad.
     *
     * @return Lista de InstructorDTO con los identificadores y datos básicos de instructores activos; puede ser
     * vacía si no existen.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de datos.
     */
    List<InstructorDTO> getActiveInstructorsIdentifiers() throws DAOException;

    /**
     * Busca el número personal activo de un instructor a partir de su correo electrónico.
     *
     * Propósito: Obtener el identificador (número personal) asociado al correo, útil para validaciones,
     * trazabilidad y operaciones que requieren el número como clave.
     *
     * @param email Correo electrónico del instructor cuyo número personal activo se desea obtener. No debe ser null ni vacío.
     * @return Número personal del instructor si existe uno activo con ese correo; puede devolver null si no se encuentra.
     * @throws DAOException Si ocurre un error al consultar la base de datos.
     */
    String findActivePersonalNumberByEmail(String email) throws DAOException;

    /**
     * Comprueba si un instructor tiene al menos un curso asignado.
     *
     * Propósito: Verificar si el instructor identificado por su id está asignado a algún curso,
     * lo cual puede impedir su eliminación o desactivación dependiendo de las reglas de negocio.
     *
     * @param instructorId Identificador numérico del instructor a comprobar.
     * @return true si el instructor tiene al menos un curso asignado; false si no tiene asignaciones.
     * @throws DAOException Si ocurre un error al consultar la persistencia.
     */
    boolean hasInstructorCourseAssigned(int instructorId) throws DAOException;

}
