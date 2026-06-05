package spp.dataaccess.connection;

import spp.utils.logger.AppLogger;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnectionManager {

    private static MySQLConnectionManager instance;
    private Connection connection;

    private MySQLConnectionManager() {

    }

    public static MySQLConnectionManager getInstance() {
        if (instance == null) {
           instance = new MySQLConnectionManager();
        }

        return instance;

    }

    public void rollbackSafe() {
        try {
            this.connection = MySQLConnection.getInstance().getConnection();
            if (this.connection != null) {
                this.connection.rollback();
            }
        } catch (SQLException e) {
            AppLogger.logError(e);
        }

    }

    public void enableAutoCommitConnection() {
        try {
            this.connection = MySQLConnection.getInstance().getConnection();
            if (this.connection != null) {
                this.connection.setAutoCommit(true);
            }
        } catch (SQLException e){
                AppLogger.logError(e);
        }

    }

    public void disableAutoCommitConnection() {
        try {
            this.connection = MySQLConnection.getInstance().getConnection();
            if (this.connection != null) {
                this.connection.setAutoCommit(false);
            }
        } catch (SQLException e){
            AppLogger.logError(e);
        }

    }

}
