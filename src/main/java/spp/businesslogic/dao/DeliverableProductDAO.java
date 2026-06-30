package spp.businesslogic.dao;

import spp.businesslogic.dto.DeliverableProductDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.IDeliverableProductDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.exceptionmanager.SQLStateConstant;
import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class DeliverableProductDAO implements IDeliverableProductDAO {

    @Override
    public boolean saveDeliverableProductForIntern(String studentNumber, DeliverableProductDTO deliverableProductDTO) throws DAOException {
        final String INSERT_PRODUCT = "INSERT INTO productos_entregables " +
                "(nombre, descripcion, avance, observaciones, id_usuario_practicante, matricula) " +
                "SELECT ?, ?, ?, ?, p.id_usuario, p.matricula" +
                "FROM practicantes p WHERE p.matricula = ?";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_PRODUCT)) {
                preparedStatement.setString(1, deliverableProductDTO.getName());
                preparedStatement.setString(2, deliverableProductDTO.getDescription());
                preparedStatement.setInt(3, deliverableProductDTO.getProgress());
                preparedStatement.setString(4, deliverableProductDTO.getObservations());
                preparedStatement.setString(5, studentNumber);

                isInsertSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;

            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("No se pudo registrar el producto entregable. Verifique que los datos ingresados " +
                    "sean válidos y que el practicante exista en el sistema.");

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al registrar el producto entregable.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Error de conexión al registrar el producto entregable", e);
        }

        return isInsertSuccessful;

    }

    @Override
    public List<DeliverableProductDTO> findDeliverableProductsByStudentNumber(String studentNumber) throws DAOException {
        final String SELECT_PRODUCTS = "SELECT id_producto_entregable, nombre, descripcion, avance, observaciones " +
                "FROM productos_entregables WHERE matricula = ?";
        List<DeliverableProductDTO> deliverableProductList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PRODUCTS)) {
                preparedStatement.setString(1, studentNumber);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        deliverableProductList.add(buildDeliverableProductDTOFromResultSet(resultSet));
                    }
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar los productos entregables.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al obtener los productos entregables del practicante.");
            } else {
                throw new DAOException("Ocurrió un error al consultar la lista de productos entregables.");
            }
        }

        return deliverableProductList;
    }

    private DeliverableProductDTO buildDeliverableProductDTOFromResultSet(ResultSet resultSet) throws SQLException {
        DeliverableProductDTO deliverableProductDTO = new DeliverableProductDTO();
        deliverableProductDTO.setId(resultSet.getInt("id_producto_entregable"));
        deliverableProductDTO.setName(resultSet.getString("nombre"));
        deliverableProductDTO.setDescription(resultSet.getString("descripcion"));
        deliverableProductDTO.setProgress(resultSet.getInt("avance"));
        deliverableProductDTO.setObservations(resultSet.getString("observaciones"));

        return deliverableProductDTO;
    }


    public boolean deleteDeliverableProduct(int deliverableProductId) throws DAOException {
        final String DELETE_PRODUCT = "DELETE FROM productos_entregables WHERE id_producto_entregable = ?";
        boolean isDeliverableProductDeleted = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_PRODUCT)) {
                preparedStatement.setInt(1, deliverableProductId);
                isDeliverableProductDeleted = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al eliminar el producto entregable.");

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al eliminar producto entregable.");
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error al intentar eliminar producto entregable.");
            }
        }

        return isDeliverableProductDeleted;

    }

}
