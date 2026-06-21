package spp.utils.view;


import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import spp.businesslogic.enums.DocumentType;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileChooserUtil {

    public static File selectSingleFile(Window ownerWindow, String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Documentos permitidos (*.pdf, *.docx)", "*.pdf", "*.docx")
        );

        return fileChooser.showOpenDialog(ownerWindow);
    }

    public static File chooseOutputFile(ActionEvent event, DocumentType documentType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo");
        fileChooser.setInitialFileName(documentType + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".pdf");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF", "*.pdf"));
        Window window = ((Node) event.getSource()).getScene().getWindow();
        return fileChooser.showSaveDialog(window);

    }

}
