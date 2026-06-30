package spp.businesslogic.interfaces;


import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;


/**
 * Interfaz DAO para la gestión de organizaciones vinculadas.
 * Define las operaciones de persistencia necesarias para registrar y consultar
 * organizaciones externas o vinculadas al sistema.
 */
public interface ILinkedOrganizationDAO {

    /**
     * Registra una nueva organización vinculada en el sistema.
     *
     * Propósito: Persistir los datos de una organización externa (nombre, identificador,
     * dirección, contacto, etc.) para poder asociarla con otras entidades del sistema
     * (por ejemplo, prácticas, convenios o instructores).
     *
     * @param linkedOrganization DTO que contiene la información de la organización a registrar. No debe ser null y debe incluir los campos obligatorios.
     * @return true si la organización se registró correctamente; false si no se pudo registrar por reglas de negocio (por ejemplo, duplicado).
     * @throws DAOException Si ocurre un error en la capa de acceso a datos (problemas de conexión, restricciones, errores SQL) durante la operación.
     */
    boolean registerLinkedOrganization(LinkedOrganizationDTO linkedOrganization) throws DAOException;

    /**
     * Recupera los identificadores (y demás datos contenidos en el DTO) de las organizaciones vinculadas que están activas.
     *
     * Propósito: Obtener la lista de organizaciones activas para mostrarlas en interfaces, selectores o para asociarlas
     * a otros registros (por ejemplo, asignar una organización a una práctica profesional).
     *
     * @return Lista de LinkedOrganizationDTO que representan a las organizaciones vinculadas activas; devuelve una lista vacía si no hay registros.
     * @throws DAOException Si ocurre un error al consultar la persistencia o al mapear los resultados.
     */
    List<LinkedOrganizationDTO> findActiveLinkedOrganizationsIdentifiers() throws DAOException;

    /**
     * Comprueba si existen organizaciones vinculadas registradas en el sistema.
     *
     * Propósito: Saber si la tabla o el conjunto de organizaciones vinculadas contiene al menos un registro,
     * útil para inicializaciones, validaciones o para decidir flujos de UI.
     *
     * @return true si existe al menos una organización vinculada registrada; false si no existen registros.
     * @throws DAOException Si ocurre un error al consultar la capa de datos.
     */
    boolean existsLinkedOrganizations() throws DAOException;

}
