package spp.businesslogic.dao;


import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DAOException;
import spp.businesslogic.interfaces.ILinkedOrganizationDAO;
import spp.dataaccess.connection.MySQLConnection;
import spp.utils.logger.AppLogger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;


public class LinkedOrganizationDAO implements ILinkedOrganizationDAO {

    private static final int NO_ROWS_AFFECTED = 0;

    public LinkedOrganizationDAO() {

    }

    @Override
    public boolean addLinkedOrganization(LinkedOrganizationDTO linkedOrganizationDTO) throws DAOException {
        final String INSERT_LINKED_ORGANIZATION = "INSERT INTO Organizaciones_Vinculadas " +
                "(nombre, rfc, direccion, direccion_fiscal, giro, telefono, correo)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean isAddSuccessful = false;

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
                if (affectedRows == NO_ROWS_AFFECTED) {
                    throw new DAOException("WARN: Fallo al insertar organización vinculada. No se afectaron filas");
                }

                connection.commit();
                isAddSuccessful = true;

            } catch (SQLIntegrityConstraintViolationException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("WARN: Violación de integridad de datos al insertar", e);

            } catch (SQLException e) {
                connection.rollback();
                AppLogger.logError(e);
                throw new DAOException("ERROR: Error general al insertar organización vinculada", e);

            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al insertar organización vinculada", e);
        }

        return isAddSuccessful;

    }

    @Override
    public List<LinkedOrganizationDTO> obtainActiveLinkedOrganizations() throws DAOException {
        final String SELECT_LINKED_ORGANIZATION = "SELECT id_organizacion_vinculada, rfc, nombre " +
                "FROM Organizaciones_Vinculadas";
        List<LinkedOrganizationDTO> linkedOrganizationsList = new ArrayList<>();

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LINKED_ORGANIZATION);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                LinkedOrganizationDTO linkedOrganization = new LinkedOrganizationDTO();
                linkedOrganization.setId(resultSet.getInt("id_organizacion_vinculada"));
                linkedOrganization.setRfc(resultSet.getString("rfc"));
                linkedOrganization.setName(resultSet.getString("nombre"));
                linkedOrganizationsList.add(linkedOrganization);
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar organizaciones vinculadas");
        }

        return linkedOrganizationsList;

    }

    @Override
    public boolean searchLinkedOrganizationRegisters() throws DAOException {
        final String SEARCH_REGISTERS = "SELECT f_hay_organizaciones_vinculadas()";
        boolean isSearchSuccesful = false;

        try {
            Connection connection = MySQLConnection.getInstance().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_REGISTERS);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    isSearchSuccesful = resultSet.getBoolean(1);
                }
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DAOException("FATAL: Error de conexión al buscar organizaciones vinculadas");
        }

        return isSearchSuccesful;

    }

}