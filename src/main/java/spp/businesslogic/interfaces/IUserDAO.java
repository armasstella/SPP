package spp.businesslogic.interfaces;


import spp.businesslogic.dto.LoginResultDTO;
import spp.businesslogic.dto.UserDTO;
import spp.businesslogic.exceptions.DAOException;

/**
 * Interfaz DAO para operaciones de persistencia relacionadas con usuarios.
 * Cada método se encarga de una operación en la capa de datos: registro, consulta de identificador,
 * autenticación y verificación de existencia de correos.
 */
public interface IUserDAO {

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * Propósito: Persistir los datos de un usuario (por ejemplo nombre, correo, contraseña en su forma segura,
     * rol y demás atributos) y devolver el identificador generado por la base de datos para referencias posteriores.
     *
     * @param userDTO Objeto UserDTO que contiene la información necesaria para el registro (nombre, correo, contraseña, rol, etc.).
     *                No debe ser null y debe contener los campos obligatorios.
     * @return El identificador numérico (id) asignado al usuario recién creado. Un valor menor o igual a 0 puede indicar que no se creó el registro.
     * @throws DAOException Si ocurre un error en la capa de acceso a datos (violación de restricciones, errores de conexión,
     * fallos SQL) durante el registro.
     */
    int registerUser(UserDTO userDTO) throws DAOException;

    /**
     * Obtiene el identificador (id) de un usuario buscando por su correo electrónico.
     *
     * Propósito: Recuperar el id único asociado a un correo para usarlo en otras operaciones (vinculación de entidades,
     * comprobaciones, etc.).
     *
     * @param email Correo electrónico del usuario cuyo id se desea obtener. No debe ser null ni vacío.
     * @return El id del usuario si se encuentra; puede devolver un valor negativo o cero si no existe (dependiendo de
     * la implementación detallada).
     * @throws DAOException Si ocurre un error al consultar la capa de datos.
     */
    int obtainId(String email) throws DAOException;

    /**
     * Realiza la autenticación de un usuario mediante un identificador y una contraseña.
     *
     * Propósito: Validar las credenciales proporcionadas y devolver un LoginResultDTO que contenga el resultado
     * de la autenticación (éxito/fracaso), posibles tokens, roles o mensajes de error para la capa de que lo solicita.
     *
     * @param identifier Identificador usado para el login (por ejemplo, correo o nombre de usuario). No debe ser null.
     * @param password Contraseña en texto plano provista por el usuario; la implementación debe compararla con la
     *                 representación segura almacenada (hash).
     * @return LoginResultDTO con información del resultado de la autenticación (por ejemplo, éxito, token, datos del
     * usuario, mensajes).
     * @throws DAOException Si ocurre un error al acceder a la persistencia o al procesar la validación de credenciales.
     */
    LoginResultDTO login(String identifier, String password) throws DAOException;

    /**
     * Verifica si un correo electrónico ya está registrado en el sistema.
     *
     * Propósito: Evitar registros duplicados y permitir validaciones previas al registro o actualización de un usuario.
     *
     * @param email Correo electrónico a comprobar. No debe ser null ni vacío.
     * @return true si el correo ya existe en la base de datos; false si no está registrado.
     * @throws DAOException Si ocurre un error durante la consulta a la capa de datos.
     */
    boolean existsEmailRegister(String email) throws DAOException;

}
