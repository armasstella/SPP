package spp.utils.logger;


import spp.utils.exceptionmanager.ExceptionLevel;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;


public class AppLogger {

    private static final String LOG_DIRECTORY = "logs/";

    public static void log(ExceptionLevel levelException, Exception exception) {
        String currentDate = LocalDate.now().toString();
        String fileName = "log_" + currentDate + ".txt";
        Path path = Paths.get(LOG_DIRECTORY + fileName);

        try {
            Files.createDirectories(Paths.get(LOG_DIRECTORY));

            try (FileWriter writer = new FileWriter(path.toFile(), true)) {
                writer.write(buildLogEntry(levelException, exception));
            }

        } catch (IOException e) {
            AppLogger.log(ExceptionLevel.WARN, e);
        }

    }

    private static String buildLogEntry(ExceptionLevel levelException, Exception exception) {
        return levelException + ": [" + LocalDateTime.now() + "] " +
                exception.getClass().getSimpleName() + ": " +
                exception.getMessage() + "\n";

    }

}