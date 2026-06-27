package spp.utils.file;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class FileUtils {

    public static final long MAX_BYTES = 50L * 1024 * 1024;
    public static final double BYTES_PER_MB = 1024.0 * 1024.0;
    public static final List<String> ALLOWED_EXTENSIONS = List.of("pdf", "docx");

    public static String getExtension(String fileName) {
        int lastIndex = fileName.lastIndexOf('.');
        return (lastIndex > 0) ? fileName.substring(lastIndex + 1).toLowerCase() : "";

    }

    public static String generateUniqueName(String identifier, String extension, String prefix) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return prefix + "_" + identifier + "_" + timestamp + "." + extension;

    }

    public static String copyFile(File source, String destinationFolder, String destinationName) throws IOException {
        new File(destinationFolder).mkdirs();
        Path destination = Paths.get(destinationFolder + destinationName);
        Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        return destination.toString();

    }
}