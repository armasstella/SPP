package spp.dataaccess.dao;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ILinkedOrganizationDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class LinkedOrganizationDAO implements ILinkedOrganizationDAO {

    public LinkedOrganizationDAO() {

    }

    @Override
    public boolean addLinkedOrganization(LinkedOrganizationDTO linkedOrganizationDTO) throws DAOException {
        final String INSERT_LINKED_ORGANIZATION = "INSERT INTO Organizaciones_Vinculadas " +
                "(nombre, rfc, direccion, direccion_fiscal, giro, telefono, correo)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LINKED_ORGANIZATION);
                preparedStatement.setString(1, linkedOrganizationDTO.getName());
                preparedStatement.setString(2, linkedOrganizationDTO.getRfc());
                preparedStatement.setString(3, linkedOrganizationDTO.getAddress());
                preparedStatement.setString(4, linkedOrganizationDTO.getFiscalAddress());
                preparedStatement.setString(5, linkedOrganizationDTO.getBusiness());
                preparedStatement.setString(6, linkedOrganizationDTO.getPhoneNumber());
                preparedStatement.setString(7, linkedOrganizationDTO.getEmail());

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new DAOException("Fallo al insertar la organización vinculada. No se afectaron filas.");
                }

                connection.commit();

            } catch (DAOException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error al insertar la organización vinculada", e);
            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error. Datos duplicados al insertar la Organización Vinculada", e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("Error general al insertar la organización vinculada", e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("Error al acceder a la base de datos", e);
        }
        return true;
    }
}