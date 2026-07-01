package spp.utils.database;


import spp.utils.exceptionmanager.ExceptionLevel;
import spp.utils.logger.AppLogger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DatabaseConfiguration {

    private static Properties properties = new Properties();

    static {
        try {
            InputStream input = DatabaseConfiguration.class.getClassLoader().getResourceAsStream(
                    "mysqlDatabasePair.properties");

            if (input != null) {
                properties.load(input);
            }

        } catch (IOException e) {
            AppLogger.log(ExceptionLevel.FATAL, e);
        }

    }

    public static String getUser() {
        return properties.getProperty("mysqlDatabase.user");

    }

    public static String getPassword() {
        return properties.getProperty("mysqlDatabase.password");

    }

    public static String getServerURL() {
        return properties.getProperty("mysqlDatabase.server");

    }

}
