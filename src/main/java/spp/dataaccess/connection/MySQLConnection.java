package spp.dataaccess.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import spp.utils.DatabaseConfiguration;

public class MySQLConnection {

    private static final String URL = DatabaseConfiguration.getServerURL();
    private static final String USER = DatabaseConfiguration.getUser();
    private static final String PASSWORD = DatabaseConfiguration.getPassword();

    public static Connection getConnection () throws SQLException {
        return DriverManager.getConnection (URL, USER, PASSWORD);
    }
}