package spp.dataaccess.connection;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLConnectionTest {

    @Test
    @DisplayName("Debe establecer conexión exitosamente con credenciales válidas")
    void testConnectionSuccess() {
        assertDoesNotThrow(() -> {
            Connection connection = MySQLConnection.getInstance().getConnection();
            assertNotNull(connection, "La conexión no debe ser null");
            assertFalse(connection.isClosed(), "La conexión debe estar abierta");
        });
    }

    @Test
    @DisplayName("Debe retornar siempre la misma instancia (Singleton)")
    void testSameSingletonInstanceSucces() throws SQLException {
        MySQLConnection instance1 = MySQLConnection.getInstance();
        MySQLConnection instance2 = MySQLConnection.getInstance();
        assertSame(instance1, instance2, "Debe retornar la misma instancia Singleton");
    }

    @Test
    @DisplayName("Debe lanzar SQLException con URL inválida")
    void testConnectionFailsWithBadURL() {
        assertThrows(SQLException.class, () -> {
            DriverManager.getConnection("jdbc:mysql://localhost:9999/nonexistent", "bad_user", "bad_pass");
        }, "Debe lanzar SQLException con credenciales inválidas");
    }

    @Test
    @DisplayName("La conexión debe ser válida (isValid)")
    void testConnectionIsValid() throws SQLException {
        Connection connection = MySQLConnection.getInstance().getConnection();
        assertTrue(connection.isValid(2), "La conexión debe responder en menos de 2 segundos");
    }
}