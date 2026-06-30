package spp.businesslogic.interfaces;


import spp.businesslogic.dto.MessageDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

/**
 * Interfaz DAO para la gestión de mensajería interna entre usuarios del sistema.
 * Proporciona operaciones para enviar mensajes y recuperar mensajes recibidos por un usuario.
 */
public interface IMessageDAO {

    /**
     * Envía un mensaje de un usuario a otro dentro del sistema.
     *
     * Propósito: Persistir el mensaje junto con metadatos (remitente, receptor, asunto, contenido, fecha)
     * para garantizar la entrega, trazabilidad y posterior consulta por el receptor.
     *
     * @param messageDTO DTO que contiene la información del mensaje a enviar (remitente, receptor, asunto, cuerpo, fecha, etc.). No debe ser null y debe contener los campos obligatorios.
     * @return true si el mensaje se guardó y se procesó correctamente; false si la operación falló por reglas de negocio (p. ej. destinatario inexistente).
     * @throws DAOException Si ocurre un error en la capa de persistencia (errores SQL, problemas de conexión) durante el envío.
     */
    boolean sendMessage(MessageDTO messageDTO) throws DAOException;

    /**
     * Recupera la lista de mensajes cuyo receptor es el correo electrónico especificado.
     *
     * Propósito: Obtener los mensajes recibidos por un usuario para mostrarlos en su bandeja de entrada,
     * incluyendo el remitente, asunto, cuerpo y metadatos que permitan ordenarlos o filtrarlos.
     *
     * @param email Correo electrónico del receptor cuyos mensajes se desean recuperar. No debe ser null ni vacío.
     * @return Lista de MessageDTO representando los mensajes recibidos; devuelve una lista vacía si no hay mensajes.
     * @throws DAOException Si ocurre un error al consultar la persistencia o al mapear los resultados.
     */
    List<MessageDTO> findMessagesByReceiverEmail(String email) throws DAOException;

}