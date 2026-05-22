package spp.dataaccess.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import spp.utils.database.DatabaseConfiguration;
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

}