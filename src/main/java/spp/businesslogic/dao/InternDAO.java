package spp.businesslogic.dao;

import spp.businesslogic.dto.InternDTO;
import spp.businesslogic.interfaces.IInternDAO;
import spp.dataaccess.connection.MySQLConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class InternDAO implements IInternDAO {

    public InternDAO() {

    }

    @Override
    public void addIntern(InternDTO internDTO) {
        final String INSERT_USER = "INSERT INTO usuario" +
                "(estado, ultima_conexion, nombre, apellidos, " +
                "correo_electronico, telefono, contraseña) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        final String INSERT_INTERN = "INSERT INTO practicante " +
                "(id_usuario, matricula, sexo, habla_lengua_indigena, fecha_nacimiento) "  +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection ()) {

            connection.setAutoCommit(false);

            int idGenerated = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, internDTO.getStatus());
                preparedStatement.setString(2, internDTO.getLastConnection());
                preparedStatement.setString(3, internDTO.getFirstName() + " " + internDTO.getSecondName());
                preparedStatement.setString(4, internDTO.getFirstLastName() + " " + internDTO.getSecondLastName());
                preparedStatement.setString(5, internDTO.getEmail());
                preparedStatement.setString(6, internDTO.getPhoneNumber());
                preparedStatement.setString(7, internDTO.getPassword());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new SQLException("Fallo al insertar el usuario. No se afectaron filas.");
                }

                try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {

                    if (!resultSet.next()) {
                        throw new SQLException("No se generó ninguna llave");
                    }
                    idGenerated = resultSet.getInt(1);

                }

            }

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INTERN)) {
                preparedStatement.setInt(1, idGenerated);
                preparedStatement.setString (2, internDTO.getStudentNumber());
                preparedStatement.setString (3, internDTO.getGender());
                preparedStatement.setBoolean (4, internDTO.getSpeaksIndigenousLanguage());
                preparedStatement.setTimestamp (5, Timestamp.valueOf(internDTO.getFechaNacimiento()));

                int affectedRows = preparedStatement.executeUpdate ();

                if (affectedRows == 0) {
                    throw new SQLException ("Fallo al insertar al practicante. No se afectaron filas.");
                }

            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
