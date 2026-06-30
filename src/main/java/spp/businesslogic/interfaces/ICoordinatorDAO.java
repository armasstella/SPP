package spp.businesslogic.interfaces;


import spp.businesslogic.dto.CoordinatorDTO;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;

/**
 * Interfaz DAO para la gestión de coordinadores.
 * Define las operaciones de persistencia relacionadas con registro, desactivación,
 * comprobación por número personal y obtención de coordinadores activos.
 */
public interface ICoordinatorDAO {

    /**
     * Registra un nuevo coordinador en la base de datos.
     *
     * Propósito: Persistir la información de un coordinador (datos personales, número de personal,
     * correo, cargo, etc.) para que pueda gestionarse en el sistema.
     *
     * @param coordinatorDTO DTO que contiene los datos del coordinador a registrar. No debe ser null y debe incluir
     *                       los campos obligatorios.
     * @return true si el registro se realizó correctamente; false si no se pudo crear por reglas de negocio
     * (por ejemplo, duplicado).
     * @throws DAOException Si ocurre un error en la capa de acceso a datos (problemas de conexión, restricciones,
     * errores SQL).
     */
    boolean registerCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;

    /**
     * Desactiva un coordinador existente.
     *
     * Propósito: Marcar como inactivo a un coordinador (sin eliminar físicamente el registro) para que deje de aparecer
     * en listados activos y no pueda participar en procesos dependientes de coordinadores.
     *
     * @param coordinatorDTO DTO que identifica al coordinador a desactivar; típicamente debe contener el id o número
     *                       de personal.
     * @return true si la desactivación se realizó correctamente; false si el coordinador no existía o no pudo
     * desactivarse por reglas de negocio.
     * @throws DAOException Si hay errores al actualizar el estado en la capa de persistencia.
     */
    boolean deactivateCoordinator(CoordinatorDTO coordinatorDTO) throws DAOException;

    /**
     * Comprueba si existe un coordinador activo con el número de personal proporcionado.
     *
     * Propósito: Evitar duplicados al registrar nuevos coordinadores y permitir validaciones donde el número de
     * personal debe ser único entre coordinadores activos.
     *
     * @param personalNumber Número de personal del coordinador a buscar. No debe ser null ni vacío.
     * @return true si existe un coordinador activo con ese número; false en caso contrario.
     * @throws DAOException Si ocurre un error durante la consulta en la capa de datos.
     */
    boolean existsActiveCoordinatorByPersonalNumber(String personalNumber) throws DAOException;

    /**
     * Obtiene la lista de coordinadores actualmente activos en el sistema.
     *
     * Propósito: Proveer a la capa de presentación con los coordinadores que pueden participar
     * en procesos operativos (asignaciones, aprobaciones, listados), filtrando los inactivos.
     *
     * @return Lista de CoordinatorDTO que representan a los coordinadores activos; devuelve una lista vacía
     * si no hay ninguno.
     * @throws DAOException Si ocurre un error al consultar la persistencia o mapear los resultados.
     */
    List<CoordinatorDTO> getActiveCoordinators() throws DAOException;

}
