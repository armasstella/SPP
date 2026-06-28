package spp.utils.view.filechooser;

import javafx.stage.FileChooser;

public enum AllowedExtension {
    PDF("Documento PDF", "*.pdf"),
    DOCX("Documento Word", "*.docx"),
    PPTX("Presentación PowerPoint", "*.pptx");

    private final String description;
    private final String extensionPattern;

    AllowedExtension(String description, String extensionPattern) {
        this.description = description;
        this.extensionPattern = extensionPattern;
    }

    public String getExtensionPattern() {
        return extensionPattern;
    }

    public FileChooser.ExtensionFilter toJavaFXFilter() {
        return new FileChooser.ExtensionFilter(description + " (" + extensionPattern + ")", extensionPattern);
    }
}