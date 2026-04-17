package spp.businesslogic.dao;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.exceptions.LogicLayerException;
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
    public void addLinkedOrganization(LinkedOrganizationDTO linkedOrganizationDTO) throws DAOException {
        final String INSERT_LINKED_ORGANIZATION = "INSERT INTO organizacionvinculada " +
                "(nombre, rfc, direccion, direccion_fiscal, giro, telefono, correo, persona_responsable, Proyecto_id_proyecto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                preparedStatement.setString(8, linkedOrganizationDTO.getPersonResponsible());
                preparedStatement.setInt(9, 4);

                int affectedRows = preparedStatement.executeUpdate();
                if (affectedRows == 0) {
                    throw new LogicLayerException("Fallo al insertar la organización vinculada. No se afectaron filas.");
                }

                connection.commit();

            } catch (LogicLayerException | SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw DAOException.insertError(e);
            } finally {
                connection.setAutoCommit(true);
                connection.close();
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw DAOException.insertError(e);
        }
    }
}