package spp.businesslogic.dao;

import spp.businesslogic.dto.InstructorDTO;
import spp.dataaccess.connection.MySQLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class InstructorDAO {

    public InstructorDAO() {

    }

    public static void addProfesor (InstructorDTO profesorDTO) {

        String sqlUsuario = "INSERT INTO usuario" +
                "(estado, ultima_conexion, nombre, apellidos, " +
                "correo_electronico, telefono, contraseña) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        String sqlProfesor = "INSERT INTO profesor " +
                "(id_usuario, num_personal, turno) "  +
                "VALUES (?, ?, ?)";

        try (Connection connection = MySQLConnection.getConnection ()) {

            connection.setAutoCommit(false);

            int idGenerated = 0;

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlUsuario,
                    Statement.RETURN_GENERATED_KEYS)) {

                preparedStatement.setString(1, profesorDTO.getStatus());
                preparedStatement.setString(2, profesorDTO.getLastConnection());
                preparedStatement.setString(3, profesorDTO.getFirstName() + " "
                        + profesorDTO.getSecondName());
                preparedStatement.setString(4, profesorDTO.getFirstLastName() + " "
                        + profesorDTO.getSecondLastName());
                preparedStatement.setString(5, profesorDTO.getEmail());
                preparedStatement.setString(6, profesorDTO.getPhoneNumber());
                preparedStatement.setString(7, profesorDTO.getPassword());

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

            try (PreparedStatement preparedStatement = connection.prepareStatement(sqlProfesor)) {
                preparedStatement.setInt(1, idGenerated);
                preparedStatement.setString (2, profesorDTO.getNumeroPersonal ());
                preparedStatement.setString (3, profesorDTO.getTurno ());

                int affectedRows = preparedStatement.executeUpdate ();

                if (affectedRows == 0) {
                    throw new SQLException ("Fallo al insertar al profesor. No se afectaron filas.");
                }

            }

            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void main (String[] args) {

        try {
            InstructorDAO.addProfesor(new InstructorDTO("null", "2026-03-27 14:33:08",
                    "Juan", "",
                    "Alfonso", "Rodriguez", "juan@email.com",
                    "2299000011", "contr4s3ñA..", "54321", "Matutino"));

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
