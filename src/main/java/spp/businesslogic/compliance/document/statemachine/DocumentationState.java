package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;
import spp.businesslogic.enums.DocumentationPhase;

public interface DocumentationState {
    boolean canUpload(DocumentType type);
    DocumentationState nextPhase();
    DocumentationPhase getDocumentationPhase();
}