package spp.businesslogic.dao;

import spp.businesslogic.dto.LinkedOrganizationDTO;
import spp.businesslogic.interfaces.ILinkedOrganizationDAO;
import spp.dataaccess.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LinkedOrganizationDAO implements ILinkedOrganizationDAO {

    public LinkedOrganizationDAO() {

    }

    @Override
    public void addLinkedOrganization (LinkedOrganizationDTO organizacionVinculadaDTO) {

        String sqlOrganizacionVinculada = "INSERT INTO organizacionvinculada" +
                "(nombre, rfc, direccion, direccion_fiscal, giro, telefono, correo, )" +
                "(persona_responsable) " + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement
                    (sqlOrganizacionVinculada)) {

                preparedStatement.setString(1, organizacionVinculadaDTO.getName());
                preparedStatement.setString(2, organizacionVinculadaDTO.getRfc());
                preparedStatement.setString(3, organizacionVinculadaDTO.getAddress());
                preparedStatement.setString(4, organizacionVinculadaDTO.getFiscalAddress());
                preparedStatement.setString(5, organizacionVinculadaDTO.getPhoneNumber());
                preparedStatement.setString(6, organizacionVinculadaDTO.getEmail());
                preparedStatement.setString(7, organizacionVinculadaDTO.getPersonResponsible());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Fallo al insertar el usuario. No se afectaron filas.");
                }

            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
