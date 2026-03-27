package spp.dataaccess.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

public class MySQLConnection {

    private static final String url = "";
    private static final String user = "";
    private static final String password = "";

    public static Connection getConnection () throws SQLException {
        return DriverManager.getConnection (url, user, password);
    }
}
