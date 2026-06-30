package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ProjectDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de proyectos asociados a la práctica profesional.
 * Define operaciones para registrar, eliminar, actualizar y consultar proyectos relacionados
 * con el término activo.
 */
public interface IProjectDAO {

    /**
     * Registra un nuevo proyecto en la base de datos.
     *
     * Propósito: Persistir la información de un proyecto (título, descripción, responsable, fechas, etc.)
     * para que pueda asignarse a practicantes inscritos en la práctica profesional.
     *
     * @param projectDTO DTO que contiene los datos del proyecto a registrar. No debe ser null y debe incluir los campos
     *                   requeridos por la lógica de negocio.
     * @return El identificador numérico del proyecto registrado si aplica a la implementación; algunas implementaciones
     * pueden devolver un código mayor que 0 para indicar éxito.
     * @throws DAOException Si ocurre un error en la capa de persistencia (violación de restricciones, errores SQL,
     * problemas de conexión) durante el registro.
     */
    int registerProject(ProjectDTO projectDTO) throws DAOException;

    /**
     * Elimina un proyecto existente.
     *
     * Propósito: Borrar un proyecto de la persistencia (o marcarlo como inactivo según la implementación) cuando ya no
     * es válido o fue dado de baja.
     *
     * @param projectDTO DTO que identifica al proyecto a eliminar; normalmente debe contener el id del proyecto.
     * @return true si la eliminación fue exitosa; false si el proyecto no existía o no pudo eliminarse por restricciones
     * (por ejemplo, proyectos asignados).
     * @throws DAOException Si ocurre un error durante la operación de borrado en la capa de datos.
     */
    boolean deleteProject(ProjectDTO projectDTO) throws DAOException;

    /**
     * Actualiza los datos de un proyecto existente.
     *
     * Propósito: Aplicar cambios a la información de un proyecto (por ejemplo, modificar descripción, fechas o responsable)
     * y persistir esos cambios en la base de datos.
     *
     * @param projectDTO DTO con el identificador del proyecto y los nuevos valores a actualizar. Debe contener el id del proyecto.
     * @return true si la actualización fue exitosa; false si el proyecto no existe o la actualización viola reglas de negocio.
     * @throws DAOException Si ocurre un error en la capa de persistencia al ejecutar la actualización.
     */
    boolean updateProject(ProjectDTO projectDTO) throws DAOException;

    /**
     * Recupera los detalles de los proyectos correspondientes al término activo.
     *
     * Propósito: Obtener una lista de ProjectDTO con información completa sobre los proyectos disponibles
     * en el término activo para su visualización, asignación a practicantes o generación de reportes.
     *
     * @return Lista de ProjectDTO con los detalles de los proyectos del término activo; devuelve una lista vacía si no hay proyectos.
     * @throws DAOException Si ocurre un error al consultar o mapear los datos desde la capa de persistencia.
     */
    List<ProjectDTO> findProjectsDetailsForActiveTerm() throws DAOException;

    /**
     * Verifica si existe el número mínimo requerido de proyectos para el término activo.
     *
     * Propósito: Asegurar que haya suficientes proyectos disponibles para asignar a los practiciantes inscritos en el término activo,
     * de acuerdo con una regla de negocio que determina el mínimo necesario.
     *
     * @return true si se cumple el mínimo de proyectos para el término activo; false si no se alcanza el mínimo.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de datos.
     */
    boolean hasMinimumProjectsForActiveTerm() throws DAOException;

}
