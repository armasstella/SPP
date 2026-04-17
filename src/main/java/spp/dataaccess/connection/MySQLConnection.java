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

    private static MySQLConnection instance;
    private Connection connection;

    private MySQLConnection() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static MySQLConnection getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed()) {
            instance = new MySQLConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = MySQLConnection.getInstance().getConnection();
            if (connection != null) {
                System.out.println("Conexión exitosa a la base de datos");
            } else {
                System.out.println("La conexión es null, revisa tus credenciales");
            }

        } catch (SQLException e) {
            AppLogger.logError(e);
            System.out.println("Error al conectar: " + e.getMessage());
        }
    }

}