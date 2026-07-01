package spp.businesslogic.interfaces;


import spp.businesslogic.dto.CourseDTO;
import spp.businesslogic.dto.InternTrackingCourseEnrollmentDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de cursos en el sistema.
 * Contiene operaciones para registrar cursos, consultar estadísticas, asignar instructores
 * y obtener códigos de cursos del término activo.
 */
public interface ICourseDAO {

    /**
     * Registra un curso asociado a un término activo.
     *
     * Propósito: Persistir la información de un curso (código, nombre, créditos, etc.)
     * y asociarlo al término activo indicado para que esté disponible en ese periodo.
     *
     * @param courseDTO DTO que contiene los datos del curso a registrar. Debe incluir los campos obligatorios.
     * @param activeTermId Identificador numérico del término activo al cual se asociará el curso.
     * @return true si el registro fue exitoso; false si no se pudo registrar por reglas de negocio (p. ej. curso duplicado).
     * @throws DAOException Si ocurre un error en la capa de persistencia durante la operación.
     */
    boolean registerCourse(CourseDTO courseDTO, int activeTermId) throws DAOException;

    /**
     * Verifica si existen cursos registrados en el sistema.
     *
     * Propósito: Determinar si la base de datos contiene al menos un curso registrado,
     * útil para inicializaciones o validaciones administrativas.
     *
     * @return true si hay al menos un curso registrado; false si no existen cursos.
     * @throws DAOException Si ocurre un error al consultar la persistencia.
     */
    boolean existsRegisteredCourses() throws DAOException;

    /**
     * Obtiene estadísticas o información agregada de los cursos activos.
     *
     * Propósito: Devolver una lista de CourseDTO con datos relevantes para reportes o paneles
     * (por ejemplo, número de inscritos, instructor asignado, estado), filtrando sólo cursos activos.
     *
     * @return Lista de CourseDTO con la información estadística de cursos activos; puede ser vacía si no hay datos.
     * @throws DAOException Si ocurre un error al recuperar o mapear los datos.
     */
    List<CourseDTO> getActiveCoursesStatistics() throws DAOException;

    /**
     * Asigna un instructor a un curso existente.
     *
     * Propósito: Asociar los datos del instructor al curso indicado en el DTO (por ejemplo, actualizar la columna
     * de instructor),permitiendo que el curso quede vinculado a la persona responsable.
     *
     * @param courseDTO DTO que contiene al menos el identificador del curso y la información del instructor a asignar.
     * @return true si la asignación se realizó correctamente; false si el curso no existe o la asignación viola
     * alguna regla de negocio.
     * @throws DAOException Si ocurre un error en la capa de persistencia al actualizar la relación curso-instructor.
     */
    boolean assignInstructorToCourse(CourseDTO courseDTO) throws DAOException;

    /**
     * Recupera los códigos de los cursos que pertenecen al término activo.
     *
     * Propósito: Proveer una lista de CourseDTO (o solo con código según implementación) para poblar selectores,
     * listas desplegables o para validaciones al crear inscripciones dentro del término activo.
     *
     * @return Lista de CourseDTO representando los cursos del término activo; puede devolverse vacía si no existen cursos.
     * @throws DAOException Si ocurre un error durante la consulta a la persistencia.
     */
    List<CourseDTO> getCourseCodesForActiveTerm() throws DAOException;

    List<InternTrackingCourseEnrollmentDTO> getTrackingByCourseId(int courseId) throws DAOException;

}