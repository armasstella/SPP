package spp.businesslogic.interfaces;

import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.exceptions.DAOException;

import java.util.List;

/**
 * Interfaz DAO para la gestión de productos entregables asociados a practicantes.
 * Define las operaciones de persistencia necesarias para guardar y recuperar productos
 * entregables (artefactos, documentos, código) generados durante la práctica profesional.
 */
public interface IDeliverableProductDAO {

    /**
     * Guarda un producto entregable asociado a un practicante identificado por su matrícula.
     *
     * Propósito: Persistir la información del producto o artefacto (documento, código, presentación, etc.)
     * generado por el practicante como parte de las actividades de la práctica, permitiendo su registro,
     * seguimiento y posterior evaluación.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante que genera el producto entregable.
     *                      No debe ser null ni vacío.
     * @param deliverableProductDTO DTO que contiene los datos del producto entregable (título, descripción, tipo, archivo,
     *                              fecha de entrega, calidad, etc.). No debe ser null.
     * @return true si el producto se guardó correctamente; false si no se pudo almacenar por reglas de negocio (p. ej.
     * validaciones fallidas).
     * @throws DAOException Si ocurre un error en la capa de persistencia (conexión, SQL, mapeo) durante el guardado.
     */
    boolean saveDeliverableProductForIntern(String studentNumber, DeliverableProductDTO deliverableProductDTO) throws DAOException;

    /**
     * Recupera la lista de productos entregables asociados a un practicante identificado por su número de matrícula.
     *
     * Propósito: Obtener todos los artefactos y productos que el practicante ha entregado durante su práctica,
     * facilitando su visualización, descarga, evaluación y generación de reportes sobre el desempeño.
     *
     * @param studentNumber Número de matrícula o identificador único del practicante cuyos productos entregables se
     *                      desean recuperar. No debe ser null ni vacío.
     * @return Lista de DeliverableProductDTO con los productos entregables del practicante; devuelve una lista vacía si
     * no existen registros.
     * @throws DAOException Si ocurre un error al consultar la persistencia o al mapear los resultados.
     */
    List<DeliverableProductDTO> findDeliverableProductsByStudentNumber(String studentNumber) throws DAOException;
}
