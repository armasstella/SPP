package spp.businesslogic.interfaces;


import spp.businesslogic.exceptions.DAOException;

import java.util.List;

/**
 * Interfaz DAO para la gestión de términos/periodos académicos.
 * Define operaciones de lectura y modificación relacionadas con nombres de término,
 * término activo y cambios de estado en la capa de persistencia.
 */
public interface ITermDAO {

    /**
     * Recupera la lista de nombres de todos los términos almacenados.
     *
     * Propósito: Obtener los nombres de los términos (por ejemplo, semestres o ciclos) para
     * mostrarlos en interfaces de usuario, selectores o para validaciones administrativas.
     *
     * @return Lista de cadenas con los nombres de todos los términos. Devuelve una lista vacía si no existen términos.
     * @throws DAOException Si ocurre un error al acceder a la capa de datos o al mapear los resultados.
     */
    public List<String> findTermNames() throws DAOException;

    /**
     * Obtiene el nombre del término actualmente activo.
     *
     * Propósito: Proveer el nombre del término marcado como activo para operaciones que dependan
     * del contexto temporal (reportes, inscripciones, generación de documentos).
     *
     * @return Nombre del término activo si existe; puede devolver null si no hay término activo.
     * @throws DAOException Si ocurre un error durante la consulta en la capa de persistencia.
     */
    String findActiveTermName() throws DAOException;

    /**
     * Desactiva el término que actualmente está marcado como activo.
     *
     * Propósito: Cambiar el estado del término activo a inactivo (por ejemplo, al cerrar un periodo),
     * asegurando que no quede más de un término activo.
     *
     * @return true si la desactivación se realizó correctamente; false si no existía término activo o no
     * se pudo desactivar por reglas de negocio.
     * @throws DAOException Si ocurre un error al actualizar el estado en la capa de datos.
     */
    boolean deactivateCurrentTerm() throws DAOException;

    /**
     * Inserta un nuevo término con el nombre proporcionado.
     *
     * Propósito: Añadir un nuevo término/periodo al sistema para uso futuro en procesos académicos.
     *
     * @param termName Nombre del término a insertar. No debe ser null ni vacío; debe respetar las reglas de unicidad si aplica.
     * @return true si la inserción fue exitosa; false si no se insertó (p. ej. término duplicado según reglas de negocio).
     * @throws DAOException Si ocurre un error al persistir el nuevo término en la base de datos.
     */
    boolean insertTerm(String termName) throws DAOException;

    /**
     * Obtiene el identificador (id) del término actualmente activo.
     *
     * Propósito: Recuperar el id numérico del término activo para asociar entidades (inscripciones, reportes) y
     * facilitar consultas por llave primaria.
     *
     * @return Identificador numérico del término activo; puede devolver un valor no positivo (según implementación)
     * o -1 si no existe término activo.
     * @throws DAOException Si ocurre un error al consultar la persistencia.
     */
    int findActiveTermId() throws DAOException;

}
