package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ProjectManagerDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de responsables de proyecto (Project Managers).
 * Declara las operaciones necesarias para registrar, listar y consultar project managers
 * en relación con organizaciones vinculadas y el estado de su registro.
 */
public interface IProjectManagerDAO {

    /**
     * Registra un responsable de proyecto y lo asocia a una organización vinculada.
     *
     * Propósito: Persistir los datos del project manager (nombre, correo, número de empleado, contacto, etc.)
     * y asociarlo a la organización externa identificada por linkedOrganizationId para permitir
     * su posterior asignación a proyectos y su inclusión en listados por organización.
     *
     * @param projectManagerDTO DTO que contiene la información del responsable de proyecto a registrar.
     *                          No debe ser null y debe incluir los campos obligatorios.
     * @param linkedOrganizationId Identificador numérico de la organización vinculada a la que pertenece el project manager.
     *                             Debe corresponder a una organización existente.
     * @return true si el registro y la asociación se realizaron correctamente; false si no se pudo registrar por
     * reglas de negocio (por ejemplo, duplicado).
     * @throws DAOException Si ocurre un error en la capa de persistencia al insertar o asociar el registro
     * (errores SQL, problemas de conexión, restricciones).
     */
    boolean registerProjectManager(ProjectManagerDTO projectManagerDTO, int linkedOrganizationId) throws DAOException;

    /**
     * Obtiene la lista de project managers actualmente activos.
     *
     * Propósito: Proveer a la capa de presentación con los responsables de proyecto que
     * pueden asignarse a proyectos o consultarse en interfaces administrativas, filtrando los inactivos.
     *
     * @return Lista de ProjectManagerDTO con los project managers activos; devuelve una lista vacía si no
     * existen registros activos.
     * @throws DAOException Si ocurre un error al consultar la persistencia o al mapear los resultados.
     */
    List<ProjectManagerDTO> getActiveProjectManagers() throws DAOException;

    /**
     * Comprueba si existen project managers registrados en el sistema.
     *
     * Propósito: Saber si la tabla de project managers contiene al menos un registro, útil para
     * inicializaciones o decisiones en flujos administrativos.
     *
     * @return true si existe al menos un project manager registrado; false si no hay ninguno.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de datos.
     */
    boolean existsProjectManagers() throws DAOException;

    /**
     * Recupera los project managers asociados a una organización específica.
     *
     * Propósito: Obtener los responsables de proyecto que pertenecen a la organización indicada,
     * para listados por organización o para asignación de proyectos dentro de esa entidad.
     *
     * @param organizationId Identificador numérico de la organización cuyo conjunto de project managers
     *                       se desea recuperar.
     * @return Lista de ProjectManagerDTO con los responsables pertenecientes a la organización especificada;
     * puede ser vacía si no hay registros.
     * @throws DAOException Si ocurre un error al consultar la persistencia o al mapear los resultados.
     */
    List<ProjectManagerDTO> getProjectManagersByOrganization(int organizationId) throws DAOException;

}
