package spp.businesslogic.dao;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ILinkedOrganizationDAO;
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

public class LinkedOrganizationDAO implements ILinkedOrganizationDAO {

    public LinkedOrganizationDAO() {
    }

    @Override
    public boolean registerLinkedOrganization(LinkedOrganizationDTO linkedOrganizationDTO) throws DAOException {
        final String INSERT_LINKED_ORGANIZATION = "INSERT INTO Organizaciones_Vinculadas " +
                "(nombre, rfc, direccion, direccion_fiscal, giro, telefono, correo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean isInsertSuccessful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LINKED_ORGANIZATION)) {
                preparedStatement.setString(1, linkedOrganizationDTO.getName());
                preparedStatement.setString(2, linkedOrganizationDTO.getRfc());
                preparedStatement.setString(3, linkedOrganizationDTO.getAddress());
                preparedStatement.setString(4, linkedOrganizationDTO.getFiscalAddress());
                preparedStatement.setString(5, linkedOrganizationDTO.getBusiness());
                preparedStatement.setString(6, linkedOrganizationDTO.getPhoneNumber());
                preparedStatement.setString(7, linkedOrganizationDTO.getEmail());
                isInsertSuccessful = preparedStatement.executeUpdate() != DAOResultConstant.NO_ROWS_AFFECTED;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
            throw new DAOException("La organización vinculada que intenta registrar ya existe. Verifique que el RFC " +
                    "o el correo no estén duplicados.", e);

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al registrar la organización vinculada.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al registrar la organización vinculada.", e);
            } else if (SQLStateConstant.TRIGGER_EXCEPTION_CODE.equals(e.getSQLState())) {
                throw new DAOException(e.getMessage());
            } else {
                throw new DAOException("Ocurrió un error al intentar registrar la organización vinculada.", e);
            }
        }

        return isInsertSuccessful;
    }

    @Override
    public List<LinkedOrganizationDTO> findActiveLinkedOrganizationsIdentifiers() throws DAOException {
        final String SELECT_LINKED_ORGANIZATION = "SELECT id_organizacion_vinculada, rfc, nombre " +
                "FROM Organizaciones_Vinculadas";
        List<LinkedOrganizationDTO> linkedOrganizationsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LINKED_ORGANIZATION);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    LinkedOrganizationDTO linkedOrganization = new LinkedOrganizationDTO();
                    linkedOrganization.setId(resultSet.getInt("id_organizacion_vinculada"));
                    linkedOrganization.setRfc(resultSet.getString("rfc"));
                    linkedOrganization.setName(resultSet.getString("nombre"));
                    linkedOrganizationsList.add(linkedOrganization);
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar la lista de organizaciones vinculadas.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al buscar organizaciones vinculadas.", e);
            } else {
                throw new DAOException("Ocurrió un error al obtener las organizaciones vinculadas.", e);
            }
        }

        return linkedOrganizationsList;
    }

    @Override
    public boolean existsLinkedOrganizations() throws DAOException {
        final String CHECK_LINKED_ORGANIZATIONS = "SELECT f_hay_organizaciones_vinculadas()";
        boolean isSearchSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            try (PreparedStatement preparedStatement = connection.prepareStatement(CHECK_LINKED_ORGANIZATIONS);
                 ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    isSearchSuccesful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLTimeoutException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
            throw new DAOException("Tiempo de espera agotado al consultar la disponibilidad de organizaciones " +
                    "vinculadas.", e);

        } catch (SQLException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);

            if (e.getSQLState() != null && e.getSQLState().startsWith(SQLStateConstant.CONNECTION_ERROR_PREFIX)) {
                throw new DAOException("Error de conexión al verificar si existen organizaciones vinculadas.", e);
            } else {
                throw new DAOException("Ocurrió un error al consultar las organizaciones vinculadas.", e);
            }
        }

        return isSearchSuccesful;
    }
}