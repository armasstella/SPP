package spp.businesslogic.dao;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.exceptions.DataAccessException;
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
    public void addLinkedOrganization(LinkedOrganizationDTO linkedOrganizationDTO) {
        final String INSERT_LINKED_ORGANIZATION = "INSERT INTO organizacionvinculada " +
                "(nombre, rfc, direccion, direccion_fiscal, giro, telefono, correo, persona_responsable, Proyecto_id_proyecto) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        MySQLConnection database = new MySQLConnection();
        Connection connection = null;

        try {
            connection = database.getConnection();
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
                throw new DataAccessException("Fallo al insertar la organización vinculada. No se afectaron filas.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error de integridad al insertar organización vinculada", e);
        } catch (SQLException e) {
            AppLogger.logError(e);
            throw new DataAccessException("Error al insertar organización vinculada", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    AppLogger.logError(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        LinkedOrganizationDAO linkedOrganizationDAO = new LinkedOrganizationDAO();

        try {
            LinkedOrganizationDTO org = new LinkedOrganizationDTO(
                    "Tecnologías del Golfo S.A. de C.V.",
                    "TGA210315AB9",
                    "Av. Ávila Camacho 123, Xalapa, Ver.",
                    "Av. Ávila Camacho 123, Xalapa, Ver.",
                    "Desarrollo de Software",
                    "2281234567",
                    "contacto@tecgolfo.mx",
                    "Ing. Roberto Méndez"
            );
            linkedOrganizationDAO.addLinkedOrganization(org);
            System.out.println("Organización insertada correctamente.");
        } catch (DataAccessException e) {
            e.printStackTrace();
            System.out.println("Error inesperado: " + e.getMessage());
        }
    }

}