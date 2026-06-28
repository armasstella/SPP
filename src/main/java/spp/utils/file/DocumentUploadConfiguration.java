package spp.utils.file;

import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.enums.DocumentationPhase;

public class DocumentUploadConfiguration {
    private final DocumentType type;
    private final String folder;
    private final String prefix;
    private final DocumentationPhase phase;

    public DocumentUploadConfiguration(DocumentType type, String folder, String prefix, DocumentationPhase phase) {
        this.type = type;
        this.folder = folder;
        this.prefix = prefix;
        this.phase = phase;
    }

    public DocumentType getType() {
        return type;
    }

    public String getFolder() {
        return folder;
    }

    public String getPrefix() {
        return prefix;
    }

    public DocumentationPhase getPhase() {
        return phase;
    }
}