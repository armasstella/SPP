package spp.businesslogic.interfaces;

import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

/**
 * Interfaz DAO para la gestión de proyectos priorizados por practicantes.
 * Provee operaciones para guardar las prioridades seleccionadas por un practicante,
 * comprobar si existen priorizaciones y recuperar las prioridades asociadas a un practicante.
 */
public interface IPrioritizedProjectDAO {

    /**
     * Guarda la lista de proyectos priorizados por un practicante identificado por correo.
     *
     * Propósito: Persistir la preferencia ordenada de proyectos que un practicante
     * ha seleccionado como prioritarios para su asignación. La lista suele representar un
     * orden de preferencia (primera opción, segunda opción, ...).
     *
     * @param email Correo electrónico del practicante que está priorizando los proyectos. No debe ser null ni vacío.
     * @param priotitizedProjectsList Lista de ProjectDTO que contiene los proyectos en el orden de prioridad.
     *                                Cada elemento debe tener al menos el identificador del proyecto.
     * @return true si la operación de guardado se realizó con éxito; false si la operación no se completó por
     * reglas de negocio (p. ej. validaciones o conflictos).
     * @throws DAOException Si ocurre un error en la capa de persistencia (conexión, errores SQL, mapeo) durante la operación.
     */
    boolean savePrioritizedProjects(String email, List<ProjectDTO> priotitizedProjectsList) throws DAOException;

    /**
     * Comprueba si existen proyectos priorizados para un practicante identificado por correo.
     *
     * Propósito: Verificar la existencia de una priorización registrada para determinar si
     * se deben mostrar opciones de edición, registro o si ya existe una preferencia previa.
     *
     * @param email Correo electrónico del practicante a consultar. No debe ser null ni vacío.
     * @return true si existen proyectos priorizados registrados para ese correo; false si no existen.
     * @throws DAOException Si ocurre un error al consultar la capa de datos.
     */
    boolean findPrioritizedProjectsByInternEmail(String email) throws DAOException;

    /**
     * Recupera la lista de proyectos priorizados para un practicante identificado por su número de matrícula.
     *
     * Propósito: Obtener los ProjectDTO que representan las prioridades seleccionadas por el practicante,
     * en el orden registrado, para su visualización o para procesar asignaciones automáticas/manuales.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuya priorización se desea obtener. No debe ser null ni vacío.
     * @return Lista de ProjectDTO con las prioridades del practicante; devuelve una lista vacía si no hay priorizaciones.
     * @throws DAOException Si ocurre un error durante la consulta o al mapear los resultados.
     */
    List<ProjectDTO> findPrioritizedProjectsIdentifiersByStudentNumber(String studentNumber) throws DAOException;

}
