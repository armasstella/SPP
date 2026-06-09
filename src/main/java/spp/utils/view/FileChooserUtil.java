package spp.utils.view;


import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FileChooserUtil {

    public static File selectSingleFile(Window ownerWindow, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documentos permitidos (*.pdf, *.docx)", "*.pdf", "*.docx")
        );

        return fileChooser.showOpenDialog(ownerWindow);
    }

}
