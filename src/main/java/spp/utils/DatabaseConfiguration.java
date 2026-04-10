package spp.utils;

import java.io.InputStream;
import java.util.Properties;

public class DatabaseConfiguration {
    private static Properties properties = new Properties();

    static {

        try {
            InputStream input = DatabaseConfiguration.class.getClassLoader().getResourceAsStream(
                    "mysqlDatabase.properties");

            if (input == null) {
                System.out.println("Archivo de propiedades de base de datos no encontrado.");
            } else  {
                properties.load(input);
            }

        } catch (Exception e) {
            System.out.println("Error al cargar el archivo de propiedades.");

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
