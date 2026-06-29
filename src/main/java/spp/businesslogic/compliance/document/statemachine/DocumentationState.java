package spp.businesslogic.compliance.document.statemachine;

import spp.businesslogic.enums.DocumentType;

public interface DocumentationState {
    boolean canUpload(DocumentType type);
    DocumentationState nextPhase();
}