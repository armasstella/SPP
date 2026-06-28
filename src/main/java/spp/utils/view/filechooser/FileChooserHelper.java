package spp.utils.view.filechooser;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import spp.businesslogic.enums.DocumentType;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileChooserHelper {

    private static final FileChooserFactory fileChooserFactory = new FileChooserFactory();
    private static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";

    public static File selectSingleFile(Window ownerWindow, String title, AllowedExtension... allowedExtensions) {
        File selectedFile = null;

        FileChooser openFileDialog = fileChooserFactory.createOpenDialog(title, allowedExtensions);
        File userSelection = openFileDialog.showOpenDialog(ownerWindow);

        if (userSelection != null) {
            selectedFile = userSelection;
        }

        return selectedFile;
    }

    public static File chooseOutputFile(Window ownerWindow, DocumentType documentType, AllowedExtension outputExtension) {
        File targetFile = null;

        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT));
        String extensionSuffix = outputExtension.getExtensionPattern().replace("*", "");
        String generatedInitialFileName = documentType.toString() + currentDate + extensionSuffix;

        FileChooser saveFileDialog = fileChooserFactory.createSaveDialog(
                "Guardar Archivo",
                generatedInitialFileName,
                outputExtension
        );

        File userSelection = saveFileDialog.showSaveDialog(ownerWindow);

        if (userSelection != null) {
            targetFile = userSelection;
        }

        return targetFile;
    }
}