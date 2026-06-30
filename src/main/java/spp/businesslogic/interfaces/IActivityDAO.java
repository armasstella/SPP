package spp.businesslogic.interfaces;


import spp.businesslogic.dto.ActivityDTO;
import spp.businesslogic.enums.ActivityType;
import spp.businesslogic.exceptions.DAOException;
import java.util.List;

/**
 * Interfaz DAO para operaciones de persistencia relacionadas con actividades.
 * Cada método define una operación CRUD especializada para el manejo de actividades
 * asociadas a practicantes dentro del sistema.
 */
public interface IActivityDAO {

    /**
     * Guarda una actividad asociada a un practicante.
     *
     * Propósito: Persistir en la capa de datos una nueva actividad realizada por un practicante,
     * clasificándola según su tipo para posteriores consultas y reportes.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante. No debe ser null ni vacío.
     * @param activityDTO Objeto DTO que contiene los datos de la actividad a persistir (título, descripción, fecha,
     *                    horas, etc.).
     * @param activityType Enum que especifica el tipo de actividad (por ejemplo, mensual o final) y ayuda a
     *                     clasificarla.
     * @return true si la operación de guardado se realizó correctamente; false si no se pudo guardar por condiciones
     * de negocio (p. ej. duplicado o validación fallida).
     * @throws DAOException Si ocurre un error en la capa de acceso a datos (conexión, SQL, mapeo) o fallas inesperadas
     * durante la operación.
     */
    boolean saveActivityForIntern(String studentNumber, ActivityDTO activityDTO, ActivityType activityType) throws DAOException;

    /**
     * Recupera las actividades mensuales registradas para un practicante.
     *
     * Propósito: Obtener la lista de actividades que corresponden al periodo mensual o al reporte mensual asociado al practicante,
     * para mostrarlas en vistas o para calcular métricas.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuyo historial mensual se consultará.
     * @return Lista de ActivityDTO que representan las actividades mensuales; devuelve una lista vacía si no hay actividades encontradas.
     * @throws DAOException Si ocurre un error al consultar la base de datos o al mapear los resultados.
     */
    List<ActivityDTO> findMonthlyActivitiesByStudentNumber(String studentNumber) throws DAOException;

    /**
     * Recupera las actividades finales (por ejemplo, entregables finales o evaluaciones) para un practicante.
     *
     * Propósito: Obtener las actividades que se consideran finales o de clausura del período de prácticas o evaluación,
     * permitiendo su revisión o inclusión en reportes finales.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuya lista de actividades finales se consultará.
     * @return Lista de ActivityDTO que representan las actividades finales; puede ser vacía si no existen registros.
     * @throws DAOException Si ocurre un error de acceso a datos durante la consulta.
     */
    List<ActivityDTO> findFinalActivitiesByStudentNumber(String studentNumber) throws DAOException;

    /**
     * Actualiza los datos de una actividad existente.
     *
     * Propósito: Aplicar cambios a una actividad previamente persistida (por ejemplo, corregir horas, fechas o descripción),
     * conservando la integridad del registro.
     *
     * @param activity ActivityDTO con el identificador y los nuevos valores a actualizar. Debe contener el id de la
     *                 actividad a modificar.
     * @return true si la actualización fue exitosa; false si la actividad no existe o no se pudo actualizar por
     * reglas de negocio.
     * @throws DAOException Si hay errores en la capa de persistencia al ejecutar la operación de actualización.
     */
    boolean updateActivity(ActivityDTO activity) throws DAOException;

    /**
     * Elimina una actividad por su identificador.
     *
     * Propósito: Borrar permanentemente el registro de actividad indicado por su id en el sistema de persistencia,
     * liberando cualquier relación o recurso asociado según las reglas de negocio.
     *
     * @param idActivity Identificador numérico de la actividad a eliminar. Debe corresponder a una actividad existente.
     * @return true si la eliminación fue exitosa; false si la actividad no existía o no pudo eliminarse por restricciones
     * (p. ej. dependencias).
     * @throws DAOException Si ocurre un error durante la operación de borrado en la capa de datos.
     */
    boolean deleteActivity(int idActivity) throws DAOException;

}

