package spp.dataaccess.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import spp.utils.DatabaseConfiguration;
import spp.utils.logger.AppLogger;

public class MySQLConnection {

    private static final String URL = DatabaseConfiguration.getServerURL();
    private static final String USER = DatabaseConfiguration.getUser();
    private static final String PASSWORD = DatabaseConfiguration.getPassword();

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection (URL, USER, PASSWORD);
    }

    public static void main(String[] args) {
        MySQLConnection database = new MySQLConnection();
        Connection connection = null;
        try {
            connection = database.getConnection();
            if (connection != null) {
                System.out.println("Conexión exitosa a la base de datos");
            } else {
                System.out.println("La conexión es null, revisa tus credenciales");
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            System.out.println("Error al conectar: " + e.getMessage());
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    AppLogger.logError(e);
                }
            }
        }
    }

}