package spp.businesslogic.interfaces;


import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.enums.DocumentationPhase;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de practicantes.
 * Declara las operaciones de persistencia necesarias para registrar, verificar existencia,
 * desactivar y consultar practicantes, así como operaciones específicas de asignación.
 */
public interface IInternDAO{

    /**
     * Registra un nuevo practicante en el sistema.
     *
     * Propósito: Persistir los datos del practicante (nombre, correo, número de matrícula,
     * programa, contacto, entre otros) para que pueda participar en procesos de práctica profesional.
     *
     * @param internDTO DTO que contiene la información necesaria para el registro del practicante. No debe ser null y debe incluir los campos obligatorios.
     * @return true si el registro fue exitoso; false si la operación no se completó por reglas de negocio (por ejemplo, duplicado).
     * @throws DAOException Si ocurre un error en la capa de acceso a datos (conexión, restricciones, errores SQL) durante el registro.
     */
    boolean registerIntern(InternDTO internDTO) throws DAOException;

    /**
     * Verifica si existe un practicante registrado con el número de matrícula indicado.
     *
     * Propósito: Comprobar la existencia de un practicante por su número de matrícula para evitar duplicados
     * o validar operaciones dependientes del registro del practicante.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante a verificar. No debe ser null ni vacío.
     * @return true si existe un practicante con ese número; false en caso contrario.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de persistencia.
     */
    boolean existsStudentByStudentNumber(String studentNumber) throws DAOException;

    /**
     * Desactiva a un practicante registrado.
     *
     * Propósito: Marcar como inactivo al practicante (sin borrar físicamente el registro) para que deje de aparecer
     * en listados activos y no pueda participar en nuevos procesos, manteniendo la trazabilidad histórica.
     *
     * @param internDTO DTO que identifica al practicante a desactivar; normalmente contiene el id o número de matrícula.
     * @return true si la desactivación fue exitosa; false si el practicante no existía o no pudo desactivarse por restricciones.
     * @throws DAOException Si ocurre un error al actualizar el estado en la persistencia.
     */
    boolean deactivateIntern(InternDTO internDTO) throws DAOException;

    /**
     * Recupera la lista de practicantes actualmente activos en el sistema.
     *
     * Propósito: Proveer a la capa de presentación con los practicantes que pueden ser asignados
     * a proyectos, cursos o procesos relacionados con la práctica profesional.
     *
     * @return Lista de InternDTO con los practicantes activos; devuelve una lista vacía si no hay registros activos.
     * @throws DAOException Si ocurre un error al consultar la persistencia o al mapear los resultados.
     */
    List<InternDTO> getActiveInterns() throws DAOException;

    /**
     * Busca el número de matrícula activo de un practicante a partir de su correo electrónico.
     *
     * Propósito: Obtener el identificador (número de matrícula) asociado a un correo, útil para
     * vincular sesiones, envíos o realizar búsquedas cuando la entrada conocida es el correo.
     *
     * @param email Correo electrónico del practicante cuyo número de matrícula activo se desea obtener. No debe ser null ni vacío.
     * @return Número de matrícula activo asociado al correo; puede devolver null si no existe un practicante activo con ese correo.
     * @throws DAOException Si ocurre un error al consultar la base de datos.
     */
    String findActiveStudentNumberByEmail(String email) throws DAOException;

    /**
     * Recupera identificadores (y datos básicos) de los practicantes que aún no han sido asignados.
     *
     * Propósito: Obtener la lista de practicantes sin asignación (por ejemplo, sin proyecto o sin curso)
     * para facilitar su asignación manual o automática.
     *
     * @return Lista de InternDTO representando a los practicantes no asignados; puede ser vacía si no hay tales registros.
     * @throws DAOException Si ocurre un error durante la consulta o al mapear los resultados.
     */
    List<InternDTO> findUnassignedInternsIdentifiers() throws DAOException;

    /**
     * Obtiene los practicantes asignados a un profesor identificado por su correo electrónico.
     *
     * Propósito: Recuperar la lista de practicantes que están bajo la responsabilidad de un profesor
     * (por ejemplo, para seguimiento, evaluación o comunicación), facilitando la gestión docente.
     *
     * @param email Correo electrónico del profesor cuya lista de practicantes asignados se desea recuperar. No debe ser null ni vacío.
     * @return Lista de InternDTO con los practicantes asignados al profesor; devuelve una lista vacía si no tiene asignados.
     * @throws DAOException Si ocurre un error en la capa de persistencia al realizar la consulta.
     */
    List<InternDTO> getAssignedInternsByProfessorEmail(String email) throws DAOException;
    DocumentationPhase findCurrentDocumentationPhaseById(int internId) throws DAOException;

}

